package com.hz.fruit.master.core.service;

import com.hz.fruit.master.core.common.service.BaseService;
import com.hz.fruit.master.core.model.merchant.MerchantModel;

/**
 * @Description 卡商的Service层
 * @Author yoko
 * @Date 2020/9/8 17:06
 * @Version 1.0
 */
public interface MerchantService<T> extends BaseService<T> {
    /**
     * @Description: 更新卡商的使用状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/5 19:21
     */
    public int updateUseStatus(MerchantModel model);
}
