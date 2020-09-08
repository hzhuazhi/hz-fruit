package com.hz.fruit.master.core.mapper;

import com.hz.fruit.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 卡商收款回调数据的Dao层
 * @Author yoko
 * @Date 2020/9/8 18:01
 * @Version 1.0
 */
@Mapper
public interface MerchantDataMapper<T> extends BaseDao<T> {
}
