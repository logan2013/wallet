package com.blockchain.wallet.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Administrator
 * @date 2018/6/28
 */
@Data
public class WalletApiRegisterResponseDTO extends WalletApiResponseDTO {

    private String userId;
    // 平台对于记录的公钥
    private String platPublicKey;
    private String exchageWaPublicKey;



}
