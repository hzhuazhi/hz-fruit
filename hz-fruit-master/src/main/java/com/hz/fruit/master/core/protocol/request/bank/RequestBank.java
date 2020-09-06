package com.hz.fruit.master.core.protocol.request.bank;

import com.hz.fruit.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议;银行卡
 * @Author yoko
 * @Date 2020/9/5 19:45
 * @Version 1.0
 */
public class RequestBank extends BaseRequest implements Serializable {
    public static final long   serialVersionUID = 1233283332313L;

    /**
     * 银行卡主键ID
     */
    public Long id;


    /**
     * 源头ID：第三方银行卡的主键ID
     */
    public String sourceId;

    /**
     * 银行名称/归属开户行
     */
    public String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    public String bankCard;

    /**
     * 银行支行/支行名称
     */
    public String subbranchName;

    /**
     * 开户名
     */
    public String accountName;

    /**
     * 银行码
     */
    public String bankCode;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    public Integer useStatus;
    

    public RequestBank(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

    public String getSubbranchName() {
        return subbranchName;
    }

    public void setSubbranchName(String subbranchName) {
        this.subbranchName = subbranchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }
}
