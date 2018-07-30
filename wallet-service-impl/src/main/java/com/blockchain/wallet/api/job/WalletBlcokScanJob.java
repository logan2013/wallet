package com.blockchain.wallet.api.job;

import cn.hk.app.commons.job.annotation.ElasticJobConf;
import com.blockchain.wallet.api.service.UsdtServie;
import com.blockchain.wallet.api.service.impl.RedisService;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *  实现分布式任务调度
 * @author zhangkai
 * @date 2018/7/26
 */
@ElasticJobConf(name = "WalletBlcokScanJob", cron = "0 0/3 * * * ?",
         description = "扫块定时任务" , eventTraceRdbDataSource="dataSource")
@Slf4j
public class WalletBlcokScanJob implements SimpleJob {



    @Autowired
    private UsdtServie usdtServie;
    @Autowired
    private RedisService redisService;

    @Value("${spring.redis.usdtSet}")
    private  String usdtSet;

    @Value("${spring.redis.minaddress}")
    private int minaddress;


    /**
     *  定时处理提现申请未成功的请求
     * @param shardingContext
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("job [{}] , ShardingTotalCount : {} ,  ShardingItem : {} , start...", shardingContext.getJobName() , shardingContext.getShardingTotalCount(),shardingContext.getShardingItem());
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
        log.info("job [{}] , ShardingTotalCount : {} ,  ShardingItem : {} , end...", shardingContext.getJobName() , shardingContext.getShardingTotalCount(),shardingContext.getShardingItem());
    }
}
