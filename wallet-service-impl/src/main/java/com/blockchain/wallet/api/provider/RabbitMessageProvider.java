package com.blockchain.wallet.api.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.blockchain.wallet.api.dto.WalletApiWithdrawRequestDTO;
import com.blockchain.wallet.api.mq.WalletWithdrawApply;
import com.blockchain.wallet.api.service.RabbitMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by shujun on 2018/7/18.
 */
@Service
@Slf4j
public class RabbitMessageProvider implements RabbitMessageService {

    @Autowired
    WalletWithdrawApply walletWithdrawApply;

    @Override
    public void sendWithdrawApply(WalletApiWithdrawRequestDTO message) {
        walletWithdrawApply.sendMessage(message);
    }
}
