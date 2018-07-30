package com.blockchain.wallet.api.request;

import java.io.Serializable;

/**
 *
 * @author Administrator
 * @date 2018/6/26
 */
public interface WalletApiRequest extends Serializable {
    /**
     * 通知url
     *
     * @return
     */
    public String getNotifyUrl();
    /**
     *  通知url
     *
     * @param notifyUrl
     */
    public void setNotifyUrl(String notifyUrl);

    /**
     * 返回url
     *
     * @return
     */
    public String getReturnUrl();

    /**
     *  设置url
     * @param returnUrl
     */
    public void setReturnUrl(String returnUrl);

    public String getSignType() ;

    public void setSignType(String signType) ;

    public String getSign() ;

    public void setSign(String sign) ;

    public Long getTimestamp() ;

    public void setTimestamp(Long timestamp);

    public String getRequestContent() ;

    public void setRequestContent(String requestBody);


}
