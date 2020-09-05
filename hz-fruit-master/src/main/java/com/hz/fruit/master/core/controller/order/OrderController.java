package com.hz.fruit.master.core.controller.order;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.exception.ExceptionMethod;
import com.hz.fruit.master.core.common.exception.ServiceException;
import com.hz.fruit.master.core.common.utils.DateUtil;
import com.hz.fruit.master.core.common.utils.constant.ServerConstant;
import com.hz.fruit.master.core.model.order.OrderModel;
import com.hz.fruit.master.core.protocol.request.order.RequestOrder;
import com.hz.fruit.master.util.ComponentUtil;
import com.hz.fruit.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.Map;

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
     * @Description: 首页-开始浏览广告
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/order/getQrCode
     * test:http://localhost:8089/fruit/order/getQrCode?money=5.00&bankName=bankName1&bankCard=bankCard1&accountName=accountName1&bankCode=bankCode1&returnUrl=http://www.qidian.com&noredirect=
     * 请求的属性类:RequestAppeal
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 客户端加密字段:token+ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     *
     * {
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiN2UyZDhlODA4NWRhNzE5YzAzNWU2NTNiNGZmZmUzYjYiLCJzdGltZSI6MTU4MzMwNTc0NTQzOSwid2giOnsiYWRBZHMiOiJodHRwOi8vd3d3LnFpZGlhbi5jb20iLCJwcm9maXQiOiIwLjMyIn19"
     *     },
     *     "sgid": "202003041509030000001",
     *     "cgid": ""
     * }
     *
     * 九通：
     * {"channel":"channel_2","trade_type":"2032","total_amount":"10","out_trade_no":"out_trade_no_1","notify_url":"http://www.baidu.com/sb","interface_ver":"V5.0","extra_return_param":"extra_return_param_1","client_ip":"192.168.0.1","sign":"c414e730be7d93bec15408b83dd69281","sub_time":"2020-03-24 17:52:13","product_name":"product_name_1","product_code":"product_code_1","return_url":"http://www.baidu.com/return_url"}
     */
    @RequestMapping(value = "/getQrCode", method = {RequestMethod.GET})
    public String getQrCode(HttpServletRequest request, HttpServletResponse response, RequestOrder requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";

        String data = "";
        RequestOrder requestModel = new RequestOrder();
        try{
            if (requestData == null){
                throw new ServiceException("0001", "请填写参数值!");
            }else {
                if (StringUtils.isBlank(requestData.money)){
                    throw new ServiceException("0002", "请填写订单金额!");
                }
            }

            OrderModel orderAdd = HodgepodgeMethod.assembleOrderAdd(requestData, sgid);
            int num = ComponentUtil.orderService.add(orderAdd);
            if (num <= 0){
                throw new ServiceException("0000", "错误,请重试!");
            }


            String resData = "";
            String qrCodeUrl = ComponentUtil.loadConstant.qrCodeUrl;
            if (!StringUtils.isBlank(requestData.returnUrl)){
                resData = qrCodeUrl + "?orderNo=" + sgid + "&returnUrl=" + requestData.returnUrl;
            }else {
                resData = qrCodeUrl + "?orderNo=" + sgid + "&returnUrl=";
            }

            if (!StringUtils.isBlank(requestData.noredirect)){
            }else {
                response.sendRedirect(resData);
                return null;
            }
            // 返回数据给客户端
            return resData;
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this OrderController.getQrCode() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }




    /**
     * @Description: 首页-开始浏览广告
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/order/getOrder
     * test:http://localhost:8089/fruit/order/getOrder?orderNo=1
     * 请求的属性类:RequestAppeal
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 客户端加密字段:token+ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     *
     * {
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiN2UyZDhlODA4NWRhNzE5YzAzNWU2NTNiNGZmZmUzYjYiLCJzdGltZSI6MTU4MzMwNTc0NTQzOSwid2giOnsiYWRBZHMiOiJodHRwOi8vd3d3LnFpZGlhbi5jb20iLCJwcm9maXQiOiIwLjMyIn19"
     *     },
     *     "sgid": "202003041509030000001",
     *     "cgid": ""
     * }
     *
     * 九通：
     * {"channel":"channel_2","trade_type":"2032","total_amount":"10","out_trade_no":"out_trade_no_1","notify_url":"http://www.baidu.com/sb","interface_ver":"V5.0","extra_return_param":"extra_return_param_1","client_ip":"192.168.0.1","sign":"c414e730be7d93bec15408b83dd69281","sub_time":"2020-03-24 17:52:13","product_name":"product_name_1","product_code":"product_code_1","return_url":"http://www.baidu.com/return_url"}
     */
    @RequestMapping(value = "/getOrder", method = {RequestMethod.GET})
    public String getOrder(HttpServletRequest request, HttpServletResponse response, RequestOrder requestData) throws Exception{


        String data = "";
        RequestOrder requestModel = new RequestOrder();
        try{
            if (requestData == null){
                throw new ServiceException("0001", "请填写参数值!");
            }else {
                if (StringUtils.isBlank(requestData.orderNo)){
                    throw new ServiceException("0002", "请填写订单号!");
                }
            }

            OrderModel orderQuery = HodgepodgeMethod.assembleOrderQuery(requestData.orderNo);
            OrderModel orderModel = (OrderModel)ComponentUtil.orderService.findByObject(orderQuery);
            if (orderModel != null && orderModel.getId() != null && orderModel.getId() > 0){
                int isInvalid = DateUtil.compareDate(orderModel.getInvalidTime(), new Date());
                if (isInvalid != 1){
                    isInvalid = 2;
                }
                orderModel.setIsInvalid(isInvalid);
                int invalidSecond = DateUtil.calLastedTime(orderModel.getInvalidTime());
                if (invalidSecond <= 0){
                    invalidSecond = 0;
                }
                orderModel.setInvalidSecond(invalidSecond);
            }

            String resData = JSON.toJSONString(orderModel);

            // 返回数据给客户端
            return resData;
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this OrderController.getOrder() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }

}
