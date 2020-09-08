package com.hz.fruit.master.core.service;

import com.hz.fruit.master.core.common.service.BaseService;
import com.hz.fruit.master.core.model.bank.BankModel;

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
    public int updateUseStatus(BankModel model);
}