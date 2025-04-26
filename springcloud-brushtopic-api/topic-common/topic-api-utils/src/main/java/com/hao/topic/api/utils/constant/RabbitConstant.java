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
    /**
     * 分类审核
     */
    // 分类交换机
    public static final String CATEGORY_AUDIT_EXCHANGE = "category.audit.exchange";
    // 分类审核队列
    public static final String CATEGORY_AUDIT_QUEUE_NAME = "category.audit.queue";
    // 分类审核路由键
    public static final String CATEGORY_AUDIT_ROUTING_KEY_NAME = "category.audit.routing.key";
    /**
     * 标签审核
     */
    public static final String LABEL_AUDIT_EXCHANGE = "label.audit.exchange";
    public static final String LABEL_AUDIT_QUEUE_NAME = "label.audit.queue";
    public static final String LABEL_AUDIT_ROUTING_KEY_NAME = "label.audit.routing.key";

}
