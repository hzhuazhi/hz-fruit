package com.hz.fruit.master.util;

import com.hz.fruit.master.core.common.utils.constant.ServerConstant;
import com.hz.fruit.master.core.model.task.base.StatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Description 定时任务的公共类
 * @Author yoko
 * @Date 2020/1/11 16:20
 * @Version 1.0
 */
public class TaskMethod {
    private static Logger log = LoggerFactory.getLogger(TaskMethod.class);



    /**
     * @Description: 组装查询定时任务手机短信的查询条件
     * @param limitNum - 多少条数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskByMobileCardDataQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setLimitNum(limitNum);
        return resBean;
    }

    /**
     * @Description: 组装更改运行状态的数据-手机卡短信类容
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleUpdateStatusByMobileCardDataModel(long id, int runStatus, int dataType){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        resBean.setRunStatus(runStatus);
        if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 表示失败：失败则需要运行次数加一
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        }
        resBean.setDataType(dataType);
        return resBean;
    }

}
