package com.blockchain.wallet.api.dao;

import com.blockchain.wallet.api.po.walletBussinessMqMsgPO;
import com.blockchain.wallet.api.po.walletBussinessMqMsgPOWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface walletBussinessMqMsgPOMapper {
    int deleteByPrimaryKey(Long msgId);

    int insert(walletBussinessMqMsgPOWithBLOBs record);

    int insertSelective(walletBussinessMqMsgPOWithBLOBs record);

    walletBussinessMqMsgPOWithBLOBs selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(walletBussinessMqMsgPOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(walletBussinessMqMsgPOWithBLOBs record);

    int updateByPrimaryKey(walletBussinessMqMsgPO record);
}