package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.walletHotEntry;
import com.blockchain.wallet.api.po.walletPlatformOut;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface walletPlatformOutMapper {
    int deleteByPrimaryKey(Long outId);

    int insert(walletPlatformOut record);

    int insertSelective(walletPlatformOut record);

    walletPlatformOut selectByPrimaryKey(Long outId);

    int updateByPrimaryKeySelective(walletPlatformOut record);

    int updateByPrimaryKey(walletPlatformOut record);

    walletHotEntry selectWalletPlatformOutInfoByTxId(String  txid);

    walletPlatformOut selectByWithdrawNo(String withdrawNo);

}