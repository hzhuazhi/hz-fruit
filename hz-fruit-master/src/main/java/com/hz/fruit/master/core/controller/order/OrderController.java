package com.hz.fruit.master.core.controller.order;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.exception.ExceptionMethod;
import com.hz.fruit.master.core.common.utils.JsonResult;
import com.hz.fruit.master.core.common.utils.SignUtil;
import com.hz.fruit.master.core.common.utils.StringUtil;
import com.hz.fruit.master.core.common.utils.constant.ServerConstant;
import com.hz.fruit.master.core.model.RequestEncryptionJson;
import com.hz.fruit.master.core.model.ResponseEncryptionJson;
import com.hz.fruit.master.core.model.bank.BankModel;
import com.hz.fruit.master.core.model.channel.ChannelBankModel;
import com.hz.fruit.master.core.model.channel.ChannelModel;
import com.hz.fruit.master.core.model.merchant.MerchantModel;
import com.hz.fruit.master.core.model.mobilecard.MobileCardModel;
import com.hz.fruit.master.core.model.order.OrderModel;
import com.hz.fruit.master.core.model.region.RegionModel;
import com.hz.fruit.master.core.model.shortchain.ShortChainModel;
import com.hz.fruit.master.core.model.strategy.StrategyModel;
import com.hz.fruit.master.core.protocol.request.order.ProtocolOrder;
import com.hz.fruit.master.core.protocol.request.order.RequestOrder;
import com.hz.fruit.master.util.ComponentUtil;
import com.hz.fruit.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 任务订单（平台派发订单）的Controller层
 * @Author yoko
 * @Date 2020/5/22 10:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/fruit/order")
public class OrderController {

    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    /**
     * 30分钟.
     */
    public long THIRTY_MIN = 30;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;


    /**
     * @Description: 派发订单-微信群接口
     * <p>
     *
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/order/qrCode
     * 请求的属性类:RequestOrder
     * 必填字段:{"money":"1111","payType":2,"outTradeNo":"outTradeNo1","notifyUrl":"notify_url","returnUrl":"http://www.baidu.com","secretKey":"秘钥_7"}
     * 加密字段:{"jsonData":"eyJtb25leSI6IjExMTEiLCJwYXlUeXBlIjoyLCJvdXRUcmFkZU5vIjoib3V0VHJhZGVObzEiLCJub3RpZnlVcmwiOiJub3RpZnlfdXJsIiwicmV0dXJuVXJsIjoiaHR0cDovL3d3dy5iYWlkdS5jb20iLCJzZWNyZXRLZXkiOiLnp5jpkqVfNyJ9"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJvcmRlciI6eyJhY2NvdW50TmFtZSI6IuW8gOaIt+S6ul83XzQiLCJiYW5rQ2FyZCI6IumTtuihjOWNoV83XzQiLCJiYW5rQ29kZSI6IueugOeggV83XzQiLCJiYW5rTmFtZSI6IumTtuihjOWQjeensF83XzQiLCJpbnZhbGlkU2Vjb25kIjoiODk2IiwiaW52YWxpZFRpbWUiOiIyMDIwLTA5LTEzIDE2OjUwOjU2Iiwib3JkZXJNb25leSI6IjExMTEuMDAiLCJvcmRlck5vIjoiMjAyMDA5MTMxNjM1NTUwMDAwMDAxIiwib3JkZXJTdGF0dXMiOjEsInFyQ29kZVVybCI6Imh0dHAlM0ElMkYlMkZ3d3cueWJ6Zm0uY29tJTNBODAwMiUyRnBheTEyMyUyRmg1cXJjb2RlLmh0bWwlM0ZvcmRlck5vJTNEMjAyMDA5MTMxNjM1NTUwMDAwMDAxJTI2cmV0dXJuVXJsJTNEaHR0cCUzQSUyRiUyRnd3dy5iYWlkdS5jb20ifSwic2lnbiI6IiIsInN0aW1lIjoxNTk5OTg2MTU2MzU2fQ=="
     *     },
     *     "sgid": "202009131635550000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/qrCode", method = {RequestMethod.POST})
    public JsonResult<Object> qrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        ProtocolOrder requestModel = new ProtocolOrder();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, ProtocolOrder.class);

            // check校验数据
            HodgepodgeMethod.checkOrderAdd(requestModel);

            if (requestModel.money.indexOf(".") <= -1){
                requestModel.money = requestModel.money + ".00";
            }

            // 策略数据：出码开关
            StrategyModel strategyQrCodeSwitchQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.QR_CODE_SWITCH.getStgType());
            StrategyModel strategyQrCodeSwitchModel = ComponentUtil.strategyService.getStrategyModel(strategyQrCodeSwitchQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByQrCodeSwitch(strategyQrCodeSwitchModel);

            // 策略数据：出码金额范围
            StrategyModel strategyOrderMoneyRangeQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ORDER_MONEY_RANGE.getStgType());
            StrategyModel strategyOrderMoneyRangeModel = ComponentUtil.strategyService.getStrategyModel(strategyOrderMoneyRangeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkOrderMoney(strategyOrderMoneyRangeModel.getStgValue(), requestModel.money);

            // 策略数据：出码后银行卡金额的锁定时间
            int orderMoneyLockTime = 0;
            StrategyModel strategyOrderMoneyLockTimeQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ORDER_MONEY_LOCK_TIME.getStgType());
            StrategyModel strategyOrderMoneyLockTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyOrderMoneyLockTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            orderMoneyLockTime = strategyOrderMoneyLockTimeModel.getStgNumValue();

            // 策略数据：出码后订单的支付时间
            int invalidTimeNum = 0;
            StrategyModel strategyInvalidTimeNumQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.INVALID_TIME_NUM.getStgType());
            StrategyModel strategyInvalidTimeNumModel = ComponentUtil.strategyService.getStrategyModel(strategyInvalidTimeNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            invalidTimeNum = strategyInvalidTimeNumModel.getStgNumValue();


            // 根据秘钥获取商户信息
            ChannelModel channelQuery = HodgepodgeMethod.assembleChannelQuery(0, requestModel.secretKey, 1);
            ChannelModel channelModel = ComponentUtil.channelService.getChannel(channelQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkChannelIsNull(channelModel);

//            // 获取卡商集合：卡商的余额必须大于订单金额
//            MerchantModel merchantQuery = HodgepodgeMethod.assembleMerchantQuery(0, requestModel.money, 1);
//            List<MerchantModel> merchantList = ComponentUtil.merchantService.findByCondition(merchantQuery);
//            HodgepodgeMethod.checkMerchantIsNull(merchantList);
//
//            // 获取卡商的主键ID集合
//            List<Long> merchantIdList = merchantList.stream().map(MerchantModel::getAccountId).collect(Collectors.toList());
//
//            // 获取目前正常使用的手机号
//            MobileCardModel mobileCardQuery = HodgepodgeMethod.assembleMobileCardQuery(0, null, 1, 2, 1);
//            List<MobileCardModel> mobileCardList = ComponentUtil.mobileCardService.findByCondition(mobileCardQuery);
//            HodgepodgeMethod.checkmobileCardIsNull(mobileCardList);
//
//            // 获取手机卡的主键ID集合
//            List<Long> mobileCardIdList = mobileCardList.stream().map(MobileCardModel::getId).collect(Collectors.toList());

            // 获取商户与银行卡绑定关系的集合
            ChannelBankModel channelBankQuery = HodgepodgeMethod.assembleChannelBankQuery(0, channelModel.getId(),0, 1);
            List<ChannelBankModel> channelBankList = ComponentUtil.channelBankService.findByCondition(channelBankQuery);

            // 获取绑定关系的银行卡ID
            List<Long> bankIdList = null;
            if (channelBankList != null && channelBankList.size() > 0){
                bankIdList = channelBankList.stream().map(ChannelBankModel::getBankId).collect(Collectors.toList());
            }

            // 获取银行卡以及银行卡的放量策略数据
            BankModel bankByOrderQuery = HodgepodgeMethod.assembleBankByOrderQuery(requestModel.money);
            List<BankModel> bankList = ComponentUtil.bankService.getBankAndStrategy(bankByOrderQuery);
            HodgepodgeMethod.checkBankIsNull(bankList);


            // 区分出与银行卡绑定关系以及未绑定关系
            List<BankModel> bankAllList = HodgepodgeMethod.assembleBankByPriority(bankList, bankIdList);
//            for (BankModel bankModel : bankAllList){
//                log.info("ID:" + bankModel.getId() + ",priority:" + bankModel.getPriority());
//            }

            // 筛选可用的银行卡
            BankModel bankModel = ComponentUtil.orderService.screenBank(bankAllList, requestModel.money, requestModel.payType, orderMoneyLockTime);
            HodgepodgeMethod.checkScreenBankIsNull(bankModel);

            // 添加订单
            OrderModel orderModel = HodgepodgeMethod.assembleOrderAdd(bankModel, requestModel, channelModel, sgid, invalidTimeNum);
            int num = ComponentUtil.orderService.add(orderModel);
            HodgepodgeMethod.checkAddOrderIsOk(num);



            String payQrCodeUrl = "";// 要跳转的支付页
            if (requestModel.payType == 2){
                payQrCodeUrl = ComponentUtil.loadConstant.zfbQrCodeUrl;
            }else if (requestModel.payType == 3){
                payQrCodeUrl = ComponentUtil.loadConstant.cardQrCodeUrl;
            }
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
//            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderDataResult(stime, token, orderModel, requestModel.returnUrl, payQrCodeUrl, null);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this OrderController.qrCode() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.qrCode() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取派单数据-详情-返回码的接口
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/order/getQrCode
     * 请求的属性类:RequestOrder
     * 必填字段:{"orderNo":"order_no_3"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoiMjAyMDA5MTMxNjM1NTUwMDAwMDAxIn0="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJvcmRlciI6eyJhY2NvdW50TmFtZSI6IuW8gOaIt+S6ul83XzQiLCJiYW5rQ2FyZCI6IumTtuihjOWNoV83XzQiLCJiYW5rQ29kZSI6IueugOeggV83XzQiLCJiYW5rTmFtZSI6IumTtuihjOWQjeensF83XzQiLCJpbnZhbGlkU2Vjb25kIjoiMjQyIiwiaW52YWxpZFRpbWUiOiIyMDIwLTA5LTEzIDE2OjUwOjU2Iiwib3JkZXJNb25leSI6IjExMTEuMDAiLCJvcmRlck5vIjoiMjAyMDA5MTMxNjM1NTUwMDAwMDAxIiwic2hvcnRDaGFpbiI6Imh0dHA6Ly93ejIuaW4vMk8xVmJnIn0sInNpZ24iOiI3MzgzMzcwMmQzZDUzZmVjNDdjYzAyOGIzMTg5ZjliZCIsInN0aW1lIjoxNTk5OTg2ODEzMjcwfQ=="
     *     }
     * }
     */
    @RequestMapping(value = "/getQrCode", method = {RequestMethod.POST})
    public JsonResult<Object> getQrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String data = "";

        RequestOrder requestModel = new RequestOrder();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestOrder.class);

            // check校验请求的数据
            HodgepodgeMethod.checkOrderByQrCodeData(requestModel);

            // 策略数据：短链金额配置
            int shortChainMoney = 0;
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.SHORT_CHAIN_MONEY.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            shortChainMoney = strategyModel.getStgNumValue();

            // 获取短链数据
            ShortChainModel shortChainModel = ComponentUtil.shortChainService.getShortChain(new ShortChainModel(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkShortChainIsNull(shortChainModel);


            // 收款账号详情数据
            OrderModel orderQuery = HodgepodgeMethod.assembleOrderByOrderNoQuery(requestModel.orderNo, 1);
            OrderModel orderData = (OrderModel)ComponentUtil.orderService.findByObject(orderQuery);

            // 生成短链
            String shortChain = HodgepodgeMethod.getShortChain(orderData, shortChainModel.getInterfaceAds(), shortChainMoney);

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderByOrderNoDataResult(stime, sign, orderData, shortChain);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this OrderController.getQrCode() is error , the data=%s!", data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.getQrCode() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }



    /**
     * @Description: 获取派单的订单状态
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/order/getOrderStatus
     * 请求的属性类:RequestOrder
     * 必填字段:{"orderNo":"202009131631330000001"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoiMjAyMDA5MTMxNjM1NTUwMDAwMDAxIn0="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJvcmRlclN0YXR1cyI6MX0="
     *     }
     * }
     */
    @RequestMapping(value = "/getOrderStatus", method = {RequestMethod.POST})
    public JsonResult<Object> getOrderStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String data = "";

        RequestOrder requestModel = new RequestOrder();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestOrder.class);

            // check校验请求的数据
            HodgepodgeMethod.checkOrderByQrCodeData(requestModel);

            // 收款账号详情数据
            OrderModel orderQuery = HodgepodgeMethod.assembleOrderByOrderNoQuery(requestModel.orderNo, 4);
            int orderStatus = ComponentUtil.orderService.getOrderStatus(orderQuery);
            if (orderStatus != 0){
                orderStatus = 1;
            }
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderStatusResult(stime, sign, orderStatus);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this OrderController.getOrderStatus() is error , the data=%s!", data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.getOrderStatus() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }

}
