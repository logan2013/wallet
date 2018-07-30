package com.blockchain.wallet.api.business;

import com.blockchain.wallet.api.po.walletPlatformOut;

public interface UsdtWithdrawBusiness {

    public int withdraw(String receiveAddress, String amount, String withdrawNo, int limit);

    public walletPlatformOut withdrawLimit(String witddrawNo);
}
