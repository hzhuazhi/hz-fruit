package com.hz.fruit.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.exception.ServiceException;
import com.hz.fruit.master.core.common.utils.BeanUtils;
import com.hz.fruit.master.core.common.utils.DateUtil;
import com.hz.fruit.master.core.common.utils.constant.ErrorCode;
import com.hz.fruit.master.core.model.order.OrderModel;
import com.hz.fruit.master.core.model.region.RegionModel;
import com.hz.fruit.master.core.model.statistics.StatisticsClickPayModel;
import com.hz.fruit.master.core.protocol.request.bank.RequestBank;
import com.hz.fruit.master.core.protocol.request.order.RequestOrder;
import com.hz.fruit.master.core.protocol.request.statistics.RequestStatisticsClickPay;
import com.hz.fruit.master.core.protocol.response.ResponseData;
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



    public static void main(String [] args) throws Exception{
        String sb1 = "2020-08-31 10:21:39";
//        DateUtil.DateUtil.calLastedTime(orderModel.getInvalidTime());
        int sb2 = DateUtil.calLastedTime(sb1);
        System.out.println("sb2:" + sb2);
    }




    

}
