package com.blockchain.wallet.api.po;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class WalletAccountFreezePO implements java.io.Serializable {
    private static final long serialVersionUID = 7827878763984937293L;

    private Long freezeId;

    private String freezeNo;

    private Long walletId;

    private String userName;

    private String accountType;

    private String walletAddressHash;

    private Long freezBalance;

    // 手续费
    private Long absentFee;

    private String bizNo;

    private String source;

    private Long createTime;

    private String toWalletAddress;

    private String toUsername;

    private String freezState;

    private String freezReason;

    private String msgState;


}