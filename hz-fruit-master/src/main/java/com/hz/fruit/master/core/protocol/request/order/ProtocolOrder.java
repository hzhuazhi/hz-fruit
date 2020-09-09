package com.hz.fruit.master.core.protocol.request.order;

import java.io.Serializable;

/**
 * @Description 出码的协议
 * @Author yoko
 * @Date 2020/9/9 10:54
 * @Version 1.0
 */
public class ProtocolOrder implements Serializable {
    private static final long   serialVersionUID = 1233283332519L;

    /**
     * 订单金额
     */
    public String money;

    /**
     * 支付类型：1支付宝转卡，2卡转卡
     */
    public Integer payType;

    /**
     * 商家订单号
     */
    public String outTradeNo;

    /**
     * 商户秘钥
     */
    public String secretKey;

    /**
     * 同步地址
     */
    public String notifyUrl;

    /**
     * 支付成功之后自动跳转的地址
     */
    public String returnUrl;

    public ProtocolOrder(){

    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
