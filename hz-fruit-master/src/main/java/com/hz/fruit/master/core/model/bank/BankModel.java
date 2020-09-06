package com.hz.fruit.master.core.model.bank;

import com.hz.fruit.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 银行的实体属性Bean
 * @Author yoko
 * @Date 2020/9/5 19:10
 * @Version 1.0
 */
public class BankModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201102L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 归属手机号ID：对应表tb_fr_mobile_card的主键ID
     */
    private Long mobileCardId;

    /**
     * 银行卡归属类型：对应表tb_fr_bank_type的主键ID
     */
    private Long bankTypeId;

    /**
     * 源头ID：第三方银行卡的主键ID
     */
    private String sourceId;

    /**
     * 银行名称/归属开户行
     */
    private String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    private String bankCard;

    /**
     * 银行支行/支行名称
     */
    private String subbranchName;

    /**
     * 开户名
     */
    private String accountName;

    /**
     * 银行码
     */
    private String bankCode;

    /**
     * 开户地省份/所在省份
     */
    private String province;

    /**
     * 开户地城市/所在城市
     */
    private String city;

    /**
     * 回调对照凭证短信来源:例如招商银行的是95555
     */
    private String smsNum;

    /**
     * 回调对照凭证银行卡尾号：收到短信后，短信内容尾号xxxx收款
     */
    private String lastNum;

    /**
     * 银行归属卡商
     */
    private String cardMerchant;

    /**
     * 触发短信提醒的金额
     */
    private String triggerMoney;

    /**
     * 转账限量金额
     */
    private String outMoney;

    /**
     * 对接ID：支付宝内的银行卡ID
     */
    private String dockingId;

    /**
     * 开卡时间：第一次放量派发订单的时间
     */
    private String openTime;

    /**
     * 银行卡归属：1归属我方，2归属卡商，3归属第三方
     */
    private Integer ascriptionType;

    /**
     * 归属手机卡是否欠费：1未欠费，2欠费
     */
    private Integer isArrears;

    /**
     *是否测试通过：1未通过，2通过；收到银行卡短信，并且解析短信模板配置正确
     */
    private Integer isOk;

    /**
     * 被限制的原因:task跑日月总限制，如果被限制，会填充被限制的原因
     */
    private String limitInfo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    private Integer useStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    private Integer yn;

    /**
     * 手机卡ID集合
     * SQL查询条件=in
     */
    private List<Long> mobileCardIdList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMobileCardId() {
        return mobileCardId;
    }

    public void setMobileCardId(Long mobileCardId) {
        this.mobileCardId = mobileCardId;
    }

    public Long getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(Long bankTypeId) {
        this.bankTypeId = bankTypeId;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(String smsNum) {
        this.smsNum = smsNum;
    }

    public String getLastNum() {
        return lastNum;
    }

    public void setLastNum(String lastNum) {
        this.lastNum = lastNum;
    }

    public String getCardMerchant() {
        return cardMerchant;
    }

    public void setCardMerchant(String cardMerchant) {
        this.cardMerchant = cardMerchant;
    }

    public String getTriggerMoney() {
        return triggerMoney;
    }

    public void setTriggerMoney(String triggerMoney) {
        this.triggerMoney = triggerMoney;
    }

    public String getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(String outMoney) {
        this.outMoney = outMoney;
    }

    public String getDockingId() {
        return dockingId;
    }

    public void setDockingId(String dockingId) {
        this.dockingId = dockingId;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public Integer getAscriptionType() {
        return ascriptionType;
    }

    public void setAscriptionType(Integer ascriptionType) {
        this.ascriptionType = ascriptionType;
    }

    public Integer getIsArrears() {
        return isArrears;
    }

    public void setIsArrears(Integer isArrears) {
        this.isArrears = isArrears;
    }

    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }

    public String getLimitInfo() {
        return limitInfo;
    }

    public void setLimitInfo(String limitInfo) {
        this.limitInfo = limitInfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public List<Long> getMobileCardIdList() {
        return mobileCardIdList;
    }

    public void setMobileCardIdList(List<Long> mobileCardIdList) {
        this.mobileCardIdList = mobileCardIdList;
    }
}
