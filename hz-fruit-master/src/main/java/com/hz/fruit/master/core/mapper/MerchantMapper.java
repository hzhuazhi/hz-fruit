package com.hz.fruit.master.core.mapper;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.model.merchant.MerchantModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 卡商的Dao层
 * @Author yoko
 * @Date 2020/9/8 17:05
 * @Version 1.0
 */
@Mapper
public interface MerchantMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新卡商的使用状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/5 19:21
     */
    public int updateUseStatus(MerchantModel model);
}
