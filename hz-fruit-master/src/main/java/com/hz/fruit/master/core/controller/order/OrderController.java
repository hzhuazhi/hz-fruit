package com.hz.fruit.master.core.controller.order;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.exception.ExceptionMethod;
import com.hz.fruit.master.core.common.utils.JsonResult;
import com.hz.fruit.master.core.common.utils.SignUtil;
import com.hz.fruit.master.core.common.utils.StringUtil;
import com.hz.fruit.master.core.common.utils.constant.ServerConstant;
import com.hz.fruit.master.core.model.RequestEncryptionJson;
import com.hz.fruit.master.core.model.ResponseEncryptionJson;
import com.hz.fruit.master.core.model.channel.ChannelBankModel;
import com.hz.fruit.master.core.model.channel.ChannelModel;
import com.hz.fruit.master.core.model.merchant.MerchantModel;
import com.hz.fruit.master.core.model.region.RegionModel;
import com.hz.fruit.master.core.model.strategy.StrategyModel;
import com.hz.fruit.master.core.protocol.request.order.ProtocolOrder;
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
     * 1.从策略中获取目前是否属于开放出码时间
     * 2.用户余额是否满足此次派单的金额。
     * 3.用户抢单开始的状态是在上线状态。
     * 3.用户的支付宝收款账号是否正常。
     * 4.已经派单过的用户并且名下有订单没有过失效期的用户不派单。
     * 5.派单进行中：需要从余额里面剔除相对应的订单金额，把剔除到的存放到表《用户扣减余额流水表》里面。
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/order/qrCode
     * 请求的属性类:RequestOrder
     * 必填字段:{"money":"11111","payType":1,"outTradeNo":"outTradeNo1","notifyUrl":"notify_url","returnUrl":"http://www.baidu.com","secretKey":""}
     * 加密字段:{"jsonData":"eyJtb25leSI6IjExMTEiLCJwYXlUeXBlIjoyLCJvdXRUcmFkZU5vIjoib3V0VHJhZGVObzEiLCJub3RpZnlVcmwiOiJub3RpZnlfdXJsIiwicmV0dXJuVXJsIjoiaHR0cDovL3d3dy5iYWlkdS5jb20iLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJvcmRlciI6eyJpbnZhbGlkU2Vjb25kIjoiMjk5IiwiaW52YWxpZFRpbWUiOiIyMDIwLTA3LTIxIDE0OjE1OjU5Iiwib3JkZXJNb25leSI6IjEwMC4wMCIsIm9yZGVyTm8iOiIyMDIwMDcyMTE0MTA1ODAwMDAwMDEiLCJxckNvZGUiOiIiLCJxckNvZGVVcmwiOiJodHRwJTNBJTJGJTJGd3d3LnliemZtLmNvbSUzQTgwMDIlMkZ3eCUyRmluZGV4Lmh0bWwlM0ZvcmRlck5vJTNEMjAyMDA3MjExNDEwNTgwMDAwMDAxJTI2cmV0dXJuVXJsJTNEaHR0cCUzQSUyRiUyRnd3dy5iYWlkdS5jb20ifSwic2lnbiI6IiIsInN0aW1lIjoxNTk1MzExODU5NTcxfQ=="
     *     },
     *     "sgid": "202007211410580000001",
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

            // 根据秘钥获取商户信息
            ChannelModel channelQuery = HodgepodgeMethod.assembleChannelQuery(0, requestModel.secretKey, 1);
            ChannelModel channelModel = ComponentUtil.channelService.getChannel(channelQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkChannelIsNull(channelModel);

            // 获取卡商集合：卡商的余额必须大于订单金额
            MerchantModel merchantQuery = HodgepodgeMethod.assembleMerchantQuery(0, requestModel.money, 1);
            List<MerchantModel> merchantList = ComponentUtil.merchantService.findByCondition(merchantQuery);
            HodgepodgeMethod.checkMerchantIsNull(merchantList);

            // 获取卡商的主键ID集合
            List<Long> merchantIdList = merchantList.stream().map(MerchantModel::getId).collect(Collectors.toList());

            // 获取商户与银行卡绑定关系的集合
            ChannelBankModel channelBankQuery = HodgepodgeMethod.assembleChannelBankQuery(0, channelModel.getId(),0, 1);
            List<ChannelBankModel> channelBankList = ComponentUtil.channelBankService.findByCondition(channelBankQuery);

            // 获取银行卡以及银行卡的放量策略数据
//            MerchantBankModel merchantBankQuery = HodgepodgeMethod.assembleMerchantBankByOrderQuery(merchantIdList, 3, 1);
//            List<MerchantBankModel> merchantBankList = ComponentUtil.merchantBankService.getMerchantBankAndStrategy(merchantBankQuery);
//            HodgepodgeMethod.checkMerchantBankIsNull(merchantBankList);













            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
//            String strData = HodgepodgeMethod.assembleOrderGroupQrCodeDataResult(stime, token, orderModel, requestModel.returnUrl, ComponentUtil.loadConstant.wxGroupQrCodeUrl);
            // 数据加密
//            String encryptionData = StringUtil.mergeCodeBase64(strData);
            String encryptionData = StringUtil.mergeCodeBase64("sjsjasa");
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

}
