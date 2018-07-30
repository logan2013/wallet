package com.blockchain.wallet.api.job;

import cn.hk.app.commons.job.annotation.ElasticJobConf;
import com.blockchain.wallet.api.business.WalletAccountBusiness;
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
@ElasticJobConf(name = "WalletMergeBlockchainJob", cron = "0 0/3 * * * ?",
         description = "资金归结定时任务" , eventTraceRdbDataSource="dataSource")
@Slf4j
public class WalletMergeBlockchainJob implements SimpleJob {


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
        usdtServie.merge();
        log.info("job [{}] , ShardingTotalCount : {} ,  ShardingItem : {} , end...", shardingContext.getJobName() , shardingContext.getShardingTotalCount(),shardingContext.getShardingItem());
    }
}
