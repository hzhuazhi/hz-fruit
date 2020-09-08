package com.hz.fruit.master.core.model.merchant;

import com.hz.fruit.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 卡商银行/卡商银行卡的实体属性Bean
 * @Author yoko
 * @Date 2020/9/8 15:04
 * @Version 1.0
 */
public class MerchantBankModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201101L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 订单归属的卡商ID：对应表tb_fr_merchant的主键ID
     */
    private Long merchantId;

    /**
     * 银行卡归属类型：对应表tb_fr_bank_type的主键ID
     */
    private Long bankTypeId;

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
     * 收款日限金额
     */
    private String inDayMoney;

    /**
     * 收款月限金额
     */
    private String inMonthMoney;

    /**
     * 转账日限金额
     */
    private String outDayMoney;

    /**
     * 转账月限金额
     */
    private String outMonthMoney;

    /**
     * 开户地省份/所在省份
     */
    private String province;

    /**
     * 开户地城市/所在城市
     */
    private String city;

    /**
     * 检测状态：1初始化正常，2不正常
     */
    private Integer checkStatus;

    /**
     * 检测被限制的原因:task跑日月总限制，如果被限制，连续给出订单失败会填充被限制的原因
     */
    private String checkInfo;

    /**
     * 补充数据的类型：1初始化，2补充数据失败（其它原因等..），3补充数据成功
     */
    private Integer workType;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(Long bankTypeId) {
        this.bankTypeId = bankTypeId;
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

    public String getInDayMoney() {
        return inDayMoney;
    }

    public void setInDayMoney(String inDayMoney) {
        this.inDayMoney = inDayMoney;
    }

    public String getInMonthMoney() {
        return inMonthMoney;
    }

    public void setInMonthMoney(String inMonthMoney) {
        this.inMonthMoney = inMonthMoney;
    }

    public String getOutDayMoney() {
        return outDayMoney;
    }

    public void setOutDayMoney(String outDayMoney) {
        this.outDayMoney = outDayMoney;
    }

    public String getOutMonthMoney() {
        return outMonthMoney;
    }

    public void setOutMonthMoney(String outMonthMoney) {
        this.outMonthMoney = outMonthMoney;
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

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
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
}
