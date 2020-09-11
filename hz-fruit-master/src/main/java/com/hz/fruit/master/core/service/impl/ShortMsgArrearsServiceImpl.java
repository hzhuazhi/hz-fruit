package com.hz.fruit.master.core.service.impl;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fruit.master.core.mapper.ShortMsgArrearsMapper;
import com.hz.fruit.master.core.service.ShortMsgArrearsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 手机卡欠费短信的Service层的实现层
 * @Author yoko
 * @Date 2020/9/11 15:14
 * @Version 1.0
 */
@Service
public class ShortMsgArrearsServiceImpl<T> extends BaseServiceImpl<T> implements ShortMsgArrearsService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ShortMsgArrearsMapper shortMsgArrearsMapper;

    public BaseDao<T> getDao() {
        return shortMsgArrearsMapper;
    }
}
