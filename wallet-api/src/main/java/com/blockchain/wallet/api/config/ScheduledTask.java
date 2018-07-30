package com.blockchain.wallet.api.config;

import com.alibaba.fastjson.JSON;
import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.consumer.WalletApiConsumer;
import com.blockchain.wallet.api.wallet.BtcWallet;
import com.blockchain.wallet.api.service.impl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务
 */
@Component
public class ScheduledTask {

    @Autowired
    private RedisService redisService;


    @Autowired
    private WalletApiConsumer consumer;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 预先生成批量btc地址存入redis
     */
    //@Scheduled(fixedRate = 3600000)
    public void produceWallet(){
        System.out.println("开始生成BTC：" + dateFormat.format(new Date()));
        for(int i=0; i<50; i++ ){
            Map<String, String> map = BtcWallet.produceAddress();
            String jsonStr = JSON.toJSONString(map);
            redisService.add("wallet", jsonStr);
        }
        System.out.println("生成BTC完成：" + dateFormat.format(new Date()));
    }

    /**
     * 扫块
     */
   // @Scheduled(fixedRate = 60000)
    public void blockScan() throws WalletApiException {
        System.out.println("=================开始扫块");
        consumer.blockScanUsdt();
        System.out.println("=================结束扫块");
    }

}
