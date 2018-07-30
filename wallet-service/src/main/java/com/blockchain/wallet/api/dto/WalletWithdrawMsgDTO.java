package com.blockchain.wallet.api.dto;

import lombok.Data;

/**
 *提现确认通知
 * @author zhangkai
 * @date 2018/7/19
 */
@Data
public class WalletWithdrawMsgDTO implements java.io.Serializable{


    private static final long serialVersionUID = -6947933043350408211L;

    private Long amount;

    // 区块交易流水号
    private String txid;

    private String bizNo;

    private String walletAddressHash;

    private String accountType;

    private Long timestamp;

    private String userName;

}
