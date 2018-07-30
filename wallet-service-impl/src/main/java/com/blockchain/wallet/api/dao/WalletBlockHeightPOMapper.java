package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.WalletBlockHeightPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WalletBlockHeightPOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WalletBlockHeightPO record);

    int insertSelective(WalletBlockHeightPO record);

    WalletBlockHeightPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletBlockHeightPO record);

    int updateByPrimaryKey(WalletBlockHeightPO record);
}