package com.blockchain.wallet.api.po;

public class WalletPreinstallAddress {
    private Integer id;

    private String walletAddressHash;

    private Long createTime;

    private Integer walletId;

    private Boolean useStatus;

    private Boolean expireState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWalletAddressHash() {
        return walletAddressHash;
    }

    public void setWalletAddressHash(String walletAddressHash) {
        this.walletAddressHash = walletAddressHash == null ? null : walletAddressHash.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public Boolean getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Boolean useStatus) {
        this.useStatus = useStatus;
    }

    public Boolean getExpireState() {
        return expireState;
    }

    public void setExpireState(Boolean expireState) {
        this.expireState = expireState;
    }
}