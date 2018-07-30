package com.blockchain.wallet.api.request;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.response.Response;
import com.blockchain.wallet.api.response.ResponseMessage;
import com.blockchain.wallet.api.response.ResponseMessageInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 *
 * @author shujun
 * @date 2018/6/26
 */
public class WalletApiResponse<T> extends Response<T> {

    private static final long serialVersionUID = 1917501254671181819L;
    public WalletApiResponse() {
        super();
    }
    // RSA , RSA2 算法支持
    private String signType = WalletApiConstants.SIGN_TYPE_RSA;

    private WalletApiResponse(ResponseMessageInterface msg) {
        this.code = msg.getCode();
        this.message = msg.getMessage();
    }

    private WalletApiResponse(ResponseMessageInterface msg, T data) {
        this.code = msg.getCode();
        this.message = msg.getMessage();
        this.data = data;
    }

    private String sign;
    /**
     * 客户端调用接口的时间戳 到毫秒
     */
    private Long timestamp;
  //  private T responseContent;

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    public static <T> WalletApiResponse<T> success() {
        WalletApiResponse<T> obj =  new WalletApiResponse(ResponseMessage.SUCCESS);
        obj.setSignType(WalletApiConstants.SIGN_TYPE_RSA);
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }

    public static <T> WalletApiResponse<T> success(T body) {
        WalletApiResponse<T> obj =  new WalletApiResponse(ResponseMessage.SUCCESS ,  body);
        obj.setSignType(WalletApiConstants.SIGN_TYPE_RSA);
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }



    public static <T> WalletApiResponse<T> error(String msg) {
        WalletApiResponse<T> obj = new WalletApiResponse(ResponseMessage.ERROR);
        obj.setMessage(msg);
        obj.setSignType(WalletApiConstants.SIGN_TYPE_RSA);
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }

    public static <T> Response<T> error(T data) {
        WalletApiResponse<T> obj = new WalletApiResponse(ResponseMessage.ERROR);
        obj.setData(data);
        obj.setSignType(WalletApiConstants.SIGN_TYPE_RSA);
        obj.setTimestamp(System.currentTimeMillis());
        return obj;
    }




}
