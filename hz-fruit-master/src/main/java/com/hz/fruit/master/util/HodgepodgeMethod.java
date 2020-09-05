package com.hz.fruit.master.util;

import com.hz.fruit.master.core.common.utils.BeanUtils;
import com.hz.fruit.master.core.common.utils.DateUtil;
import com.hz.fruit.master.core.model.order.OrderModel;
import com.hz.fruit.master.core.model.region.RegionModel;
import com.hz.fruit.master.core.protocol.request.order.RequestOrder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


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

    public static OrderModel assembleOrderAdd(RequestOrder requestData, String orderNo){
        OrderModel resBean = BeanUtils.copy(requestData, OrderModel.class);
        resBean.setOrderNo(orderNo);
        resBean.setOrderMoney(requestData.money);
        String invalidTime = DateUtil.addDateMinute(5);
        resBean.setInvalidTime(invalidTime);
        if (StringUtils.isBlank(requestData.bankCard)){
//            resBean.setBankName("工商银行");
//            resBean.setBankCard("6212260302041783788");
//            resBean.setAccountName("程洪桂");
//            resBean.setBankCode("ICBC");
//            resBean.setBankName("北京银行");
//            resBean.setBankCard("6214680101631610");
//            resBean.setAccountName("毛文龙");
//            resBean.setBankCode("BOB");
            resBean.setBankName("光大银行");
            resBean.setBankCard("6226622108434149");
            resBean.setAccountName("毛文龙");
            resBean.setBankCode("CEB");

        }
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        return resBean;
    }

    public static OrderModel assembleOrderQuery(String orderNo){
        OrderModel resBean = new OrderModel();
        resBean.setOrderNo(orderNo);
        return resBean;
    }

    public static OrderModel assembleOrderByTaskQuery(int orderStatus, String invalidTime){
        OrderModel resBean = new OrderModel();
        resBean.setOrderStatus(orderStatus);
        resBean.setInvalidTime(invalidTime);
        return resBean;
    }

    public static OrderModel assembleOrderUpdate(long id, int orderStatus){
        OrderModel resBean = new OrderModel();
        resBean.setId(id);
        resBean.setOrderStatus(orderStatus);
        return resBean;
    }




    public static void main(String [] args) throws Exception{
        String sb1 = "2020-08-31 10:21:39";
//        DateUtil.DateUtil.calLastedTime(orderModel.getInvalidTime());
        int sb2 = DateUtil.calLastedTime(sb1);
        System.out.println("sb2:" + sb2);
    }




    

}
