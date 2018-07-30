package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.dto.WalletBlockScanRequestDTO;
import com.blockchain.wallet.api.dto.WalletUsdtMsgDTO;
import com.blockchain.wallet.api.po.WalletHotPO;
import com.blockchain.wallet.api.po.walletHotEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface walletHotEntryMapper {
    int deleteByPrimaryKey(Integer entryId);

    int insert(walletHotEntry record);

    int insertSelective(walletHotEntry record);

    walletHotEntry selectByPrimaryKey(Integer entryId);

    int updateByPrimaryKeySelective(walletHotEntry record);

    int updateByPrimaryKey(walletHotEntry record);

    List<walletHotEntry> selectWalletHotEntry(walletHotEntry record);

    walletHotEntry  selectWalletHotEntryInfo(walletHotEntry record);

    walletHotEntry  selectWalletHotEntryByCount(walletHotEntry record);

   List<WalletUsdtMsgDTO> selectWalletHotEntryByblockScan(walletHotEntry record);

    int updateByHeightSelective(walletHotEntry record);

    List<walletHotEntry> selectWalletAccount(@Param("txid") String txid , @Param("msgState") String msgState , @Param("walletAddressHash") String walletAddressHash);



}