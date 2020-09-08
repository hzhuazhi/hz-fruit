package com.hz.fruit.master.core.mapper;

import com.hz.fruit.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 我方银行的Dao层
 * @Author yoko
 * @Date 2020/9/8 19:46
 * @Version 1.0
 */
@Mapper
public interface MyBankMapper<T> extends BaseDao<T> {
}
