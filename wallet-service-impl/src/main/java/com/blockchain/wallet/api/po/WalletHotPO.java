package com.blockchain.wallet.api.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shujun
 * @date
 */
@Data
public class WalletHotPO  implements Serializable{

    private Long walletId;

    private String userName;

    private Long accountId;

    private String userId;

   // private String walletPrivateKey;

   // private String appPublicKey;

//    private String secretKey;


    private String walletAddressHash;
     private String origWalletPuk;
     private String origWalletWif;
     private String origWalletPrvk;
     private String accountType;

    public Boolean getImportFlag() {
        return importFlag;
    }

    public void setImportFlag(Boolean importFlag) {
        this.importFlag = importFlag;
    }

    private Boolean importFlag;

    private Boolean useStatus;
    private Boolean isDefaultAccount;

    private Boolean expireState;

    private Long balance;

    private Long freezBalance;

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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWalletAddressHash() {
        return walletAddressHash;
    }

    public void setWalletAddressHash(String walletAddressHash) {
        this.walletAddressHash = walletAddressHash;
    }

    public String getOrigWalletPuk() {
        return origWalletPuk;
    }

    public void setOrigWalletPuk(String origWalletPuk) {
        this.origWalletPuk = origWalletPuk;
    }

    public String getOrigWalletWif() {
        return origWalletWif;
    }

    public void setOrigWalletWif(String origWalletWif) {
        this.origWalletWif = origWalletWif;
    }

    public String getOrigWalletPrvk() {
        return origWalletPrvk;
    }

    public void setOrigWalletPrvk(String origWalletPrvk) {
        this.origWalletPrvk = origWalletPrvk;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Boolean getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Boolean useStatus) {
        this.useStatus = useStatus;
    }

    public Boolean getDefaultAccount() {
        return isDefaultAccount;
    }

    public void setDefaultAccount(Boolean defaultAccount) {
        isDefaultAccount = defaultAccount;
    }

    public Boolean getExpireState() {
        return expireState;
    }

    public void setExpireState(Boolean expireState) {
        this.expireState = expireState;
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