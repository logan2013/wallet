package com.blockchain.wallet.api.po;

public class walletHotOut {
    private Integer entryId;

    private Integer walletId;

    private String walletAddressHash;

    private String accountType;

    private Long createTime;

    private String txid;

    private String toWalletAddress;

    private String amount;

    private Long absentFee;

    private String actualFee;

    private Long timestamp;

    private String version;

    private String source;

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }

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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid == null ? null : txid.trim();
    }

    public String getToWalletAddress() {
        return toWalletAddress;
    }

    public void setToWalletAddress(String toWalletAddress) {
        this.toWalletAddress = toWalletAddress == null ? null : toWalletAddress.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getAbsentFee() {
        return absentFee;
    }

    public void setAbsentFee(Long absentFee) {
        this.absentFee = absentFee;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }
}