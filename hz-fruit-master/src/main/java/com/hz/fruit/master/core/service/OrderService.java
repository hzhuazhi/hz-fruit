package com.hz.fruit.master.core.service;


import com.hz.fruit.master.core.common.service.BaseService;
import com.hz.fruit.master.core.model.bank.BankModel;
import com.hz.fruit.master.core.model.order.OrderModel;
import com.hz.fruit.master.core.model.strategy.StrategyData;

import java.util.List;

/**
 * @Description 任务订单的Service层
 * @Author yoko
 * @Date 2020/5/21 19:34
 * @Version 1.0
 */
public interface OrderService<T> extends BaseService<T> {

    /**
     * @Description: 筛选可用的银行卡
     * @param bankList - 银行卡集合
     * @param orderMoney - 订单金额
     * @param payType - 支付类型
     * @param orderMoneyLockTime - 银行卡金额的锁定时间
     * @return
     * @author yoko
     * @date 2020/9/12 20:49
    */
    public BankModel screenBank(List<BankModel> bankList, String orderMoney, int payType, int orderMoneyLockTime);


    /**
     * @Description: 根据订单号查询订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/8 20:00
     */
    public int getOrderStatus(OrderModel model);


    /**
     * @Description: 筛选可用的银行卡
     * @param bankList - 银行卡集合
     * @param orderMoney - 订单金额
     * @param payType - 支付类型
     * @param orderMoneyLockTime - 银行卡金额的锁定时间
     * @param bankMoneyOut - 浮动：动态：1为减，2加，3随机加减，4为整数
     * @param moneyList - 浮动的金额范围集合
     * @return
     * @author yoko
     * @date 2020/9/12 20:49
     */
    public BankModel screenBankByMoney(List<BankModel> bankList, String orderMoney, int payType, int orderMoneyLockTime, int bankMoneyOut, List<StrategyData> moneyList);
}
