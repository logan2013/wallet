package com.blockchain.wallet.api.service;


import com.blockchain.wallet.api.exception.BizException;
import com.blockchain.wallet.api.po.WalletAccountPO;

import java.util.List;

public interface WalletAccountService {

    /**
     *  注册账户的时候，初始化一条记录到余额表
     * @param record
     * @return
     */
    public int insertWalletAccount(WalletAccountPO record);


    /**
     *  检查账户是否存在
     * @param userName
     * @param walletAddress
     * @param walletType
     * @return
     */
    public boolean isExsitAccount(String userName , String walletAddress , String walletType);


    List<WalletAccountPO> selectWalletAccount(String userName , String accountType ,  String walletAddressHash) throws BizException;


    WalletAccountPO selectByPrimaryKeyLockRow(Long id);

    int updateByPrimaryKeySelective(WalletAccountPO record);


}
