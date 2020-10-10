package com.hz.fruit.master.core.service.impl;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fruit.master.core.common.utils.StringUtil;
import com.hz.fruit.master.core.common.utils.constant.CacheKey;
import com.hz.fruit.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fruit.master.core.mapper.OrderMapper;
import com.hz.fruit.master.core.model.bank.BankModel;
import com.hz.fruit.master.core.model.order.OrderModel;
import com.hz.fruit.master.core.model.strategy.StrategyData;
import com.hz.fruit.master.core.service.OrderService;
import com.hz.fruit.master.util.ComponentUtil;
import com.hz.fruit.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description 任务订单的Service层的实现层
 * @Author yoko
 * @Date 2020/5/21 19:35
 * @Version 1.0
 */
@Service
public class OrderServiceImpl<T> extends BaseServiceImpl<T> implements OrderService<T> {

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    public long TWO_HOUR = 2;

    @Autowired
    private OrderMapper orderMapper;



    public BaseDao<T> getDao() {
        return orderMapper;
    }


    @Override
    public BankModel screenBank(List<BankModel> bankList, String orderMoney, int payType, int orderMoneyLockTime) {
        BankModel bankModel = null;
        for (BankModel bankData : bankList){
            bankModel = getBankData(bankData, orderMoney, payType, orderMoneyLockTime);
            if (bankModel != null && bankModel.getId() != null && bankModel.getId() > 0){
                return bankModel;
            }
        }
        return null;
    }

    @Override
    public int getOrderStatus(OrderModel model) {
        return orderMapper.getOrderStatus(model);
    }

    @Override
    public BankModel screenBankByMoney(List<BankModel> bankList, String orderMoney, int payType, int orderMoneyLockTime, int bankMoneyOut, List<StrategyData> moneyList) {
        BankModel bankModel = null;
        if (bankMoneyOut != 4){
            // 属于小数点金额
            int addAndSubtract = 0;// 金额加减：1表示减，2表示加
            addAndSubtract = HodgepodgeMethod.getAddAndSubtract(bankMoneyOut);
            String money = "";
            if (addAndSubtract == 1){
                // 金额相减
                for (StrategyData strategyData : moneyList){
                    money = StringUtil.getBigDecimalSubtractByStr(orderMoney, strategyData.getStgValue());
                    for (BankModel bankData : bankList){
                        bankModel = getBankData(bankData, money, payType, orderMoneyLockTime);
                        if (bankModel != null && bankModel.getId() != null && bankModel.getId() > 0){
                            return bankModel;
                        }
                    }
                }
            }else if (addAndSubtract == 2){
                // 金额相加
                for (StrategyData strategyData : moneyList){
                    money = StringUtil.getBigDecimalAdd(orderMoney, strategyData.getStgValue());
                    for (BankModel bankData : bankList){
                        bankModel = getBankData(bankData, money, payType, orderMoneyLockTime);
                        if (bankModel != null && bankModel.getId() != null && bankModel.getId() > 0){
                            return bankModel;
                        }
                    }
                }
            }
        }else {
            // 属于整数金额
            for (BankModel bankData : bankList){
                bankModel = getBankData(bankData, orderMoney, payType, orderMoneyLockTime);
                if (bankModel != null && bankModel.getId() != null && bankModel.getId() > 0){
                    return bankModel;
                }
            }
        }



        return null;
    }

    /**
     * @Description: check校验被筛选的银行卡的使用状态
     * @param bankModel - 银行卡数据
     * @param orderMoney - 订单金额
     * @param payType - 支付类型
     * @param orderMoneyLockTime - 银行卡金额的锁定时间
     * @return com.hz.fruit.master.core.model.bank.BankModel
     * @author yoko
     * @date 2020/9/12 21:02
     */
    public BankModel getBankData(BankModel bankModel, String orderMoney, int payType, int orderMoneyLockTime){
        // 判断此银行卡是否被锁住
        String lockKey_bank = CachedKeyUtils.getCacheKey(CacheKey.LOCK_BANK, bankModel.getId());
        boolean flagLock_bank = ComponentUtil.redisIdService.lock(lockKey_bank);
        if (flagLock_bank){
            // 判断此银行卡是否有相同金额正在进行中
            String strKeyCache_bank_order_money = CachedKeyUtils.getCacheKey(CacheKey.BANK_ORDER_MONEY, bankModel.getId(), orderMoney);
            String strCache_bank_order_money = (String) ComponentUtil.redisService.get(strKeyCache_bank_order_money);
            if (StringUtils.isBlank(strCache_bank_order_money)){
                // 表示没有相同金额的挂单

                // 校验银行卡是否受到收款限制
                boolean flag = checkBankLimit(bankModel, payType);
                if (flag){
                    if (!StringUtils.isBlank(bankModel.getOpenTimeSlot())){
                        // 校验银行卡的放量时间
                        boolean flag_openTime = HodgepodgeMethod.checkOpenTimeSlot(bankModel.getOpenTimeSlot());
                        if (flag_openTime){
                            // 锁定此银行卡的金额，存放与redis缓存中
                            String strKeyCache_bank_money = CachedKeyUtils.getCacheKey(CacheKey.BANK_ORDER_MONEY, bankModel.getId(), orderMoney);
                            ComponentUtil.redisService.set(strKeyCache_bank_money, orderMoney, orderMoneyLockTime, TimeUnit.MINUTES);

                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey_bank);
                            bankModel.setDistributionMoney(orderMoney);// 赋值实际派发的金额
                            return bankModel;
                        }
                    }

                }
            }
            // 解锁
            ComponentUtil.redisIdService.delLock(lockKey_bank);
        }
        return null;
    }

    /**
     * @Description: check校验银行卡限量
     * @param bankModel - 银行卡信息
     * @param payType - 支付类型
     * @return
     * @author yoko
     * @date 2020/9/12 21:19
    */
    public boolean checkBankLimit(BankModel bankModel, int payType){
        boolean flag = true;
        if (payType == 1){
            String dayMoney = getRedisDataByKey(CacheKey.WX_IN_DAY_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(dayMoney)){
                return false;
            }
            String monthMoney = getRedisDataByKey(CacheKey.WX_IN_MONTH_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(monthMoney)){
                return false;
            }
            String dayNum = getRedisDataByKey(CacheKey.WX_IN_DAY_NUM, bankModel.getId());
            if (!StringUtils.isBlank(dayNum)){
                return false;
            }
        }else if (payType == 2){
            String dayMoney = getRedisDataByKey(CacheKey.ZFB_IN_DAY_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(dayMoney)){
                return false;
            }
            String monthMoney = getRedisDataByKey(CacheKey.ZFB_IN_MONTH_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(monthMoney)){
                return false;
            }
            String dayNum = getRedisDataByKey(CacheKey.ZFB_IN_DAY_NUM, bankModel.getId());
            if (!StringUtils.isBlank(dayNum)){
                return false;
            }
        }else if (payType == 3){
            String dayMoney = getRedisDataByKey(CacheKey.CARD_IN_DAY_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(dayMoney)){
                return false;
            }
            String monthMoney = getRedisDataByKey(CacheKey.CARD_IN_MONTH_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(monthMoney)){
                return false;
            }
            String dayNum = getRedisDataByKey(CacheKey.CARD_IN_DAY_NUM, bankModel.getId());
            if (!StringUtils.isBlank(dayNum)){
                return false;
            }
        }
        return flag;
    }

    /**
     * @Description: 组装缓存key查询缓存中存在的数据
     * @param cacheKey - 缓存的类型key
     * @param obj - 数据的ID
     * @return
     * @author yoko
     * @date 2020/5/20 14:59
     */
    public String getRedisDataByKey(String cacheKey, Object obj){
        String str = null;
        String strKeyCache = CachedKeyUtils.getCacheKey(cacheKey, obj);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (StringUtils.isBlank(strCache)){
            return str;
        }else{
            str = strCache;
            return str;
        }
    }


}
