package com.blockchain.wallet.api.mq;

import com.blockchain.wallet.api.dto.WalletUsdtMsgDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

import java.util.List;

/**
 * @author shujun
 * @date 2018/7/18
 */
public class WalletMqBlockScanMessageTask implements Runnable {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitmqConfig config;
    private final WalletUsdtMsgDTO list;

    public WalletMqBlockScanMessageTask(RabbitTemplate rabbitTemplate, RabbitmqConfig config, WalletUsdtMsgDTO dto) {
        this.rabbitTemplate = rabbitTemplate;
        this.config = config;
        this.list = dto;
    }

    @Override
    public void run() {
        rabbitTemplate.convertAndSend(config.getBlockScan().getExchange(), config.getBlockScan().getRouteKey(), list, new CorrelationData(list.getTxid()));
    }
}
