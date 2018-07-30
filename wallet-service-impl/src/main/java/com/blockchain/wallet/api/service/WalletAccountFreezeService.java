package com.blockchain.wallet.api.service;


import com.blockchain.wallet.api.po.WalletAccountFreezePO;
import java.util.List;

/**
 * @author shujun
 */
public interface WalletAccountFreezeService {

    WalletAccountFreezePO selectByBizNo( String bizNo);

    int insertSelective(WalletAccountFreezePO record);

    int updateByfreezeNo(WalletAccountFreezePO record);

    WalletAccountFreezePO selectByFreezeNo( String freezeNo);


    WalletAccountFreezePO selectByPrimaryKeyLock(Long freezeId);

    int updateByPrimaryKeySelective(WalletAccountFreezePO record);

    List<WalletAccountFreezePO> selectWithdrawBillRetryPos( Long times ,  String msgState);





}
