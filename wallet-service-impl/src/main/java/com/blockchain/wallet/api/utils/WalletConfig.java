package com.blockchain.wallet.api.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description : 一期只实现比特币的功能 USDT
 * @author shujun
 * @date 2018/7/10
 */
@Component
@ConfigurationProperties(prefix="wallet")
public class WalletConfig {

    private Long  workerId ;
    private Long  datacenterId;

    private Btcoin btcoin;

    public Btcoin getBtcoin() {
        return btcoin;
    }
    public void setBtcoin(Btcoin btcoin) {
        this.btcoin = btcoin;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(Long datacenterId) {
        this.datacenterId = datacenterId;
    }

    /**
     *  比特币配置
     */
    @Data
    public static class Btcoin {
        //默认为生成环境
        private String env = "prod";
        private String url;
    }

}
