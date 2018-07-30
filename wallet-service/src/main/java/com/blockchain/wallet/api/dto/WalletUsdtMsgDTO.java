package com.blockchain.wallet.api.dto;

import lombok.Data;

/**
 * Created by Administrator on 2018/7/20.
 */
@Data
public class WalletUsdtMsgDTO implements java.io.Serializable{

    private static final long serialVersionUID = -2155800435180121401L;
    private Long amount;

    private String txid;

    private String walletAddressHash;

    private String accountType;

    private Long timestamp;

    private String userName;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getWalletAddressHash() {
        return walletAddressHash;
    }

    public void setWalletAddressHash(String walletAddressHash) {
        this.walletAddressHash = walletAddressHash;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
