package com.blockchain.wallet.api.response;

/**
 * Created by shujun on 2018/6/26.
 */
public abstract interface ResponseMessageInterface {

    public abstract int getCode();

    public abstract String getMessage();

    public static class Default implements ResponseMessageInterface {
        private final int code;
        private final String message;

        Default(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
