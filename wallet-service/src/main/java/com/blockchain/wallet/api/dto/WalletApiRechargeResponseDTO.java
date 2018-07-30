package com.blockchain.wallet.api.dto;

import lombok.Data;

/**
 *
 * @author Administrator
 * @date 2018/6/28
 */
@Data
public class WalletApiRechargeResponseDTO extends WalletApiResponseDTO {

    private String txid;

    private Long balance;

    private Long freezeBalance;

}
