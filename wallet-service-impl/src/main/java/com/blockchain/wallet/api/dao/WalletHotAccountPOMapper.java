package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.dto.WalletHotAccountDTO;
import com.blockchain.wallet.api.po.WalletHotAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WalletHotAccountPOMapper {
    int deleteByPrimaryKey(Long accountId);

    int insert(WalletHotAccountPO record);

    int insertSelective(WalletHotAccountPO record);

    WalletHotAccountPO selectByPrimaryKey(Long accountId);

    int updateByPrimaryKeySelective(WalletHotAccountPO record);

    int updateByPrimaryKey(WalletHotAccountPO record);


    WalletHotAccountDTO selectWalletHotByAddr(@Param("walletAddressHash") String walletAddressHash);

    WalletHotAccountDTO selectWalletHotByUserName(@Param("userName") String userName);


    WalletHotAccountDTO isExsitWalletAddressAccount(@Param("userName") String userName , @Param("accountType") String accountType);


}