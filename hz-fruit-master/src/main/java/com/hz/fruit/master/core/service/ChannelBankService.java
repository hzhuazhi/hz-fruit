package com.hz.fruit.master.core.service;

import com.hz.fruit.master.core.common.service.BaseService;
import com.hz.fruit.master.core.model.channel.ChannelBankModel;

import java.util.List;

/**
 * @Description 商户与银行卡的绑定关系的Service层
 * @Author yoko
 * @Date 2020/9/8 20:53
 * @Version 1.0
 */
public interface ChannelBankService<T> extends BaseService<T> {

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
