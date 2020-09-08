package com.hz.fruit.master.core.service.impl;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fruit.master.core.mapper.ChannelMapper;
import com.hz.fruit.master.core.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 商户信息的Service层的实现层
 * @Author yoko
 * @Date 2020/9/8 20:54
 * @Version 1.0
 */
@Service
public class ChannelServiceImpl<T> extends BaseServiceImpl<T> implements ChannelService<T> {
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
    private ChannelMapper channelMapper;

    public BaseDao<T> getDao() {
        return channelMapper;
    }
}
