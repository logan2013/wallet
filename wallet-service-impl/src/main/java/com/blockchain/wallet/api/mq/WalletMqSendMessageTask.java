package com.blockchain.wallet.api.mq;

import com.blockchain.wallet.api.dto.WalletApiWithdrawRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author shujun
 * @date 2018/7/18
 */
public class WalletMqSendMessageTask implements Runnable {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitmqConfig config;
    private final WalletApiWithdrawRequestDTO dto;

    public WalletMqSendMessageTask(RabbitTemplate rabbitTemplate, RabbitmqConfig config,WalletApiWithdrawRequestDTO dto) {
        this.rabbitTemplate = rabbitTemplate;
        this.config = config;
        this.dto = dto;
    }

    @Override
    public void run() {
        rabbitTemplate.convertAndSend(config.getWithdraw().getExchange(), config.getWithdraw().getRouteKey(), dto , new CorrelationData(dto.getBizNo()));
    }
}
