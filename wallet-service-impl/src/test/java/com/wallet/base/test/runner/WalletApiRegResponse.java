package com.wallet.base.test.runner;

import lombok.Data;

/**
 * Created by Administrator on 2018/7/27.
 */
@Data
public class WalletApiRegResponse {

    private Integer code;

    private String message;

    private Response data;

    private String signType;

    private String sign;

    private Long timestamp;


    @Data
    public static class Response {

        private String walletAddress;
        private String accUserName;

        private String platPublicKey;

        private String exchageWaPublicKey;


    }



}
