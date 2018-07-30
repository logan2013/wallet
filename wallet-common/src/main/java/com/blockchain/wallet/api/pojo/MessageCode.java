package com.blockchain.wallet.api.pojo;

public enum MessageCode {

    OK(200, "OK"),
    SYSTEM_ERR(404, "System error"),
    EORROR(500, "error"),
    MSG_EROOR(401, "save error");

    private int code;
    private String msg;

    MessageCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
