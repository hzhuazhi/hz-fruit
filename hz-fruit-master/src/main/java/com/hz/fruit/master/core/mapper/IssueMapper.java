package com.hz.fruit.master.core.mapper;

import com.hz.fruit.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 下发的Dao层
 * <p>
 *     下发：商户申请提现
 * </p>
 * @Author yoko
 * @Date 2020/9/8 19:46
 * @Version 1.0
 */
@Mapper
public interface IssueMapper<T> extends BaseDao<T> {
}
