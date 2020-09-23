package com.hz.fruit.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.exception.ServiceException;
import com.hz.fruit.master.core.common.utils.BeanUtils;
import com.hz.fruit.master.core.common.utils.DateUtil;
import com.hz.fruit.master.core.common.utils.ShortChainUtil;
import com.hz.fruit.master.core.common.utils.StringUtil;
import com.hz.fruit.master.core.common.utils.constant.ErrorCode;
import com.hz.fruit.master.core.common.utils.constant.ServerConstant;
import com.hz.fruit.master.core.model.bank.BankModel;
import com.hz.fruit.master.core.model.channel.ChannelBankModel;
import com.hz.fruit.master.core.model.channel.ChannelModel;
import com.hz.fruit.master.core.model.issue.IssueModel;
import com.hz.fruit.master.core.model.merchant.MerchantModel;
import com.hz.fruit.master.core.model.mobilecard.MobileCardModel;
import com.hz.fruit.master.core.model.order.OrderModel;
import com.hz.fruit.master.core.model.region.RegionModel;
import com.hz.fruit.master.core.model.shortchain.ShortChainModel;
import com.hz.fruit.master.core.model.statistics.StatisticsClickPayModel;
import com.hz.fruit.master.core.model.strategy.StrategyModel;
import com.hz.fruit.master.core.protocol.request.issue.RequestIssue;
import com.hz.fruit.master.core.protocol.request.order.ProtocolOrder;
import com.hz.fruit.master.core.protocol.request.order.RequestOrder;
import com.hz.fruit.master.core.protocol.request.statistics.RequestStatisticsClickPay;
import com.hz.fruit.master.core.protocol.response.ResponseData;
import com.hz.fruit.master.core.protocol.response.issue.Issue;
import com.hz.fruit.master.core.protocol.response.issue.ResponseIssue;
import com.hz.fruit.master.core.protocol.response.order.Order;
import com.hz.fruit.master.core.protocol.response.order.ResponseOrder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.URLEncoder;
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

    /**
     * @Description: check筛选的银行卡是否为空
     * @param bankModel
     * @return
     * @author yoko
     * @date 2020/9/13 14:41
    */
    public static void checkScreenBankIsNull(BankModel bankModel) throws Exception{
        if (bankModel == null || bankModel.getId() == null || bankModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00013.geteCode(), ErrorCode.ENUM_ERROR.OR00013.geteDesc());
        }
    }


    /**
     * @Description: 组装添加订单的方法
     * @param bankModel - 筛选出的银行卡
     * @param requestModel - 请求数据
     * @param channelModel - 商户信息
     * @param orderNo - 订单号
     * @param invalidTimeNum - 策略中的失效时间
     * @return com.hz.fruit.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/9/13 14:41
     */
    public static OrderModel assembleOrderAdd(BankModel bankModel, ProtocolOrder requestModel, ChannelModel channelModel, String orderNo, int invalidTimeNum){
        OrderModel resBean = new OrderModel();
        resBean.setOrderNo(orderNo);
        resBean.setBankId(bankModel.getId());
        resBean.setOrderType(requestModel.payType);
        resBean.setOrderMoney(requestModel.money);
        resBean.setOutTradeNo(requestModel.outTradeNo);
        // 订单失效时间
        String invalidTime = DateUtil.addDateMinute(invalidTimeNum);
        resBean.setInvalidTime(invalidTime);
        resBean.setNotifyUrl(requestModel.notifyUrl);
        resBean.setBankName(bankModel.getBankName());
        resBean.setBankCard(bankModel.getBankCard());
        resBean.setAccountName(bankModel.getAccountName());
        resBean.setBankCode(bankModel.getBankCode());
        resBean.setMobileCardId(bankModel.getMobileCardId());
        resBean.setPhoneNum(bankModel.getPhoneNum());
        resBean.setAccountId(bankModel.getAccountId());
        if (!StringUtils.isBlank(bankModel.getAcName())){
            resBean.setMerchantName(bankModel.getAcName());
        }
        if (channelModel != null && channelModel.getId() != null && channelModel.getId() > 0){
            resBean.setChannelId(channelModel.getId());
            if (!StringUtils.isBlank(channelModel.getAlias())){
                resBean.setChannelName(channelModel.getAlias());
            }
        }

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }

    /**
     * @Description: check校验添加订单信息是否正常
     * @param num
     * @return
     * @author yoko
     * @date 2020/9/13 14:49
    */
    public static void checkAddOrderIsOk(int num) throws Exception{
        if (num <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00014.geteCode(), ErrorCode.ENUM_ERROR.OR00014.geteDesc());
        }
    }


    /**
     * @Description: 用户派单成功的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderModel - 用户派单的详情
     * @param returnUrl - 支付完成之后自动跳转的地址
     * @param payQrCodeUrl - 生成的HTML页面的地址
     * @param shortChain - 短链
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderDataResult(long stime, String sign, OrderModel orderModel, String returnUrl, String payQrCodeUrl, String shortChain) throws Exception{
        ResponseOrder dataModel = new ResponseOrder();
        if (orderModel != null){
            Order order = new Order();
            order.orderNo = orderModel.getOrderNo();
            if (!StringUtils.isBlank(orderModel.getQrCode())){
                order.qrCode = orderModel.getQrCode();
            }
            order.orderMoney = orderModel.getOrderMoney();
            order.orderStatus = 1;
            order.invalidTime = orderModel.getInvalidTime();
            int invalidSecond = DateUtil.calLastedTime(orderModel.getInvalidTime());
            order.invalidSecond = String.valueOf(invalidSecond);
            order.bankName = orderModel.getBankName();
            order.bankCard = orderModel.getBankCard();
            order.accountName = orderModel.getAccountName();
            order.bankCode = orderModel.getBankCode();

            String resQrCodeUrl = "";
            if (!StringUtils.isBlank(returnUrl)){
                resQrCodeUrl = payQrCodeUrl + "?" + "orderNo=" +  orderModel.getOrderNo() + "&" + "returnUrl=" + returnUrl;
            }else {
                resQrCodeUrl = payQrCodeUrl + "?" + "orderNo=" +  orderModel.getOrderNo() + "&" + "returnUrl=";
            }
            order.qrCodeUrl = URLEncoder.encode(resQrCodeUrl,"UTF-8");
            if (!StringUtils.isBlank(shortChain)){
                order.shortChain = shortChain;
            }
            dataModel.order = order;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取派单数据-详情-返回码的接口时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkOrderByQrCodeData(RequestOrder requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00015.geteCode(), ErrorCode.ENUM_ERROR.OR00015.geteDesc());
        }

        if (StringUtils.isBlank(requestModel.orderNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00016.geteCode(), ErrorCode.ENUM_ERROR.OR00016.geteDesc());
        }
    }

    /**
     * @Description: check短链数据是否为空
     * @param shortChainModel
     * @return
     * @author yoko
     * @date 2020/9/13 15:48
    */
    public static void checkShortChainIsNull(ShortChainModel shortChainModel) throws Exception{
        if (shortChainModel == null || shortChainModel.getId() == null || shortChainModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00017.geteCode(), ErrorCode.ENUM_ERROR.OR00017.geteDesc());
        }
    }

    /**
     * @Description: 组装根据派单的订单号查询派单信息
     * @param orderNo - 派单的订单号
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/18 11:41
     */
    public static OrderModel assembleOrderByOrderNoQuery(String orderNo, int orderStatus){
        OrderModel resBean = new OrderModel();
        resBean.setOrderNo(orderNo);
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        return resBean;
    }



    /**
     * @Description: 根据订单号查询的订单数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderModel - 用户派单的详情
     * @param shortChain - 短链
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderByOrderNoDataResult(long stime, String sign, OrderModel orderModel, String shortChain) throws Exception{
        ResponseOrder dataModel = new ResponseOrder();
        if (orderModel != null){
            Order order = new Order();
            order.orderNo = orderModel.getOrderNo();
            if (!StringUtils.isBlank(orderModel.getQrCode())){
                order.qrCode = orderModel.getQrCode();
            }
            order.orderMoney = orderModel.getOrderMoney();
            order.invalidTime = orderModel.getInvalidTime();
            int invalidSecond = DateUtil.calLastedTime(orderModel.getInvalidTime());
            order.invalidSecond = String.valueOf(invalidSecond);
            order.bankName = orderModel.getBankName();
            order.bankCard = orderModel.getBankCard();
            order.accountName = orderModel.getAccountName();
            order.bankCode = orderModel.getBankCode();
            if (!StringUtils.isBlank(shortChain)){
                order.shortChain = shortChain;
            }
            dataModel.order = order;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 调用短链的公共方法，生成短链
     * @param orderModel - 订单信息
     * @param interfaceAds - 短链的API
     * @param shortChainMoney  -  短链生成是否带金额的参数：1不带金额参数，2带金额参数。
     * @return
     * @author yoko
     * @date 2020/9/13 16:18
    */
    public static String getShortChain(OrderModel orderModel, String interfaceAds, int shortChainMoney){
        String str = null;
        if (orderModel != null && orderModel.getId() != null && orderModel.getId() > 0){
            if (orderModel.getOrderType() == 2){
                // 支付宝转卡
                BankModel bankModel = BeanUtils.copy(orderModel, BankModel.class);
                if (bankModel != null && !StringUtils.isBlank(bankModel.getBankCard())){
                    // 生成短链
                    str = ShortChainUtil.getShortChainUrl(bankModel, interfaceAds, shortChainMoney, orderModel.getOrderMoney());
                }
            }
        }
        return str;
    }


    /**
     * @Description: 查询派单成功的订单状态数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderStatus - 不等于0表示成功
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderStatusResult(long stime, String sign, int orderStatus){
        ResponseOrder resBean = new ResponseOrder();
        Order dataModel = new Order();
        dataModel.orderStatus = orderStatus;
        resBean.order = dataModel;
        resBean.setStime(stime);
        resBean.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: 判断时间是否是在当前时间范围内
     * @param openTimeSlot
     * @return
     * @author yoko
     * @date 2020/9/23 14:15
    */
    public static boolean checkOpenTimeSlot(String openTimeSlot){
        boolean flag = false;
        String[] strArr = openTimeSlot.split("#");
        for (String str : strArr){
            String[] str_ = str.split("-");
            boolean flag_ = DateUtil.isBelong(str_[0], str_[1]);
            if (flag_){
                return true;
            }
        }
        return flag;
    }

    /**
     * @Description: check校验数据下发数据-集合
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkIssueGetDataList(RequestIssue requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00001.geteCode(), ErrorCode.ENUM_ERROR.I00001.geteDesc());
        }
    }


    /**
     * @Description: 下发集合的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param issueList - 下发集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleIssueListResult(long stime, String sign, List<IssueModel> issueList, Integer rowCount){
        ResponseIssue dataModel = new ResponseIssue();
        if (issueList != null && issueList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<Issue> dataList = BeanUtils.copyList(issueList, Issue.class);
            dataModel.dataList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据下发数据-详情
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkIssueGetData(RequestIssue requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00002.geteCode(), ErrorCode.ENUM_ERROR.I00002.geteDesc());
        }
    }


    /**
     * @Description: 下发集合的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param issueModel - 下发详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleIssueResult(long stime, String sign, IssueModel issueModel){
        ResponseIssue dataModel = new ResponseIssue();
        if (issueModel != null){
            Issue data = BeanUtils.copy(issueModel, Issue.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据下发数据-新增
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkIssueAdd(RequestIssue requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00003.geteCode(), ErrorCode.ENUM_ERROR.I00003.geteDesc());
        }

        // 校验支付平台的订单号
        if (StringUtils.isBlank(requestModel.outTradeNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00003.geteCode(), ErrorCode.ENUM_ERROR.I00003.geteDesc());
        }

        // 校验支付平台的订单金额
        if (StringUtils.isBlank(requestModel.orderMoney)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00004.geteCode(), ErrorCode.ENUM_ERROR.I00004.geteDesc());
        }else {
            // 金额是否有效
            if (requestModel.orderMoney.indexOf(".") > -1){
                boolean flag = StringUtil.isNumberByMoney(requestModel.orderMoney);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.I00005.geteCode(), ErrorCode.ENUM_ERROR.I00005.geteDesc());
                }
            }else {
                boolean flag = StringUtil.isNumer(requestModel.orderMoney);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.I00006.geteCode(), ErrorCode.ENUM_ERROR.I00006.geteDesc());
                }
            }
        }

        // 校验银行名称
        if (StringUtils.isBlank(requestModel.bankName)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00007.geteCode(), ErrorCode.ENUM_ERROR.I00007.geteDesc());
        }

        // 校验银行卡卡号
        if (StringUtils.isBlank(requestModel.bankCard)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00008.geteCode(), ErrorCode.ENUM_ERROR.I00008.geteDesc());
        }

        // 校验银行卡开户人
        if (StringUtils.isBlank(requestModel.accountName)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00009.geteCode(), ErrorCode.ENUM_ERROR.I00009.geteDesc());
        }
    }


    /**
     * @Description: 组装查询下发的查询条件
     * @param id - 主键ID
     * @param orderNo - 订单号
     * @param outTradeNo - 支付平台订单号：下游上报的订单号
     * @param orderStatus - 订单状态：1初始化，2超时/失败/审核驳回，3成功
     * @param ascriptionType - 订单分配归属类型：1归属卡商，2归属平台
     * @param isDistribution - 是否已分配完毕归属：1初始化/未分配，2已分配
     * @param isComplete - 是否已归集完毕：1初始化/未归集完毕，2已归集完毕；此状态：是归属类型属于平台方，平台方需要向卡商发布充值订单，发布完毕，如果卡商都已经充值完毕到我方卡，则修改此状态，修改成归集完毕的状态
     * @param checkStatus - 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @param whereCheckStatus - SQL查询条件 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @return com.hz.fruit.master.core.model.issue.IssueModel
     * @author yoko
     * @date 2020/9/23 15:03
     */
    public static IssueModel assembleIssueQuery(long id, String orderNo, String outTradeNo, int orderStatus, int ascriptionType, int isDistribution, int isComplete, int checkStatus, int whereCheckStatus){
        IssueModel resBean = new IssueModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (!StringUtils.isBlank(outTradeNo)){
            resBean.setOutTradeNo(outTradeNo);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (ascriptionType > 0){
            resBean.setAscriptionType(ascriptionType);
        }
        if (isDistribution > 0){
            resBean.setIsDistribution(isDistribution);
        }
        if (isComplete > 0){
            resBean.setIsComplete(isComplete);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (whereCheckStatus > 0){
            resBean.setWhereCheckStatus(whereCheckStatus);
        }
        return resBean;
    }


    /**
     * @Description: check下发新增：数据是否有重复录入
     * @param issueModel
     * @return
     * @author yoko
     * @date 2020/9/23 15:10
    */
    public static void checkIssueIsNotNull(IssueModel issueModel) throws Exception{
        if (issueModel != null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00010.geteCode(), ErrorCode.ENUM_ERROR.I00010.geteDesc());
        }
    }

    /**
     * @Description: 组装生成要添加的下发数据
     * @param requestModel - 支付平台请求的数据
     * @param orderNo - 订单号
     * @return com.hz.fruit.master.core.model.issue.IssueModel
     * @author yoko
     * @date 2020/9/23 15:13
     */
    public static IssueModel assembleIssueAdd(RequestIssue requestModel, String orderNo){
        IssueModel resBean = BeanUtils.copy(requestModel, IssueModel.class);
        resBean.setOrderNo(orderNo);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: check校验数据下发数据-更新审核状态
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkIssueUpdateCheck(RequestIssue requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00011.geteCode(), ErrorCode.ENUM_ERROR.I00011.geteDesc());
        }

        // 校验支付平台的订单号
        if (StringUtils.isBlank(requestModel.outTradeNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00012.geteCode(), ErrorCode.ENUM_ERROR.I00012.geteDesc());
        }

        // 校验审核状态
        if (requestModel.checkStatus == null || requestModel.getCheckStatus() == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00013.geteCode(), ErrorCode.ENUM_ERROR.I00013.geteDesc());
        }


    }

    /**
     * @Description: check下发审核状态更新：数据是否存在
     * @param issueModel
     * @return
     * @author yoko
     * @date 2020/9/23 15:10
     */
    public static void checkIssueIsNull(IssueModel issueModel) throws Exception{
        if (issueModel == null || issueModel.getId() == null || issueModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00014.geteCode(), ErrorCode.ENUM_ERROR.I00014.geteDesc());
        }
    }

    /**
     * @Description: check下发数据中，此订单的状态是否是成功状态
     * <p>
     *     成功状态来由：由卡商在充值订单中更新了充值成功的状态
     * </p>
     * @param orderStatus
     * @return
     * @author yoko
     * @date 2020/9/23 15:46
    */
    public static void checkIssueOrderStatus(int orderStatus) throws Exception{
        if (orderStatus != 3){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00015.geteCode(), ErrorCode.ENUM_ERROR.I00015.geteDesc());
        }
    }

    /**
     * @Description: check下发数据中，此订单的状态是否是成功状态
     * <p>
     *     成功状态来由：由卡商在充值订单中更新了充值成功的状态
     * </p>
     * @param checkStatus
     * @return
     * @author yoko
     * @date 2020/9/23 15:46
     */
    public static void checkIssueCheckStatus(int checkStatus) throws Exception{
        if (checkStatus == 3){
            throw new ServiceException(ErrorCode.ENUM_ERROR.I00016.geteCode(), ErrorCode.ENUM_ERROR.I00016.geteDesc());
        }
    }


    /**
     * @Description: 组装查询下发的更新方法
     * @param id - 主键ID
     * @param orderNo - 订单号
     * @param outTradeNo - 支付平台订单号：下游上报的订单号
     * @param orderStatus - 订单状态：1初始化，2超时/失败/审核驳回，3成功
     * @param pictureAds - 转账成功图片凭证
     * @param myBankInfo - 我方银行卡信息备注：假如归属类型：我方/平台，填写我方银行卡的信息
     * @param ascriptionType - 订单分配归属类型：1归属卡商，2归属平台
     * @param isDistribution - 是否已分配完毕归属：1初始化/未分配，2已分配
     * @param isComplete - 是否已归集完毕：1初始化/未归集完毕，2已归集完毕；此状态：是归属类型属于平台方，平台方需要向卡商发布充值订单，发布完毕，如果卡商都已经充值完毕到我方卡，则修改此状态，修改成归集完毕的状态
     * @param checkStatus - 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @param checkInfo -  审核失败缘由，审核失败的原因
     * @param dataExplain - 数据说明：做解说用的
     * @param whereCheckStatus - SQL查询条件 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @return com.hz.fruit.master.core.model.issue.IssueModel
     * @author yoko
     * @date 2020/9/23 15:03
     */
    public static IssueModel assembleIssueUpdate(long id, String orderNo, String outTradeNo, int orderStatus, String pictureAds,
                                                 String myBankInfo, int ascriptionType,int isDistribution, int isComplete, int checkStatus, String checkInfo,
                                                 String dataExplain, int whereCheckStatus){
        IssueModel resBean = new IssueModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (!StringUtils.isBlank(outTradeNo)){
            resBean.setOutTradeNo(outTradeNo);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(pictureAds)){
            resBean.setPictureAds(pictureAds);
        }
        if (!StringUtils.isBlank(myBankInfo)){
            resBean.setMyBankInfo(myBankInfo);
        }
        if (ascriptionType > 0){
            resBean.setAscriptionType(ascriptionType);
        }
        if (isDistribution > 0){
            resBean.setIsDistribution(isDistribution);
        }
        if (isComplete > 0){
            resBean.setIsComplete(isComplete);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (!StringUtils.isBlank(checkInfo)){
            resBean.setCheckInfo(checkInfo);
        }
        if (!StringUtils.isBlank(dataExplain)){
            resBean.setDataExplain(dataExplain);
        }
        if (whereCheckStatus > 0){
            resBean.setWhereCheckStatus(whereCheckStatus);
        }
        return resBean;
    }






    public static void main(String [] args) throws Exception{
        String sb1 = "2020-08-31 10:21:39";
//        DateUtil.DateUtil.calLastedTime(orderModel.getInvalidTime());
        int sb2 = DateUtil.calLastedTime(sb1);
        System.out.println("sb2:" + sb2);

        String str = "00:01-19:12#12:01-19:00#20:01-23:59";
        boolean flag = checkOpenTimeSlot(str);
        System.out.println("flag:" + flag);


    }




    

}
