package com.blockchain.wallet.api.po;

public class walletBussinessMqMsgPO {
    private Long msgId;

    private String msgClazzType;

    private Integer expCode;

    private Long createTime;

    private Long lastModifyTime;

    private String bussinessType;

    private String state;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgClazzType() {
        return msgClazzType;
    }

    public void setMsgClazzType(String msgClazzType) {
        this.msgClazzType = msgClazzType == null ? null : msgClazzType.trim();
    }

    public Integer getExpCode() {
        return expCode;
    }

    public void setExpCode(Integer expCode) {
        this.expCode = expCode;
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

    public String getBussinessType() {
        return bussinessType;
    }

    public void setBussinessType(String bussinessType) {
        this.bussinessType = bussinessType == null ? null : bussinessType.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }
}