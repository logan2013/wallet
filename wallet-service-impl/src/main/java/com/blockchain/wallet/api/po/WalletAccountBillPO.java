package com.blockchain.wallet.api.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class WalletAccountBillPO implements Serializable {

    private Long billId;

    private Long walletId;

    private String txid;

    private String orderNo;

    private String bizType;

    private Long orderAmount;

    private Long beforeBalance;

    private Long balance;

    private String flowType;

    private String accountType;

    private Long createTime;

    private Long lastModifyTime;

    private String source;

    private String blockTxid;

   // private Long absentFee;


}