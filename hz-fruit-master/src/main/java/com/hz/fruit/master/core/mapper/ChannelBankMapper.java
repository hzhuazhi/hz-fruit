package com.hz.fruit.master.core.mapper;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.model.channel.ChannelBankModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 商户与银行卡的绑定关系的Dao层
 * @Author yoko
 * @Date 2020/9/8 20:52
 * @Version 1.0
 */
@Mapper
public interface ChannelBankMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询绑定渠道的银行卡ID集合
     * <p>
     *     这里查询有两个方面：1查询某渠道关联的银行卡ID集合。
     *     2查询所有已关联过渠道的银行卡ID集合。
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/10/9 18:56
    */
    public List<Long> getBankRelationList(ChannelBankModel model);
}
