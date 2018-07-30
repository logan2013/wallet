package com.blockchain.wallet.api.service.impl;

import com.blockchain.wallet.api.dao.WalletHotAccountPOMapper;
import com.blockchain.wallet.api.dto.WalletHotAccountDTO;
import com.blockchain.wallet.api.po.WalletHotAccountPO;
import com.blockchain.wallet.api.service.WalletHotAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrator
 * @date 2018/7/13
 */
@Component
public class WalletHotAccountServiceImpl implements WalletHotAccountService {


    @Autowired
    WalletHotAccountPOMapper mapper;


    @Override
    public int insertSelective(WalletHotAccountPO record) {
        return mapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(WalletHotAccountPO record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public boolean isExsitWalletAddressAccount(String userName, String walletType) {
        WalletHotAccountDTO dto = mapper.isExsitWalletAddressAccount(userName , walletType);
        if(dto == null){
            return false;
        }
        return true;
    }

    @Override
    public WalletHotAccountDTO selectWalletHotByAddr(String walletAddressHash) {
        return mapper.selectWalletHotByAddr(walletAddressHash);
    }

    @Override
    public WalletHotAccountDTO selectWalletHotByUserName(String userName) {
        return mapper.selectWalletHotByUserName(userName);
    }


}
