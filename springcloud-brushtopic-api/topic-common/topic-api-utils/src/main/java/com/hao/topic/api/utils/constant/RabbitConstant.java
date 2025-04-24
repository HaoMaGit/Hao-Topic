package com.hao.topic.api.utils.constant;

/**
 * Description: mq常量
 * Author: Hao
 * Date: 2025/4/24 22:52
 */
public class RabbitConstant {
    /**
     * 专题审核
     */
    // 专题交换机
    public static final String SUBJECT_AUDIT_EXCHANGE = "subject.audit.exchange"; // 交换机
    // 专题审核队列
    public static final String SUBJECT_AUDIT_QUEUE_NAME = "subject.audit.queue";
    // 专题审核路由键
    public static final String SUBJECT_AUDIT_ROUTING_KEY_NAME = "subject.audit.routing.key";
    // // 审核成功的路由键发送到指定的交换机
    // public static final String SUBJECT_AUDIT_ROUTING_KEY = "subject.audit.success.routing.key";
    // // 审核失败的路由键发送到指定的交换机
    // public static final String SUBJECT_AUDIT_ROUTING_KEY_ERROR = "subject.audit.error.routing.key";
    // // 存储成功的队列消息等待消费者消费
    // public static final String SUBJECT_AUDIT_SUCCESS_QUEUE_NAME = "subject.audit.success.queue";
    // // 存储失败的队列消息等待消费者消费
    // public static final String SUBJECT_AUDIT_ERROR_QUEUE_NAME = "subject.audit.error.queue";
}
