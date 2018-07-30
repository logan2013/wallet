package com.blockchain.wallet.api.service;

import com.blockchain.wallet.api.dto.WalletHotAccountDTO;

/**
 *
 * @author Administrator
 * @date 2018/7/13
 */
public interface WalletHotAccountProviderService {


    public WalletHotAccountDTO selectWalletHotByAddr(String addressHash);

    public WalletHotAccountDTO selectWalletHotByUserName(String username);

}
