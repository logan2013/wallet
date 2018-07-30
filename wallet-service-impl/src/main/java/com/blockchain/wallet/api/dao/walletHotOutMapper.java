package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.walletHotOut;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface walletHotOutMapper {
    int deleteByPrimaryKey(Integer entryId);

    int insert(walletHotOut record);

    int insertSelective(walletHotOut record);

    walletHotOut selectByPrimaryKey(Integer entryId);

    int updateByPrimaryKeySelective(walletHotOut record);

    int updateByPrimaryKey(walletHotOut record);
}