package com.blockchain.wallet.api.po;

/**
 * @author Administrator
 */
public class walletBussinessMqMsgPOWithBLOBs extends walletBussinessMqMsgPO {
    private String msgContent;

    private String expDesc;

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    public String getExpDesc() {
        return expDesc;
    }

    public void setExpDesc(String expDesc) {
        this.expDesc = expDesc == null ? null : expDesc.trim();
    }
}