package com.blockchain.wallet.api.request;

import com.blockchain.wallet.api.WalletApiConstants;
import com.blockchain.wallet.api.internal.util.JSON;
import com.blockchain.wallet.api.internal.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by shujun on 2018/6/26.
 */
public class WalletCommonApiRequest implements WalletApiRequest  {

    private static final String DEFAULT_URL ="";

    private String notifyUrl;

    private String returnUrl;

    // RSA , RSA2 算法支持
    private String signType = WalletApiConstants.SIGN_TYPE_RSA;

    //@JsonIgnore
    private String sign= DEFAULT_URL ;
    /**
     * 客户端调用接口的时间戳 到毫秒
     */
    private Long timestamp;

    private String requestContent;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getNotifyUrl() {
        return StringUtils.isEmpty(this.notifyUrl) ? DEFAULT_URL : this.notifyUrl ;
    }

    @Override
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String getReturnUrl() {
        return StringUtils.isEmpty(this.returnUrl) ? DEFAULT_URL : this.returnUrl ;
    }

    @Override
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Override
    public String getSignType() {
        return StringUtils.isEmpty(signType) ? WalletApiConstants.SIGN_TYPE_RSA : signType;
    }

    @Override
    public void setSignType(String signType) {
        this.signType = signType;
    }
    @Override
    public String getSign() {
        return sign;
    }
    @Override
    public void setSign(String sign) {
        this.sign = sign;
    }
    @Override
    public Long getTimestamp() {
        return timestamp;
    }
    @Override
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String getRequestContent() {
        return requestContent;
    }
    @Override
    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }



}
