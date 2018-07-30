package com.blockchain.wallet.api.business;

import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.dto.*;
import com.blockchain.wallet.api.po.WalletAccountPO;
import com.blockchain.wallet.api.request.WalletRsaResponse;

/**
 *
 * @author Administrator
 * @date 2018/7/12
 */
public interface WalletAccountBusiness {

    /**
     *  初始化账户
     * @param po
     */
    void initWalletAccount(WalletAccountPO po);


    /**
     *  充值接口
     * @param request
     * @return
     * @throws WalletApiException
     */
    WalletRsaResponse rechargeOnLine(WalletApiRechargeRequestDTO request)
            throws WalletApiException ;


    WalletRsaResponse cashTrade(WalletApiCashTradeRequestDTO request) throws WalletApiException ;


    WalletRsaResponse withdrawBill(WalletApiWithdrawRequestDTO request) throws WalletApiException ;


    /**
     *  提现确认接口实现
     */
    void withdrawNotifyBill(WalletWithdrawMsgDTO dto);

    void rechargeNotifyBill(WalletUsdtMsgDTO dto);


    /**
     *  提现申请检查
     * @param delayTimes 延迟处理时间
     */
    void withdrawBillRetry(Long delayTimes);



}
