package com.blockchain.wallet.api.service;


import com.blockchain.wallet.api.po.WalletHotPO;

/**
 * @author Administrator
 */
public interface WalletHotService {

    int insertSelective(WalletHotPO record);

    /**
     *  检查账户是否存在
     * @param userName
     * @param walletAddress
     * @param walletType
     * @return
     */
    public boolean isExsitWalletHot(String userName , String walletAddress , String walletType);

   // WalletHotPO selectWalletHotByAddr( String addressHash );

   // WalletHotPO selectWalletHotByUserName( String username );



}
