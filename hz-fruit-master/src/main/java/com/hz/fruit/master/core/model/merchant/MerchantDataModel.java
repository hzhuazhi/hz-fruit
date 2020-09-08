package com.hz.fruit.master.core.model.merchant;

import com.hz.fruit.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 卡商收款回调数据的实体属性Bean
 * @Author yoko
 * @Date 2020/9/8 17:38
 * @Version 1.0
 */
public class MerchantDataModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201106L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 银行卡账号/银行卡号：卡商 银行卡卡号
     */
    private String bankCard;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private String orderMoney;

    /**
     * 订单状态：1初始化，2超时/失败，3质疑，4成功
     */
    private Integer orderStatus;

    /**
     * 收款时间：银行卡收到转账的时间
     */
    private String collectionTime;

    /**
     * 是否是补单：1初始化不是补单，2是补单
     */
    private Integer replenishType;

    /**
     * 是否能匹配到数据关系：1初始化，2匹配不成功，3匹配成功；匹配订单号
     */
    private Integer isMatching;

    /**
     * 未匹配到的缘由
     */
    private String matchingInfo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建日期：存的日期格式20160530
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    private Integer curminute;

    /**
     *运行计算次数
     */
    private Integer runNum;

    /**
     * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer runStatus;

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

    private Integer curdayStart;
    private Integer curdayEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public Integer getReplenishType() {
        return replenishType;
    }

    public void setReplenishType(Integer replenishType) {
        this.replenishType = replenishType;
    }

    public Integer getIsMatching() {
        return isMatching;
    }

    public void setIsMatching(Integer isMatching) {
        this.isMatching = isMatching;
    }

    public String getMatchingInfo() {
        return matchingInfo;
    }

    public void setMatchingInfo(String matchingInfo) {
        this.matchingInfo = matchingInfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
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

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }
}
