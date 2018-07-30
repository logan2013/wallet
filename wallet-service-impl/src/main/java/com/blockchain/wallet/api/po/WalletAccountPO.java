package com.blockchain.wallet.api.po;

import lombok.Data;
import java.io.Serializable;

@Data
public class WalletAccountPO implements Serializable {

    private Long walletId;

    private String userName;

    private String walletAddressHash;

    private Boolean useStatus;

    private Long balance;

    private Long freezBalance;

    private String accountType;

    private Boolean expireState;

    private Long expireTime;

    private Long createTime;

    private Long lastModifyTime;

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWalletAddressHash() {
        return walletAddressHash;
    }

    public void setWalletAddressHash(String walletAddressHash) {
        this.walletAddressHash = walletAddressHash;
    }

    public Boolean getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Boolean useStatus) {
        this.useStatus = useStatus;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getFreezBalance() {
        return freezBalance;
    }

    public void setFreezBalance(Long freezBalance) {
        this.freezBalance = freezBalance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Boolean getExpireState() {
        return expireState;
    }

    public void setExpireState(Boolean expireState) {
        this.expireState = expireState;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}