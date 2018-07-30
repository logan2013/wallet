package com.blockchain.wallet.api.po;

public class walletPlatformOut {
    private Long outId;

    private Long walletId;

    private String walletAddressHash;

    private String accountType;

    private Long createTime;

    private String withdrawNo;

    private String txid;

    private String toWalletAddress;

    private String amount;

    private Long absentFee;

    private String actualFee;

    private Long timestamp;

    private String version;

    private String source;

    private String msgState;


    public Integer getWithdrawLimit() {
        return withdrawLimit;
    }

    public void setWithdrawLimit(Integer withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }

    private Integer withdrawLimit;

    public String getWithdrawState() {
        return withdrawState;
    }

    public void setWithdrawState(String withdrawState) {
        this.withdrawState = withdrawState;
    }

    private String withdrawState;

    public Long getOutId() {
        return outId;
    }

    public void setOutId(Long outId) {
        this.outId = outId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
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

    public String getWithdrawNo() {
        return withdrawNo;
    }

    public void setWithdrawNo(String withdrawNo) {
        this.withdrawNo = withdrawNo == null ? null : withdrawNo.trim();
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

    public String getMsgState() {
        return msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState == null ? null : msgState.trim();
    }
}