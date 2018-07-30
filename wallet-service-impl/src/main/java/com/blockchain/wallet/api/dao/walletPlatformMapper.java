package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.walletPlatform;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface walletPlatformMapper {
    int deleteByPrimaryKey(Integer walletId);

    int insert(walletPlatform record);

    int insertSelective(walletPlatform record);

    walletPlatform selectByPrimaryKey(Integer walletId);

    List< walletPlatform> selectWallet();

    int updateByPrimaryKeySelective(walletPlatform record);

    int updateByPrimaryKey(walletPlatform record);

    List<walletPlatform> selectwalletPlatformList(walletPlatform record);
}