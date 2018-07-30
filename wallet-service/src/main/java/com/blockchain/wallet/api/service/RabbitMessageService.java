package com.blockchain.wallet.api.service;

import com.blockchain.wallet.api.dto.WalletApiWithdrawRequestDTO;

/**
 * Created by Administrator on 2018/7/18.
 */
public interface RabbitMessageService {

    void sendWithdrawApply(WalletApiWithdrawRequestDTO message);

}
