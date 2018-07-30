package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.WalletAccountBillPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WalletAccountBillPOMapper {
    int deleteByPrimaryKey(Long billId);

    int insert(WalletAccountBillPO record);

    int insertSelective(WalletAccountBillPO record);

    WalletAccountBillPO selectByPrimaryKey(Long billId);

    int updateByPrimaryKeySelective(WalletAccountBillPO record);

    int updateByPrimaryKey(WalletAccountBillPO record);

    int selectWalletAccBillByPo( @Param("userName") String username, @Param("walletAddressHash") String addr, @Param("accountType") String accType, @Param("orderNo") String orderNo);

    WalletAccountBillPO selectByBlockId(@Param("blockTxid") String blockTxid);


}