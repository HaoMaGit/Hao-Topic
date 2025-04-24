package com.hao.topic.api.utils.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: mq发送消息的服务方法
 * Author: Hao
 * Date: 2025/4/24 22:40
 */
@Service
public class RabbitService {
    // mq的客户端
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息的方法
     *
     * @param exchange   交换机(生产者将消息发送到这里并将消息发送到队列中存储供消费)
     * @param routingKey 路由键(生产者将消息发送到交换器时指定的一个字符串标识用于发送到指定的交换机)
     * @param message    消息(内容)
     * @return
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        // 调用发送消息的方法
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }

}
