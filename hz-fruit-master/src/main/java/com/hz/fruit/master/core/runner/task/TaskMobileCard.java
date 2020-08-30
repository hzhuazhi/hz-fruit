package com.hz.fruit.master.core.runner.task;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.utils.constant.ServerConstant;
import com.hz.fruit.master.core.model.mobilecard.MobileCardDataModel;
import com.hz.fruit.master.core.model.order.OrderModel;
import com.hz.fruit.master.core.model.task.base.StatusModel;
import com.hz.fruit.master.util.ComponentUtil;
import com.hz.fruit.master.util.HodgepodgeMethod;
import com.hz.fruit.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 手机卡的短信信息处理的task
 * @Author yoko
 * @Date 2020/6/2 17:39
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskMobileCard {
    private final static Logger log = LoggerFactory.getLogger(TaskMobileCard.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;

    /**
     * @Description: 解析所有手机短信信息
     * <p>
     *     每3每秒运行一次
     *     1.拆解短信的类型：1广告短信，2挂失短信，3欠费短信，4普通短信（普通短信里面存在银行卡收款信息）
     *     2.短信类型为挂失短信，则需要对银行卡状态进行停用。
     *     3.当手机属于欠费短信：对手机卡的状态进行停用。
     *     4.当普通短信，则需要把数据添加到：银行卡收款回调数据表中。
     *     5.当广告短信则不作处理。
     *     6.手机号变更，则需要对手机号状态更改
     * </p>
     * helloworld
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 2000) // 每3秒执行
    public void mobileCardData() throws Exception{
//        log.info("----------------------------------TaskMobileCard.mobileCardData()----start");
        // 获取手机短信数据
        StatusModel statusQuery = TaskMethod.assembleTaskByMobileCardDataQuery(limitNum);
        List<MobileCardDataModel> synchroList = ComponentUtil.taskMobileCardService.getMobileCardDataList(statusQuery);
        for (MobileCardDataModel data : synchroList){
            try{

                if (!StringUtils.isBlank(data.getSmsContent())){
                    if (data.getSmsContent().indexOf("尾号") > -1){
                        OrderModel orderQuery = HodgepodgeMethod.assembleOrderByTaskQuery(1, "1");
                        OrderModel orderModel = (OrderModel)ComponentUtil.orderService.findByObject(orderQuery);
                        if (orderModel != null && orderModel.getId() != null && orderModel.getId() > 0){
                            OrderModel orderUpdate = HodgepodgeMethod.assembleOrderUpdate(orderModel.getId(), 4);
                            ComponentUtil.orderService.update(orderUpdate);
                        }
                    }
                }

                // 组装更改运行状态的数据：更新成成功
                StatusModel statusModel = TaskMethod.assembleUpdateStatusByMobileCardDataModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE, 6);
                ComponentUtil.taskMobileCardService.updateMobileCardDataStatus(statusModel);


//                log.info("----------------------------------TaskMobileCard.mobileCardData()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMobileCard.mobileCardData() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleUpdateStatusByMobileCardDataModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO, 1);
                ComponentUtil.taskMobileCardService.updateMobileCardDataStatus(statusModel);
            }
        }

    }


    public static void main(String [] args){
        String str = "赢大奖，赚大钱";
        String keyStr = "赚,赢";
        String[] keyStrArr = keyStr.split(",");
        int num = 0;
        int count = 0;
        for (String key : keyStrArr){

            if (str.indexOf(key) > -1){
                System.out.println("符合");
                count ++;
            }
        }

    }

}
