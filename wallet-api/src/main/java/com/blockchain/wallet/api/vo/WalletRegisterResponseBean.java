package com.blockchain.wallet.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author shujun
 * @date 2018/6/26
 */
@Data
public class WalletRegisterResponseBean implements Serializable {


    private static final long serialVersionUID = -7146923551516843972L;


    private String exchangeWalletPublicKey;

    /**
     *  系统注册时候，生成商户的PublicKey, 提供给钱包平台，私钥对方平台自己保存（后续请求会用此私钥签名）
     */
    private String walletPublicKey;

    /**
     *  账号，平台唯一用户
     */
    private String appUser;

    /**
     *  钱包地址
     */
    private String walletAddress;
}
