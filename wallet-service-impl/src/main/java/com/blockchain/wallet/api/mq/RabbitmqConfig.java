package com.blockchain.wallet.api.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2018/7/5
 */
@Component
@ConfigurationProperties(prefix = "wallet.bill")
public class RabbitmqConfig {

    private final BussinessBinding recharge = new BussinessBinding();
    private final BussinessBinding withdraw = new BussinessBinding();
    private final BussinessBinding withdrawNotify = new BussinessBinding();
    private final BussinessBinding rechargeNotify = new BussinessBinding();
    private final BussinessBinding blockScan = new BussinessBinding();

    public BussinessBinding getRechargeNotify() {
        return rechargeNotify;
    }
    public BussinessBinding getWithdrawNotify() {
        return withdrawNotify;
    }

    public BussinessBinding getRecharge() {
        return recharge;
    }

    public BussinessBinding getWithdraw() {
        return withdraw;
    }

    public BussinessBinding getBlockScan() {
        return blockScan;
    }

    @Data
    public static class BussinessBinding {
        private String queue;
        private String exchange;
        private String routeKey;
    }

}
