package com.hao.topic.ai.constant;

import org.apache.commons.collections4.Put;

/**
 * Description: ai服务相关常量
 * Author: Hao
 * Date: 2025/4/20 14:47
 */
public class AiConstant {
    // 系统模式'AI从系统题库中提取题目逐题提问并根据系统答案校验正确性'
    public final static String SYSTEM_MODEL = "system";
    // 模型模式'完全使用AI生成的题目覆盖更开放或创新题型'
    public final static String AI_MODEL = "model";
    // 混合模式'AI随机混合系统题库和AI自定义题目增加多样性'
    public final static String MIX_MODEL = "mix";
    public static final String DAY_HISTORY = "当天";
}
