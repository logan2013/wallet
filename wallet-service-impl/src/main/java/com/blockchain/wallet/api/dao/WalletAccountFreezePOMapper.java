package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.WalletAccountFreezePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface WalletAccountFreezePOMapper {

    int deleteByPrimaryKey(Long freezeId);

    int insert(WalletAccountFreezePO record);

    int insertSelective(WalletAccountFreezePO record);

    WalletAccountFreezePO selectByPrimaryKeyLock(Long freezeId);

    int updateByPrimaryKeySelective(WalletAccountFreezePO record);

    int updateByPrimaryKey(WalletAccountFreezePO record);

    WalletAccountFreezePO selectByBizNo(@Param("bizNo") String bizNo);

    WalletAccountFreezePO selectByFreezeNo(@Param("freezeNo") String freezeNo);

    int updateByfreezeNo(WalletAccountFreezePO record);


    List<WalletAccountFreezePO> selectWithdrawBillRetryPos(@Param("times") Long times , @Param("msgState") String msgState);
}