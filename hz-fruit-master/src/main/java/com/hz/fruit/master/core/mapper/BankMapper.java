package com.hz.fruit.master.core.mapper;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.model.bank.BankModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行的Dao层
 * @Author yoko
 * @Date 2020/5/18 19:04
 * @Version 1.0
 */
@Mapper
public interface BankMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新银行卡的使用状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/5 19:21
     */
    public int updateUseStatus(BankModel model);
}
