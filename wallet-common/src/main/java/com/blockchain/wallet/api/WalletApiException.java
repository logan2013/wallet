package com.blockchain.wallet.api;

/**
 * @author shujun
 *
 */
public class WalletApiException extends Exception {

    private static final long serialVersionUID = -7668984320915488533L;

    private int  errCode;
    private String  errMsg;

    public WalletApiException() {
        super();
    }

    public WalletApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletApiException(String message) {
        super(message);
    }

    public WalletApiException(Throwable cause) {
        super(cause);
    }

    public WalletApiException(int errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }
}
