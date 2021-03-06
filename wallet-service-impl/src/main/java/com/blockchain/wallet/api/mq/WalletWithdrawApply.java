package com.blockchain.wallet.api.mq;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.blockchain.wallet.api.common.BizTypeConstants;
import com.blockchain.wallet.api.dto.WalletApiWithdrawRequestDTO;
import com.blockchain.wallet.api.dto.WalletUsdtMsgDTO;
import com.blockchain.wallet.api.po.WalletAccountFreezePO;
import com.blockchain.wallet.api.service.WalletAccountFreezeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *    发送提现申请到mq
 * @author shujun
 * @date 2018/7/4
 */
@Component
@Slf4j
public class WalletWithdrawApply implements RabbitTemplate.ConfirmCallback{

    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    private static final double blockingCoefficient = 0.5;//阻塞系数
    private static final int poolSize = (int)(CPU_NUM / (1 - blockingCoefficient)); //求得线程数大小

    private final ThreadPoolExecutor mProcessThreadPool = new ThreadPoolExecutor(poolSize, 2*poolSize, 500L,
                                                                                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory("Wallet_Withdraw_apply"));
    private final RabbitTemplate rabbitTemplate;

    private final RabbitmqConfig config;

    @Autowired
    private WalletAccountFreezeService service;

    @Autowired
    public WalletWithdrawApply(@Qualifier("rabbitTemplate")RabbitTemplate rabbitTemplate , RabbitmqConfig config){
        this.rabbitTemplate = rabbitTemplate;
        this.config = config;
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendMessage(WalletApiWithdrawRequestDTO dto ){
        WalletMqSendMessageTask task = new WalletMqSendMessageTask(rabbitTemplate,config,dto);
        mProcessThreadPool.submit(task);
    }



    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 如果没有成功，定时任务后台处理定时转发
        if(ack == false){
            log.error("frzeeNo : {} Message delivery fail. cause: {} " , correlationData.getId() , cause );
        }else{
            //更新数据库
            WalletAccountFreezePO record = new WalletAccountFreezePO();
            record.setFreezeNo(correlationData.getId());
            record.setMsgState(BizTypeConstants.MSG_DELIVER_STATE_2.getCode());
            service.updateByfreezeNo(record);
        }
    }
}
