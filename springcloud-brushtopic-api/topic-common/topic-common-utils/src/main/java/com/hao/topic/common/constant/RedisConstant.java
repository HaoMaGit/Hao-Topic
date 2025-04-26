package com.hao.topic.common.constant;

/**
 * Description: rediskey
 * Author: Hao
 * Date: 2025/4/1 13:48
 */

public class RedisConstant {
    // 登录key
    public static final String USER_LOGIN_KEY_PREFIX = "user:login:";
    // 生成ai答案中的key
    public static final String GENERATE_ANSWER_KEY_PREFIX = "generate:answer:";
    // 题目审核的key
    public static final String TOPIC_AUDIT_KEY_PREFIX = "topic:audit:";
    // 题目分类审核的key
    public static final String CATEGORY_AUDIT_KEY_PREFIX = "category:audit:";
    // 题目标签审核的key
    public static final String LABEL_AUDIT_KEY_PREFIX = "label:audit:";
    // 题目专题审核的key
    public static final String SUBJECT_AUDIT_KEY_PREFIX = "subject:audit:";
    // 审核过期时间
    public static final Long AUDIT_EXPIRE_TIME = 5L;
}
