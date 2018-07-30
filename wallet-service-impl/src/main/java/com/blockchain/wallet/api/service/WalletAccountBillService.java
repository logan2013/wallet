package com.blockchain.wallet.api.service;


import com.blockchain.wallet.api.po.WalletAccountBillPO;

/**
 * @author Administrator
 */
public interface WalletAccountBillService {

    int selectWalletAccBillCount(String username , String addr , String accType , String orderNo);

    /**
     *  插入充值中单
     */
    int insertChargeBill(WalletAccountBillPO po);

    WalletAccountBillPO selectByBlockId( String blockTxid);

}
