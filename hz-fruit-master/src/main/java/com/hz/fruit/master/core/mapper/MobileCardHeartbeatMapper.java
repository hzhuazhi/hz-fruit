package com.hz.fruit.master.core.mapper;

import com.hz.fruit.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 手机卡的心跳纪录的Dao层
 * @Author yoko
 * @Date 2020/9/20 14:39
 * @Version 1.0
 */
@Mapper
public interface MobileCardHeartbeatMapper<T> extends BaseDao<T> {
}
