package com.blockchain.wallet.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author shujun
 * @date 2018/7/12
 */
@Data
public class WalletApiResponseDTO implements Serializable {

    private static final long serialVersionUID = -2216181656620980989L;

    // 钱包 hash地址
    protected String walletAddress;

    protected Long timestamp;

    protected String accUserName;

    protected String walletType;


}
