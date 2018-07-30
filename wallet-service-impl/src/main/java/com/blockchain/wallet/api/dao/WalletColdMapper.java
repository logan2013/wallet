package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.WalletCold;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WalletColdMapper {
    int deleteByPrimaryKey(Integer walletId);

    int insert(WalletCold record);

    int insertSelective(WalletCold record);

    WalletCold selectByPrimaryKey(Integer walletId);

    int updateByPrimaryKeySelective(WalletCold record);

    int updateByPrimaryKey(WalletCold record);

    List<WalletCold> selectWalletAccount(WalletCold record);

}