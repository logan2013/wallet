package com.blockchain.wallet.api.request;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.response.Response;
import com.blockchain.wallet.api.response.ResponseMessage;
import com.blockchain.wallet.api.response.ResponseMessageInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author shujun
 * @date 2018/6/26
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletRsaResponse  implements Serializable {


    private static final long serialVersionUID = -2714565572064941526L;

    private int code;
    private String message;
    private String bizContent;

    public WalletRsaResponse() {
        super();
    }
    // RSA , RSA2 算法支持
    private String signType = WalletApiConstants.SIGN_TYPE_RSA;

    private WalletRsaResponse(ResponseMessageInterface msg) {
        this.code = msg.getCode();
        this.message = msg.getMessage();
    }

    private WalletRsaResponse(ResponseMessageInterface msg, String bizContent) {
        this.code = msg.getCode();
        this.message = msg.getMessage();
        this.bizContent = bizContent;
    }

    @JsonIgnore
    private String sign;
    /**
     * 客户端调用接口的时间戳 到毫秒
     */
    private Long timestamp;



    public static  WalletRsaResponse success() {
        WalletRsaResponse obj =  new WalletRsaResponse(ResponseMessage.SUCCESS);
        obj.setSignType(WalletApiConstants.SIGN_TYPE_RSA);
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }

    public static  WalletRsaResponse success(String body) {
        WalletRsaResponse obj =  new WalletRsaResponse(ResponseMessage.SUCCESS ,  body);
        obj.setSignType(WalletApiConstants.SIGN_TYPE_RSA);
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }



    public static  WalletRsaResponse error(String msg) {
        WalletRsaResponse obj = new WalletRsaResponse(ResponseMessage.ERROR);
        obj.setMessage(msg);
        obj.setSignType(WalletApiConstants.SIGN_TYPE_RSA);
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }

    public static  WalletRsaResponse error(String msg ,String data) {
        WalletRsaResponse obj = new WalletRsaResponse(ResponseMessage.ERROR);
        obj.setBizContent(data);
        obj.setMessage(msg);
        obj.setSignType(WalletApiConstants.SIGN_TYPE_RSA);
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }



}
