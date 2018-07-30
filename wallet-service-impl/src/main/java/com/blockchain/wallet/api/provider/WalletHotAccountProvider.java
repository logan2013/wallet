package com.blockchain.wallet.api.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.blockchain.wallet.api.dto.WalletHotAccountDTO;
import com.blockchain.wallet.api.service.WalletHotAccountService;
import com.blockchain.wallet.api.service.WalletHotAccountProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2018/7/13.
 */
@Service
@Slf4j
public class WalletHotAccountProvider implements WalletHotAccountProviderService {


    @Autowired
    private WalletHotAccountService walletHotAccountService;

    @Override
    public WalletHotAccountDTO selectWalletHotByAddr(String addressHash) {
        WalletHotAccountDTO dot = walletHotAccountService.selectWalletHotByAddr(addressHash);
        return dot;
    }

    @Override
    public WalletHotAccountDTO selectWalletHotByUserName(String username) {
        return walletHotAccountService.selectWalletHotByUserName(username);
    }
}
