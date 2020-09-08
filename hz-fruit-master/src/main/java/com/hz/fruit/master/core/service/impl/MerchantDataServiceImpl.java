package com.hz.fruit.master.core.service.impl;

import com.hz.fruit.master.core.common.dao.BaseDao;
import com.hz.fruit.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fruit.master.core.mapper.MerchantDataMapper;
import com.hz.fruit.master.core.service.MerchantDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 卡商收款回调数据的Service层的实现层
 * @Author yoko
 * @Date 2020/9/8 18:04
 * @Version 1.0
 */
@Service
public class MerchantDataServiceImpl<T> extends BaseServiceImpl<T> implements MerchantDataService<T> {
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
    private MerchantDataMapper merchantDataMapper;

    public BaseDao<T> getDao() {
        return merchantDataMapper;
    }
}
