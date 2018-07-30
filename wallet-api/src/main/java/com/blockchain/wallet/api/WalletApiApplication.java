package com.blockchain.wallet.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author shujun
 * @date 2018/6/26
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class WalletApiApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(WalletApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
