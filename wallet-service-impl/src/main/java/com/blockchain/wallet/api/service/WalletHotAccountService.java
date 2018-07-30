package com.blockchain.wallet.api.service;


import com.blockchain.wallet.api.dto.WalletHotAccountDTO;
import com.blockchain.wallet.api.po.WalletHotAccountPO;

/**
 * @author Administrator
 */
public interface WalletHotAccountService {

    int insertSelective(WalletHotAccountPO record);


    int updateByPrimaryKeySelective(WalletHotAccountPO record);

    /**
     *  检查账户是已经注册过
     * @param userName
     * @param walletType
     * @return
     */
    public boolean isExsitWalletAddressAccount(String userName,  String walletType);

    WalletHotAccountDTO selectWalletHotByAddr( String walletAddressHash);

    WalletHotAccountDTO selectWalletHotByUserName( String userName);



}
