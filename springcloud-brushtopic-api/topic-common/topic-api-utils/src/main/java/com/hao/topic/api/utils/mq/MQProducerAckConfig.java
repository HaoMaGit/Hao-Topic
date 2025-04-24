package com.hao.topic.api.utils.mq;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 消息确认机制
 * Author: Hao
 * Date: 2025/4/24 22:40
 */
@Component
public class MQProducerAckConfig implements RabbitTemplate.ReturnsCallback, RabbitTemplate.ConfirmCallback {
    //  我们发送消息使用的是 private RabbitTemplate rabbitTemplate; 对象
    //  如果不做设置的话 当前的rabbitTemplate 与当前的配置类没有任何关系！
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 设置表示修饰一个非静态的void方法，在服务器加载Servlet的时候运行。并且只执行一次！
    // 确保回调会执行
    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(this);
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 表示消息是否正确发送到了交换机上
     *
     * @param correlationData 消息的载体
     * @param ack             判断是否发送到交换机上
     * @param cause           原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消息发送成功！");
        } else {
            System.out.println("消息发送失败！" + cause);
        }
    }

    /**
     * 消息如果没有正确发送到队列中，则会走这个方法！如果消息被正常处理，则这个方法不会走！
     *
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        System.out.println("消息主体: " + returnedMessage.getMessage());
        System.out.println("应答码: " + returnedMessage.getReplyCode());
        System.out.println("描述：" + returnedMessage);
        System.out.println("消息使用的交换器 exchange : " + returnedMessage.getExchange());
        System.out.println("消息使用的路由键 routing : " + returnedMessage.getRoutingKey());
    }
}
