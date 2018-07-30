package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.WalletPreinstallAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface WalletPreinstallAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WalletPreinstallAddress record);

    int insertSelective(WalletPreinstallAddress record);

    WalletPreinstallAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletPreinstallAddress record);

    int updateByPrimaryKey(WalletPreinstallAddress record);

    List<WalletPreinstallAddress> selectWalletPreinstallAddressmList(WalletPreinstallAddress record);
}