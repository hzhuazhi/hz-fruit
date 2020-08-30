package com.hz.fruit.master.core.protocol.request.order;


import com.hz.fruit.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：任务订单（平台派发订单）
 * @Author yoko
 * @Date 2020/5/22 10:54
 * @Version 1.0
 */
public class RequestOrder extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332513L;


    /**
     * 订单金额
     */
    public String money;

    /**
     * 支付成功之后自动跳转的地址
     */
    public String returnUrl;

    /**
     * 银行名称/归属开户行
     */
    public String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    public String bankCard;

    /**
     * 开户名
     */
    public String accountName;

    /**
     * 是否302跳转
     * 为空302进行跳转，不为空返回地址
     */
    public String noredirect;

    /**
     * 订单号
     */
    public String orderNo;

    public RequestOrder(){

    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNoredirect() {
        return noredirect;
    }

    public void setNoredirect(String noredirect) {
        this.noredirect = noredirect;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
