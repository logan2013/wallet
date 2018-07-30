package com.blockchain.wallet.api.utils;

import com.blockchain.wallet.api.po.walletHotEntry;
import com.blockchain.wallet.api.service.UsdtServie;
import com.blockchain.wallet.api.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 定时任务
 */
@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private UsdtServie usdtServie;
    @Autowired
    private RedisService redisService;

    @Value("${spring.redis.usdtSet}")
    private  String usdtSet;

    @Value("${spring.redis.minaddress}")
    private int minaddress;


    /**
     * 扫块
     */
    //@Scheduled(fixedDelay = 120000)
    public void blockScan(){
        log.info("***********************开始扫块：{}");
        usdtServie.blockScanUsdt();
        log.info("***********************结束扫块：{}");

        log.info("***********************生成地址开始：{}");
        long num = redisService.countNum(usdtSet);
        if(num < minaddress){
            for(int i =0; i<1000; i++){
                String address = usdtServie.getNewAddress();
                long l = redisService.add(usdtSet,address);
                log.info("***********************缓存池地址添加成功：{} "+l);
            }
        }
        log.info("***********************生成地址结束：{}");
        log.info("***********************发送未修改状态到mq开始：{}");
        int count=usdtServie.sendRabbitMq();
        log.info("***********************修改状态到mq结束,总共处理：{}条",count);
    }


    //归集资金
   // @Scheduled(fixedRate = 180000)
    //@Scheduled(cron = "0 0 12 * * ?")每天中午12点执行
    //@Scheduled(fixedRate = 180000)
    public void merge(){
        log.info("=================开始归集资金");
        usdtServie.merge();
        log.info("=================结束归集资金");
    }

}
