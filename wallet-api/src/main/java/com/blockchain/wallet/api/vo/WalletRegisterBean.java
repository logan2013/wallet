package com.blockchain.wallet.api.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @author shujun
 * @date 2018/6/26
 */
@Data
public class WalletRegisterBean implements Serializable{

    private static final long serialVersionUID = -978723164231092284L;
    /**
     *  DH 算法中 用于协商生成共享秘钥的 公钥
     *   注册成功后，会收到平台应答的 公钥， 生成AES 对称加密秘钥平台自己保存（私密），
     *   注意保存，后续接口数据加密采用此秘钥对数据进行加密，解密
     *  @See  com.blockchain.wallet.api.internal.util.KeyExchangeAlgorithmEncrypt 算法实现
     */
    private String exchangePublicKey;

    /**
     *  系统注册时候，生成商户的PublicKey, 提供给钱包平台，私钥对方平台自己保存。
     *  （后续请求会用此私钥签名）
     */
    private String appPublicKey;

    /**
     *  商户主键 非必须
     */
    private Long appId;

    /**
     *  账号，平台唯一用户
     */
    private String appUser;


}
