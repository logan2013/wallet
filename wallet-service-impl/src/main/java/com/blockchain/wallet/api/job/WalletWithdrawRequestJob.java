package com.blockchain.wallet.api.job;

import cn.hk.app.commons.job.annotation.ElasticJobConf;
import com.blockchain.wallet.api.business.WalletAccountBusiness;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 *  实现分布式任务调度
 * @author shujun
 * @date 2018/7/25
 */
@ElasticJobConf(name = "WalletWithdrawRequestJob", cron = "0 0/10 * * ?",
         description = "提现请求定时任务" , eventTraceRdbDataSource="dataSource")
@Slf4j
public class WalletWithdrawRequestJob implements SimpleJob {

    @Autowired
    WalletAccountBusiness bussiness;

    private static final Long DELAY_TIMES = 2*60*1000L;

    /**
     *  定时处理提现申请未成功的请求
     * @param shardingContext
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info(" start job [{}] , ShardingTotalCount : {} ,  ShardingItem : {} task...", shardingContext.getJobName() , shardingContext.getShardingTotalCount(),shardingContext.getShardingItem());
        bussiness.withdrawBillRetry(DELAY_TIMES);
//        Random ra =new Random();
//        int i= ra.nextInt(20000);
//        try {
//            Thread.sleep(new Long(i+1));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        log.info(" end job [{}] , ShardingTotalCount : {} ,  ShardingItem : {} , task...", shardingContext.getJobName() , shardingContext.getShardingTotalCount(),shardingContext.getShardingItem());
    }
}
