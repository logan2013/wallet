package com.blockchain.wallet.api.utils;

import lombok.Data;

/**
 *
 * @author Administrator
 * @date 2018/7/10
 */
@Data
public class WalletContainer {

    private String walletAddress;

    private String walletPubKey;

    private String walletPrvKey;

    private String walletPrivateKeyAsWiF;


}
