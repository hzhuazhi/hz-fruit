package com.hz.fruit.master.core.service.impl;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fruit.master.core.mapper.MerchantBankCollectionMapper;
import com.hz.fruit.master.core.service.MerchantBankCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 银行收款信息的Service层的实现层
 * @Author yoko
 * @Date 2020/5/21 17:02
 * @Version 1.0
 */
@Service
public class MerchantBankCollectionServiceImpl<T> extends BaseServiceImpl<T> implements MerchantBankCollectionService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private MerchantBankCollectionMapper merchantBankCollectionMapper;

    public BaseDao<T> getDao() {
        return merchantBankCollectionMapper;
    }
}
