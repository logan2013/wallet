package com.blockchain.wallet.api.exception;

import lombok.Data;

/**
 *
 * @author shujun
 * @date 2018/6/28
 */
@Data
public class ApiErrorResponse {

    private int status;
    private int code;
    private String message;

    public ApiErrorResponse(){}

    public ApiErrorResponse(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiErrorResponse{" +
                "status=" + status +
                ", code=" + code +
                ", message=" + message +
                '}';
    }


}
