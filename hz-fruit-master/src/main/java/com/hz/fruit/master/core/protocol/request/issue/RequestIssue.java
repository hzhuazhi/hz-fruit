package com.hz.fruit.master.core.protocol.request.issue;

import com.hz.fruit.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：下发
 * @Author yoko
 * @Date 2020/9/23 14:05
 * @Version 1.0
 */
public class RequestIssue extends BaseRequest implements Serializable {
    public static final long   serialVersionUID = 1233283332413L;

    public RequestIssue(){

    }


    /**
     * 支付平台订单号：下游上报的订单号
     */
    public String outTradeNo;

    /**
     * 订单金额
     */
    public String orderMoney;

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
     * 订单状态：1初始化，2超时/失败/审核驳回，3成功
     */
    public Integer orderStatus;

    /**
     * 充值记录银行卡转账图片凭证
     */
    public String pictureAds;

    /**
     * 审核状态：1初始化，2审核收款失败，3审核收款成功
     */
    public Integer checkStatus;

    /**
     *审核失败缘由，审核失败的原因
     */
    public String checkInfo;


    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPictureAds() {
        return pictureAds;
    }

    public void setPictureAds(String pictureAds) {
        this.pictureAds = pictureAds;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }
}
