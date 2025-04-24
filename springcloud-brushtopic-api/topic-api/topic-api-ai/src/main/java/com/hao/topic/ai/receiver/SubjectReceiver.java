package com.hao.topic.ai.receiver;

import com.alibaba.fastjson.JSON;
import com.hao.topic.ai.service.ModelService;
import com.hao.topic.api.utils.constant.RabbitConstant;
import com.hao.topic.model.dto.topic.TopicAuditSubject;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Description: 接收专题审核消息
 * Author: Hao
 * Date: 2025/4/24 23:00
 */
@Component
@Slf4j
public class SubjectReceiver {

    @Autowired
    private ModelService modelService;


    /**
     * 接收生产者发送审核的信息
     *
     * @param topicAuditSubjectJson 要审核的专题
     * @param message               接收到的完整消息对象
     * @param channel               跟mq通信的方法
     */
    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = RabbitConstant.SUBJECT_AUDIT_QUEUE_NAME),// 存储消息队列
                    exchange = @Exchange(value = RabbitConstant.SUBJECT_AUDIT_EXCHANGE),// 转发消息的交换机
                    key = {RabbitConstant.SUBJECT_AUDIT_ROUTING_KEY_NAME}))// 路由key
    public void auditSubject(String topicAuditSubjectJson, Message message, Channel channel) throws IOException {
        log.info("接收到专题审核消息{}",topicAuditSubjectJson);
        // // 转换json
        // TopicAuditSubject topicAuditSubject = JSON.parseObject(topicAuditSubjectJson, TopicAuditSubject.class);
        // // 开始审核
        // modelService.auditSubject(topicAuditSubject);
        // // 手动确认该消息 通过唯一标识已被消费
        // // 参数1：标号用于消息确认 记载 消息重试等
        // channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

