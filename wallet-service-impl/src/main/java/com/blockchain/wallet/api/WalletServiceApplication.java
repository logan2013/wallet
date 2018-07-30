package com.blockchain.wallet.api;

import cn.hk.app.commons.job.annotation.EnableElasticJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author shujun
 * @date 2018/6/27
 */
@EnableTransactionManagement
@SpringBootApplication
@EnableElasticJob
public class WalletServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }
}
