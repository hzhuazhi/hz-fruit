package com.hz.fruit.master.core.service.impl;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fruit.master.core.mapper.MerchantBankStrategyMapper;
import com.hz.fruit.master.core.service.MerchantBankStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 银行卡放量策略的Service层的实现层
 * @Author yoko
 * @Date 2020/9/8 18:04
 * @Version 1.0
 */
@Service
public class MerchantBankStrategyServiceImpl<T> extends BaseServiceImpl<T> implements MerchantBankStrategyService<T> {
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
    private MerchantBankStrategyMapper merchantBankStrategyMapper;

    public BaseDao<T> getDao() {
        return merchantBankStrategyMapper;
    }
}
