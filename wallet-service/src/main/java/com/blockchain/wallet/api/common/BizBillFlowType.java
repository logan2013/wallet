package com.blockchain.wallet.api.common;

/**
 *
 * @author Administrator
 * @date 2018/7/12
 */
public class BizBillFlowType {
    public static final String FLOWTYPE_INCOME = "1";
    public static final String FLOWTYPE_OUTCOME = "2";

    // 平台充值
    public static final String  FLOWTYPE_INCOME_SOURCE_PT ="1";
    // 扫块
    public static final String  FLOWTYPE_INCOME_SOURCE_SK ="2";

    // 币币线上交易获得
    public static final String  FLOWTYPE_INCOME_SOURCE_JY ="3";
    public static final String  FLOWTYPE_INCOME_SOURCE_TX ="4";


}
