package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.walletPlatformEntry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface walletPlatformEntryMapper {
    int deleteByPrimaryKey(Integer entryId);

    int insert(walletPlatformEntry record);

    int insertSelective(walletPlatformEntry record);

    walletPlatformEntry selectByPrimaryKey(Integer entryId);

    int updateByPrimaryKeySelective(walletPlatformEntry record);

    int updateByPrimaryKey(walletPlatformEntry record);

    List<walletPlatformEntry> selectWalletColdEntry(walletPlatformEntry record);

}