package com.blockchain.wallet.api.common;

/**
 *
 * @author Administrator
 * @date 2018/7/12
 */
public enum BizTypeConstants {

    /**
     *  充值 提现 手续费 zaixian jiaoyi
     */
    BIZ_CHARGE("1", "BC"),
    BIZ_WITHDRAW("2", "BW"),
    BIZ_FEE_PLATE("3", "BF"),
    BIZ_TRADE_ONLINE("4", "BT"),



    ACCOUNT_BAFREEZ_STATE_1("1" ,"冻结中"),
    ACCOUNT_BAFREEZ_STATE_2("2" ,"已入账"),
    ACCOUNT_BAFREEZ_STATE_3("3" ,"已取消"),


    ACCOUNT_BAFREEZ_REASON_1("1" ,"提现申请冻结"),
    ACCOUNT_BAFREEZ_REASON_2("2" ,"撮合交易冻结"),


    MSG_DELIVER_STATE_1("1" ,"未投递"),
    MSG_DELIVER_STATE_2("2" ,"已投递，未接收处理结果"),
    MSG_DELIVER_STATE_3("3" ,"已处理");




    private String code;
    private String message;

    private BizTypeConstants(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }


}
