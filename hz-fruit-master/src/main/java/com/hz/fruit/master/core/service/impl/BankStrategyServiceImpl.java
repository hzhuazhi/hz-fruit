package com.hz.fruit.master.core.service.impl;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fruit.master.core.mapper.BankStrategyMapper;
import com.hz.fruit.master.core.service.BankStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 银行卡放量策略的Service层的实现层
 * @Author yoko
 * @Date 2020/9/11 16:41
 * @Version 1.0
 */
@Service
public class BankStrategyServiceImpl<T> extends BaseServiceImpl<T> implements BankStrategyService<T> {
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
    private BankStrategyMapper bankStrategyMapper;

    public BaseDao<T> getDao() {
        return bankStrategyMapper;
    }
}
