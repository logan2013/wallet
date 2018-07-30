package com.blockchain.wallet.api.mq;

import com.blockchain.wallet.api.business.WalletAccountBusiness;
import com.blockchain.wallet.api.dto.WalletUsdtMsgDTO;
import com.blockchain.wallet.api.dto.WalletWithdrawMsgDTO;
import com.blockchain.wallet.api.exception.BizException;
import com.blockchain.wallet.api.internal.util.JSON;
import com.blockchain.wallet.api.po.walletBussinessMqMsgPOWithBLOBs;
import com.blockchain.wallet.api.service.walletBussinessMqMsgService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 *
 * @author shujun
 * @date 2018/7/5
 */
@Slf4j
public class ConsumerWalletWithdrawNotify implements ChannelAwareMessageListener {

    private final Jackson2JsonMessageConverter converter;
    private final WalletAccountBusiness bussiness;
    private final walletBussinessMqMsgService mqMsgService;


    public ConsumerWalletWithdrawNotify(Jackson2JsonMessageConverter converter, WalletAccountBusiness bussiness, walletBussinessMqMsgService mqMsgService) {
        this.converter = converter;
        this.bussiness = bussiness;
        this.mqMsgService = mqMsgService;
    }

    private walletBussinessMqMsgPOWithBLOBs generate(WalletWithdrawMsgDTO dto){
        walletBussinessMqMsgPOWithBLOBs po = new walletBussinessMqMsgPOWithBLOBs();
        po.setMsgContent(JSON.toJsonString(dto));
        po.setBussinessType("WithdrawNotify");
        po.setMsgClazzType(WalletWithdrawMsgDTO.class.getName());
        po.setCreateTime(System.currentTimeMillis());
        po.setState("0");
        return po;
    }

    /**
     *  消费成功后手工确认
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        log.debug("consumer message start...");
        Object obj = converter.fromMessage(message);
        WalletWithdrawMsgDTO dto = (WalletWithdrawMsgDTO)obj;
        try{
            bussiness.withdrawNotifyBill(dto);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            if(e instanceof BizException){
                // 业务校验不通过直接丢弃消息，并把错误消息保存入库
                walletBussinessMqMsgPOWithBLOBs p = this.generate(dto);
                p.setExpCode(((BizException) e).getCode());
                p.setExpDesc(((BizException) e).getMessage());
                mqMsgService.insert(p);
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            }else{
                // db 异常可以尝试 重试操作3次来处理。
                MessageCounter<WalletWithdrawMsgDTO> counter = MessageCounter.getInstance();
                if(counter.add(dto)){
                    Thread.sleep(1000L);
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false ,true);
                }else{
                    walletBussinessMqMsgPOWithBLOBs p = this.generate(dto);
                    p.setExpDesc(e.getMessage());
                    mqMsgService.insert(p);
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                }
            }
        }
    }
}
