package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.WalletAccountPO;
import com.blockchain.wallet.api.po.WalletHotPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author shujun
 */
@Mapper
public interface WalletHotPOMapper {

    int insertSelective(WalletHotPO record);

    int updateByPrimaryKeySelective(WalletHotPO record);

    int selectWalletHotByPo(WalletHotPO record);

    List<WalletHotPO> selectWalletHot();
    /**
     *
     * @param userName
     * @param accountType
     * @param walletAddressHash
     * @return
     */
    List<WalletHotPO> selectWalletAccount(@Param("userName") String userName , @Param("accountType") String accountType , @Param("walletAddressHash") String walletAddressHash);

}