package com.blockchain.wallet.api.mq;

import com.blockchain.wallet.api.business.UsdtWithdrawBusiness;
import com.blockchain.wallet.api.dto.WalletApiWithdrawRequestDTO;
import com.blockchain.wallet.api.po.walletPlatformOut;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Slf4j
public class UsdtWithdrawNotify  implements ChannelAwareMessageListener {

    private final Jackson2JsonMessageConverter converter;
    private final UsdtWithdrawBusiness usdtWithdrawBusiness;


    public UsdtWithdrawNotify(Jackson2JsonMessageConverter converter, UsdtWithdrawBusiness usdtWithdrawBusiness) {
        this.converter = converter;
        this.usdtWithdrawBusiness = usdtWithdrawBusiness;
    }

    /**
     * 消费成功后手工确认
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        walletPlatformOut wpo;
        int res = 0;
        int limit = 0;
        Object obj = converter.fromMessage(message);
        if(obj instanceof WalletApiWithdrawRequestDTO){
            WalletApiWithdrawRequestDTO dto = (WalletApiWithdrawRequestDTO)obj;
            String orderNo = dto.getOrderNo();
            if(orderNo.equals("") || orderNo == null){
                log.error("*******订单流水号："+orderNo);
            }
            String receiveAddress = dto.getToAccAddress();
            String amount = String.format("%.8f", (dto.getValue()/new Double(Math.pow(10,8))));
            //String amount = String.valueOf(dto.getValue());

            wpo = usdtWithdrawBusiness.withdrawLimit(orderNo);
            if(wpo != null){
                Object o = wpo.getWithdrawLimit();
                if(o != null){
                    limit = (int)o;
                }
            }
            if(limit > 3){
                // 丢弃消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag() , false);
            }
            //withdraw
            res = usdtWithdrawBusiness.withdraw(receiveAddress, amount, orderNo,limit);

            log.error(dto.toString());
        }
        if(res > 0){
            // 消息确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }else{
            //处理失败消息重新投递
            channel.basicNack(message.getMessageProperties().getDeliveryTag() , false , true);
        }
    }
}
