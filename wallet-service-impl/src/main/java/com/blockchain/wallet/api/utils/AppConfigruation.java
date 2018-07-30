package com.blockchain.wallet.api.utils;

import com.blockchain.wallet.api.internal.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by shujun on 2018/7/12.
 */
@Configuration
@Slf4j
public class AppConfigruation {

    private final WalletConfig config;

    public AppConfigruation(WalletConfig config) {
        this.config = config;
    }

    /**
     *  UUID 用于生产交易流水号
     * @return
     */
    @Bean
    public SnowflakeIdWorker snowflakeIdWorkerUUid(){
        return new SnowflakeIdWorker(config.getWorkerId() , config.getDatacenterId());
    }
}
