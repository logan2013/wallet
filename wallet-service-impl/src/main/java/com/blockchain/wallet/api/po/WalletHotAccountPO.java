package com.blockchain.wallet.api.po;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class WalletHotAccountPO {
    private Long accountId;

    private String userName;

    private String userId;

    private String walletPrivateKey;

    private String appPublicKey;

    private String secretKey;

    private Boolean expireState;

    private Long expireTime;

    private Long createTime;

    private Long lastModifyTime;


}