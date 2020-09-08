package com.hz.fruit.master.core.mapper;

import com.hz.fruit.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行收款信息的Dao层
 * @Author yoko
 * @Date 2020/5/21 17:00
 * @Version 1.0
 */
@Mapper
public interface MerchantBankCollectionMapper<T> extends BaseDao<T> {
}