package com.blockchain.wallet.api.dto;


import lombok.Data;
/**
 *
 * @author zhangkai
 * @date 2018/7/19
 */
@Data
public class WalletBlockScanRequestDTO implements java.io.Serializable{


    private static final long serialVersionUID = 3542001128139566335L;
    private Long amount;

    private String txid;

    private String walletAddressHash;

    private String accountType;

    private Long timestamp;

    private String userName;


}
