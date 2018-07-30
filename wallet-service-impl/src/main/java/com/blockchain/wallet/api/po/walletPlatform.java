package com.blockchain.wallet.api.po;

public class walletPlatform {
    private Integer walletId;

    private String walletAddressHash;

    private String origWalletPuk;

    private String origWalletWif;

    private String origWalletPrvk;

    private Long freezBalance;

    private Boolean useStatus;

    private Long balance;

    private String accountType;

    private Boolean expireState;

    private Integer expireTime;

    private Integer createTime;

    private Integer lastModifyTime;

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public String getWalletAddressHash() {
        return walletAddressHash;
    }

    public void setWalletAddressHash(String walletAddressHash) {
        this.walletAddressHash = walletAddressHash == null ? null : walletAddressHash.trim();
    }

    public String getOrigWalletPuk() {
        return origWalletPuk;
    }

    public void setOrigWalletPuk(String origWalletPuk) {
        this.origWalletPuk = origWalletPuk == null ? null : origWalletPuk.trim();
    }

    public String getOrigWalletWif() {
        return origWalletWif;
    }

    public void setOrigWalletWif(String origWalletWif) {
        this.origWalletWif = origWalletWif == null ? null : origWalletWif.trim();
    }

    public String getOrigWalletPrvk() {
        return origWalletPrvk;
    }

    public void setOrigWalletPrvk(String origWalletPrvk) {
        this.origWalletPrvk = origWalletPrvk == null ? null : origWalletPrvk.trim();
    }

    public Long getFreezBalance() {
        return freezBalance;
    }

    public void setFreezBalance(Long freezBalance) {
        this.freezBalance = freezBalance;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public Boolean getExpireState() {
        return expireState;
    }

    public void setExpireState(Boolean expireState) {
        this.expireState = expireState;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Integer lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}