package com.blockchain.wallet.api.mq;

import com.blockchain.wallet.api.business.UsdtWithdrawBusiness;
import com.blockchain.wallet.api.business.WalletAccountBusiness;
import com.blockchain.wallet.api.service.walletBussinessMqMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

/**
 * @author shujun
 * @date 2018/7/4
 */
@Configuration
//@EnableRabbit
@Slf4j
public class RabbitConfiguration {

    private final RabbitProperties properties;
    private final RabbitmqConfig config;
    private final ConnectionFactory connectionFactory;



    public RabbitConfiguration(RabbitProperties properties , RabbitmqConfig config , ConnectionFactory connectionFactory) {
        this.properties = properties;
        this.config = config ;
        this.connectionFactory = connectionFactory;
    }

    private boolean determineMandatoryFlag() {
        Boolean mandatory = this.properties.getTemplate().getMandatory();
        return (mandatory != null ? mandatory : this.properties.isPublisherReturns());
    }

    @Bean
    public Jackson2JsonMessageConverter configJackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        PropertyMapper map = PropertyMapper.get();
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(determineMandatoryFlag());
        RabbitProperties.Template properties = this.properties.getTemplate();
        map.from(properties::getReceiveTimeout).whenNonNull().as(Duration::toMillis)
                .to(template::setReceiveTimeout);
        map.from(properties::getReplyTimeout).whenNonNull().as(Duration::toMillis)
                .to(template::setReplyTimeout);
        map.from(properties::getExchange).to(template::setExchange);
        map.from(properties::getRoutingKey).to(template::setRoutingKey);
        // json 格式化传输对象
        template.setMessageConverter(configJackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public RabbitTemplate rabbitTemplateBlock() {
        PropertyMapper map = PropertyMapper.get();
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(determineMandatoryFlag());
        RabbitProperties.Template properties = this.properties.getTemplate();
        map.from(properties::getReceiveTimeout).whenNonNull().as(Duration::toMillis)
                .to(template::setReceiveTimeout);
        map.from(properties::getReplyTimeout).whenNonNull().as(Duration::toMillis)
                .to(template::setReplyTimeout);
        map.from(properties::getExchange).to(template::setExchange);
        map.from(properties::getRoutingKey).to(template::setRoutingKey);
        // json 格式化传输对象
        template.setMessageConverter(configJackson2JsonMessageConverter());
        return template;
    }


//    @Bean
//    public Queue queueTest() {
//        return new Queue("spring-boot-queue", true); //队列持久
//    }

    //充值exchange
    @Bean
    public DirectExchange rechargeExchange() {
        return new DirectExchange(config.getRecharge().getExchange(),true , false);
    }
    //充值队列
    @Bean
    public Queue rechargeQueue() {
        return QueueBuilder.durable(config.getRecharge().getQueue()).build();
    }

    /**
     *  充值绑定
     * @return
     */
    @Bean
    public Binding rechargeBinding() {
        return BindingBuilder.bind( this.rechargeQueue()).to(rechargeExchange()).with(config.getRecharge().getRouteKey());
    }


    // 提现申请exchange
    @Bean
    public DirectExchange withdrawExchange() {
        return new DirectExchange(config.getWithdraw().getExchange(),true , false);
    }
    //提现申请
    @Bean
    public Queue withdrawQueue() {
        return QueueBuilder.durable(config.getWithdraw().getQueue()).build();
    }

    /**
     *  提现申请
     * @return
     */
    @Bean
    public Binding withdrawBinding() {
        return BindingBuilder.bind( this.withdrawQueue()).to(withdrawExchange()).with(config.getWithdraw().getRouteKey());
    }

    /**
      * 提现
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer(Jackson2JsonMessageConverter converter  , UsdtWithdrawBusiness usdtWithdrawBusiness) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(withdrawQueue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        // 需要手动确认
        container.setAfterReceivePostProcessors();
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        UsdtWithdrawNotify notify = new UsdtWithdrawNotify(converter, usdtWithdrawBusiness);
        container.setMessageListener(notify);
        return container;
    }


    /**
     *  提现通知消息定义
     */
    // 提现确认exchange
    @Bean
    public DirectExchange withdrawNotifyExchange() {
        return new DirectExchange(config.getWithdrawNotify().getExchange(),true , false);
    }
    //提现申请
    @Bean
    public Queue withdrawNotifyQueue() {
        return QueueBuilder.durable(config.getWithdrawNotify().getQueue()).build();
    }

    /**
     *  提现申请
     * @return
     */
    @Bean
    public Binding withdrawNotifyBinding() {
        return BindingBuilder.bind( this.withdrawNotifyQueue()).to(withdrawNotifyExchange()).with(config.getWithdrawNotify().getRouteKey());
    }


    /**
     *  提现确认消息处理
     * @param converter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer withdrawNotifyMessageContainer(Jackson2JsonMessageConverter converter ,
             WalletAccountBusiness bussiness , walletBussinessMqMsgService mqMsgService) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(withdrawNotifyQueue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        // 需要手动确认
        container.setAfterReceivePostProcessors();
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置确认模式手工确认
        ConsumerWalletWithdrawNotify notify = new ConsumerWalletWithdrawNotify(converter , bussiness , mqMsgService);
        container.setMessageListener(notify);
        return container;
    }


    /**
     *  通知扫块消息定义
     */
    // 提现确认exchange
    @Bean
    public DirectExchange rechargeNotifyExchange() {
        return new DirectExchange(config.getRechargeNotify().getExchange(),true , false);
    }
    //提现申请
    @Bean
    public Queue rechargeNotifyQueue() {
        return QueueBuilder.durable(config.getRechargeNotify().getQueue()).build();
    }

    /**
     *  提现申请
     * @return
     */
    @Bean
    public Binding rechargeNotifyBinding() {
        return BindingBuilder.bind( this.rechargeNotifyQueue()).to(rechargeNotifyExchange()).with(config.getRechargeNotify().getRouteKey());
    }


    /**
     *  充值消息消费，
     *  接收扫块处理的消息
     * @param converter
     * @param bussiness
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer rechargeNotifyMessageContainer(Jackson2JsonMessageConverter converter ,
                                                                         WalletAccountBusiness bussiness, walletBussinessMqMsgService mqMsgService) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(blockScanQueue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        // 需要手动确认
        container.setAfterReceivePostProcessors();
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置确认模式手工确认
        ConsumerWalletRechargeNotify notify = new ConsumerWalletRechargeNotify(converter , bussiness,mqMsgService);
        container.setMessageListener(notify);
        return container;
    }


    /**
     *  推送扫块消息
     */
    @Bean
    public DirectExchange blockScanExchange() {
        return new DirectExchange(config.getBlockScan().getExchange(),true , false);
    }

    @Bean
    public Queue blockScanQueue() {
        return QueueBuilder.durable(config.getBlockScan().getQueue()).build();
    }

    /**
     *  推送扫块消息
     * @return
     */
    @Bean
    public Binding blockScanBinding() {
        return BindingBuilder.bind( this.blockScanQueue()).to(blockScanExchange()).with(config.getBlockScan().getRouteKey());
    }

}
