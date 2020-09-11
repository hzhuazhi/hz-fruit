package com.hz.fruit.master.core.model.bank;

import com.hz.fruit.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 银行/银行卡的实体属性Bean
 * @Author yoko
 * @Date 2020/9/11 15:36
 * @Version 1.0
 */
public class BankModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201121L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 归属手机号ID：对应表tb_fn_mobile_card的主键ID
     */
    private Long mobileCardId;

    /**
     * 银行卡归属类型：对应表tb_fr_bank_type的主键ID
     */
    private Long bankTypeId;

    /**
     * 归属卡商ID：对应表tb_hz_sys_account的主键ID，并且角色是卡商
     */
    private Long merchantId;

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
     * 归属手机卡是否欠费：1未欠费，2欠费
     */
    private Integer isArrears;


    /**
     * 检测状态：1初始化正常，2不正常
     */
    private Integer checkStatus;

    /**
     * 检测被限制的原因:task跑日月总限制，如果被限制，连续给出订单失败会填充被限制的原因
     */
    private String dataExplain;

    /**
     * 是否测试通过：1未通过，2通过；收到银行卡短信，并且解析短信模板配置正确
     */
    private Integer isOk;

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
}
