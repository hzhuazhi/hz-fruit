package com.hz.fruit.master.core.service;

import com.hz.fruit.master.core.common.service.BaseService;
import com.hz.fruit.master.core.model.merchant.MerchantBankModel;

import java.util.List;

/**
 * @Description 卡商银行/卡商银行卡的Service层
 * @Author yoko
 * @Date 2020/9/8 15:13
 * @Version 1.0
 */
public interface MerchantBankService<T> extends BaseService<T> {

    /**
     * @Description: 更新银行卡的使用状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/5 19:21
     */
    public int updateUseStatus(MerchantBankModel model);

    /**
     * @Description: 获取银行卡以及银行卡策略
     * <p>
     *     1.卡商
     *     2.商户与银行卡绑定关系的
     *     3.商户未与银行卡绑定关系的
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/9 20:17
    */
    public List<MerchantBankModel> getMerchantBankAndStrategy(MerchantBankModel model);
}
