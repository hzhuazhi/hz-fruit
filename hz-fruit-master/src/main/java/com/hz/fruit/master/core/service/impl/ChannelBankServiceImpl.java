package com.hz.fruit.master.core.service.impl;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fruit.master.core.mapper.ChannelBankMapper;
import com.hz.fruit.master.core.model.channel.ChannelBankModel;
import com.hz.fruit.master.core.service.ChannelBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 商户与银行卡的绑定关系的Service层的实现层
 * @Author yoko
 * @Date 2020/9/8 20:54
 * @Version 1.0
 */
@Service
public class ChannelBankServiceImpl<T> extends BaseServiceImpl<T> implements ChannelBankService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 3分钟
     */
    public long THREE_MIN = 180;

    /**
     * 11分钟.
     */
    public long ELEVEN_MIN = 660;

    public long TWO_HOUR = 2;



    @Autowired
    private ChannelBankMapper channelBankMapper;

    public BaseDao<T> getDao() {
        return channelBankMapper;
    }

    @Override
    public List<Long> getBankRelationList(ChannelBankModel model) {
        return channelBankMapper.getBankRelationList(model);
    }
}
