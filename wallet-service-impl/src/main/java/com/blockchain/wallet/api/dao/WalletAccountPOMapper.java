package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.WalletAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface WalletAccountPOMapper {


    int deleteByPrimaryKey(Long walletId);

    int insertSelective(WalletAccountPO record);

    WalletAccountPO selectByPrimaryKey(Long walletId);

    int updateByPrimaryKeySelective(WalletAccountPO record);

    int selectAccountByPo(WalletAccountPO record);

    /**
     *
     * @param userName
     * @param accountType
     * @param walletAddressHash
     * @return
     */
    List<WalletAccountPO> selectWalletAccount(@Param("userName") String userName , @Param("accountType") String accountType ,@Param("walletAddressHash") String walletAddressHash);



}