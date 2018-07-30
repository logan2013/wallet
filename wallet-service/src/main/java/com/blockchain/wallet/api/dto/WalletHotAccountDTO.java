package com.blockchain.wallet.api.dto;

import lombok.Data;

/**
 *
 * @author shujun
 * @date 2018/7/13
 */
@Data
public class WalletHotAccountDTO implements java.io.Serializable{

    private static final long serialVersionUID = 6778641333031003709L;
    private Long accountId;
    private String userName;
    private String userId;
    private String walletPrivateKey;
    private String secretKey;
    private String walletAddressHash;
    private String appPublicKey;

}
