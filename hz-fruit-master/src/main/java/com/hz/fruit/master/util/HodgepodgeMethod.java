package com.hz.fruit.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.exception.ServiceException;
import com.hz.fruit.master.core.common.utils.BeanUtils;
import com.hz.fruit.master.core.common.utils.DateUtil;
import com.hz.fruit.master.core.common.utils.StringUtil;
import com.hz.fruit.master.core.common.utils.constant.ErrorCode;
import com.hz.fruit.master.core.model.bank.BankModel;
import com.hz.fruit.master.core.model.channel.ChannelBankModel;
import com.hz.fruit.master.core.model.channel.ChannelModel;
import com.hz.fruit.master.core.model.merchant.MerchantModel;
import com.hz.fruit.master.core.model.mobilecard.MobileCardModel;
import com.hz.fruit.master.core.model.region.RegionModel;
import com.hz.fruit.master.core.model.statistics.StatisticsClickPayModel;
import com.hz.fruit.master.core.model.strategy.StrategyModel;
import com.hz.fruit.master.core.protocol.request.order.ProtocolOrder;
import com.hz.fruit.master.core.protocol.request.statistics.RequestStatisticsClickPay;
import com.hz.fruit.master.core.protocol.response.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Description 公共方法类
 * @Author yoko
 * @Date 2020/1/7 20:25
 * @Version 1.0
 */
public class HodgepodgeMethod {
    private static Logger log = LoggerFactory.getLogger(HodgepodgeMethod.class);

    /**
     * @Description: 组装查询地域的查询条件
     * @param ip
     * @return RegionModel
     * @author yoko
     * @date 2019/12/18 18:41
     */
    public static RegionModel assembleRegionModel(String ip){
        RegionModel resBean = new RegionModel();
        resBean.setIp(ip);
        return resBean;
    }

    /**
     * @Description: 组装获取用户归属的省份跟城市
     * @param regionModel
     * @return
     * @author yoko
     * @date 2020/7/15 19:06
    */
    public static RegionModel getRegion(RegionModel regionModel){
        RegionModel resBean = new RegionModel();
        if (regionModel != null){
            // 获取地域信息
            if (!StringUtils.isBlank(regionModel.getIp())){
                regionModel = ComponentUtil.regionService.getCacheRegion(regionModel);
                resBean.setIp(regionModel.getIp());
                if (!StringUtils.isBlank(regionModel.getProvince())){
                    resBean.setProvince(regionModel.getProvince());
                }
                if (!StringUtils.isBlank(regionModel.getCity())){
                    resBean.setCity(regionModel.getCity());
                }
            }
        }
        return resBean;
    }

    /**
     * @Description: 公共的返回客户端的方法
     * @param stime - 服务器的时间
     * @param token - 登录token
     * @param sign - 签名
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/13 21:45
     */
    public static String assembleResult(long stime, String token, String sign){
        ResponseData dataModel = new ResponseData();
        dataModel.stime = stime;
        if (!StringUtils.isBlank(token)){
            dataModel.token = token;
        }
        dataModel.sign = sign;
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据添加支付用户点击支付页统计的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkClickPayAdd(RequestStatisticsClickPay requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.ST00001.geteCode(), ErrorCode.ENUM_ERROR.ST00001.geteDesc());
        }
        // 校验标识值是否为空
        if (StringUtils.isBlank(requestModel.identif)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.ST00002.geteCode(), ErrorCode.ENUM_ERROR.ST00002.geteDesc());
        }

        // 校验数据来源类型是否为空
        if (requestModel.dataType == null || requestModel.dataType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.ST00003.geteCode(), ErrorCode.ENUM_ERROR.ST00003.geteDesc());
        }
    }

    /**
     * @Description: 组装支付用户点击支付页统计的数据
     * @param requestModel - 用户支付点击数据
     * @param regionModel - 用户的地域信息
     * @return
     * @author yoko
     * @date 2020/7/15 19:10
     */
    public static StatisticsClickPayModel assembleStatisticsClickPayData(RequestStatisticsClickPay requestModel, RegionModel regionModel){
        StatisticsClickPayModel resBean = BeanUtils.copy(requestModel, StatisticsClickPayModel.class);
        if (regionModel != null){
            if (!StringUtils.isBlank(regionModel.getIp())){
                regionModel = ComponentUtil.regionService.getCacheRegion(regionModel);
                resBean.setIp(regionModel.getIp());
                if (!StringUtils.isBlank(regionModel.getProvince())){
                    resBean.setProvince(regionModel.getProvince());
                }
                log.info("");
                if (!StringUtils.isBlank(regionModel.getCity())){
                    resBean.setCity(regionModel.getCity());
                }
            }
        }
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: check校验数据当派发订单的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkOrderAdd(ProtocolOrder requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00001.geteCode(), ErrorCode.ENUM_ERROR.OR00001.geteDesc());
        }

        // 校验金额是否为空
        if (StringUtils.isBlank(requestModel.money)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00002.geteCode(), ErrorCode.ENUM_ERROR.OR00002.geteDesc());
        }else {
            // 金额是否有效
            if (requestModel.money.indexOf(".") > -1){
                boolean flag = StringUtil.isNumberByMoney(requestModel.money);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.OR00006.geteCode(), ErrorCode.ENUM_ERROR.OR00006.geteDesc());
                }
            }else {
                boolean flag = StringUtil.isNumer(requestModel.money);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.OR00007.geteCode(), ErrorCode.ENUM_ERROR.OR00007.geteDesc());
                }
            }
        }

        // 校验支付类型为空
        if (requestModel.payType == null || requestModel.payType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00003.geteCode(), ErrorCode.ENUM_ERROR.OR00003.geteDesc());
        }

        if (StringUtils.isBlank(requestModel.secretKey)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00008.geteCode(), ErrorCode.ENUM_ERROR.OR00008.geteDesc());
        }
    }

    /**
     * @Description: 组装查询策略数据条件的方法
     * @return com.pf.play.rule.core.model.strategy.StrategyModel
     * @author yoko
     * @date 2020/5/19 17:12
     */
    public static StrategyModel assembleStrategyQuery(int stgType){
        StrategyModel resBean = new StrategyModel();
        resBean.setStgType(stgType);
        return resBean;
    }


    /**
     * @Description: 校验策略类型数据:出码开关-判断此时是否属于正常出码
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByQrCodeSwitch(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00001.geteCode(), ErrorCode.ENUM_ERROR.S00001.geteDesc());
        }
        if (strategyModel.getStgNumValue() == 1){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00002.geteCode(), ErrorCode.ENUM_ERROR.S00002.geteDesc());
        }
        if (strategyModel.getStgNumValue() == 2){
            if (StringUtils.isBlank(strategyModel.getStgValue())){
                throw new ServiceException(ErrorCode.ENUM_ERROR.S00003.geteCode(), ErrorCode.ENUM_ERROR.S00003.geteDesc());
            }else{
                String[] str = strategyModel.getStgValue().split("-");
                boolean flag = DateUtil.isBelong(str[0], str[1]);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.S00004.geteCode(), ErrorCode.ENUM_ERROR.S00004.geteDesc());
                }
            }
        }
    }


    /**
     * @Description: check订单是否在策略规定的范围内
     * @param orderMoneyRange - 策略规定的订单金额范围
     * @param orderMoney - 订单金额
     * @return java.lang.String
     * @author yoko
     * @date 2020/6/6 11:48
     */
    public static void checkOrderMoney(String orderMoneyRange, String orderMoney) throws Exception{
        String [] rule = orderMoneyRange.split("-");
        double start = Double.parseDouble(rule[0]);
        double end = Double.parseDouble(rule[1]);
        double money = Double.parseDouble(orderMoney);
        if (money < start || money > end){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00005.geteCode(), ErrorCode.ENUM_ERROR.S00005.geteDesc());
        }
    }


    /**
     * @Description: 组装查询卡商的查询方法
     * @param id - 主键ID
     * @param orderMoney - 订单金额
     * @param useStatus - 使用状态
     * @return com.hz.fruit.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/9/9 17:16
     */
    public static MerchantModel assembleMerchantQuery(long id, String orderMoney, int useStatus){
        MerchantModel resBean = new MerchantModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(orderMoney)){
            BigDecimal bd = new BigDecimal(orderMoney);
            resBean.setMoney(bd);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询商户的查询方法
     * @param id - 主键ID
     * @param secretKey - 秘钥
     * @param useStatus - 使用状态
     * @return com.hz.fruit.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/9/9 17:16
     */
    public static ChannelModel assembleChannelQuery(long id, String secretKey, int useStatus){
        ChannelModel resBean = new ChannelModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(secretKey)){
            resBean.setSecretKey(secretKey);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: check商户数据是否为空
     * @param channelModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkChannelIsNull(ChannelModel channelModel) throws Exception{
        if (channelModel == null || channelModel.getId() == null || channelModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00009.geteCode(), ErrorCode.ENUM_ERROR.OR00009.geteDesc());
        }
    }

    /**
     * @Description: check卡商数据是否为空
     * @param merchantList
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkMerchantIsNull(List<MerchantModel> merchantList) throws Exception{
        if (merchantList == null || merchantList.size() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00010.geteCode(), ErrorCode.ENUM_ERROR.OR00010.geteDesc());
        }
    }

    /**
     * @Description: 组装查询手机号的查询方法
     * @param id - 主键ID
     * @param phoneNum - 手机号
     * @param isArrears - 是否欠费：1未欠费，2欠费
     * @param heartbeatStatus - 心跳状态：1初始化异常，2正常
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return com.hz.fruit.master.core.model.mobilecard.MobileCardModel
     * @author yoko
     * @date 2020/9/12 14:53
     */
    public static MobileCardModel assembleMobileCardQuery(long id, String phoneNum, int isArrears, int heartbeatStatus, int useStatus){
        MobileCardModel resBean = new MobileCardModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(phoneNum)){
            resBean.setPhoneNum(phoneNum);
        }
        if (isArrears > 0){
            resBean.setIsArrears(isArrears);
        }
        if (heartbeatStatus > 0){
            resBean.setHeartbeatStatus(heartbeatStatus);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: check校验手机卡数据是否为空
     * @param mobileCardList
     * @return
     * @author yoko
     * @date 2020/9/12 15:00
    */
    public static void checkmobileCardIsNull(List<MobileCardModel> mobileCardList) throws Exception{
        if (mobileCardList == null || mobileCardList.size() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00011.geteCode(), ErrorCode.ENUM_ERROR.OR00011.geteDesc());
        }
    }


    /**
     * @Description: 组装查询商户与银行卡绑定关系的查询方法
     * @param id - 主键ID
     * @param channelId - 商户ID
     * @param bankId - 银行卡ID
     * @param useStatus - 使用状态
     * @return com.hz.fruit.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/9/9 17:16
     */
    public static ChannelBankModel assembleChannelBankQuery(long id, long channelId, long bankId, int useStatus){
        ChannelBankModel resBean = new ChannelBankModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: 组装银行卡以及放量策略的查询条件
     * @param orderMoney
     * @return
     * @author yoko
     * @date 2020/9/12 20:15
    */
    public static BankModel assembleBankByOrderQuery(String orderMoney){
        BankModel resBean = new BankModel();
        if (!StringUtils.isBlank(orderMoney)){
            BigDecimal bd = new BigDecimal(orderMoney);
            resBean.setMoney(bd);
        }
        return resBean;
    }

    /**
     * @Description: check校验银行卡以及银行卡的放量策略数据是否为空
     * @param bankList
     * @return
     * @author yoko
     * @date 2020/9/12 20:19
    */
    public static void checkBankIsNull(List<BankModel> bankList) throws Exception{
        if (bankList == null || bankList.size() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00012.geteCode(), ErrorCode.ENUM_ERROR.OR00012.geteDesc());
        }
    }

    /**
     * @Description: 根据银行卡优先级排序银行卡集合
     * <p>
     *     商户与银行卡绑定关系的优先级排在集合的前面；
     *     商户未与银行卡绑定关系的优先级排在集合的后面
     * </p>
     * @param bankList
     * @param bankIdList
     * @return java.util.List<com.hz.fruit.master.core.model.bank.BankModel>
     * @author yoko
     * @date 2020/9/12 20:26
     */
    public static List<BankModel> assembleBankByPriority(List<BankModel> bankList, List<Long> bankIdList){
        List<BankModel> resList = new ArrayList<>();
        if (bankIdList == null || bankIdList.size() <= 0){
            resList = bankList;
        }else {
            List<BankModel> yesList = new ArrayList<>();
            List<BankModel> noList = new ArrayList<>();
            for (BankModel bankModel : bankList){
                int num = 0;
                for (long bankId : bankIdList){
                    if (bankModel.getId() == bankId){
                        num = 1;
                        break;
                    }
                }
                if (num != 0){
                    yesList.add(bankModel);
                }else {
                    noList.add(bankModel);
                }
            }

            if (yesList != null && yesList.size() > 0){
                resList.addAll(yesList);
            }
            if (noList != null && noList.size() > 0){
                resList.addAll(noList);
            }

        }
        return resList;
    }





    public static void main(String [] args) throws Exception{
        String sb1 = "2020-08-31 10:21:39";
//        DateUtil.DateUtil.calLastedTime(orderModel.getInvalidTime());
        int sb2 = DateUtil.calLastedTime(sb1);
        System.out.println("sb2:" + sb2);
    }




    

}
