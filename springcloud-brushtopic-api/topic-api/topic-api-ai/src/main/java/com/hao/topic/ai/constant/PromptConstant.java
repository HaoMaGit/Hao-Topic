package com.hao.topic.ai.constant;

/**
 * Description: 提示词
 * Author: Hao
 * Date: 2025/4/20 15:25
 */
public class PromptConstant {
    // 个人介绍
    public static final String INTRODUCTION = "你是一个面试题AI助手，你的名字是HaoAi，" +
            "你有10年面试官经验掌握市面上最流行的面试题，" +
            "你的职责是出面试题，并对我的回答进行评估，" +
            "准确率(优秀（90%-100%）,良好（70%-89%）,一般（50%-69%）,较差（0%-49%）)，表达清晰度，完整性状态" +
            "然后如果准确率低于良好就帮我改进回答,准确率高于良好就返回评估信息就可以了";
    // 评估
    public static final String EVALUATE = "请根据我的回答评估我的准确率，准确率(优秀（90%-100%）,良好（70%-89%）,一般（50%-69%）,较差（0%-49%）)，表达清晰度，完整性状态 然后如果准确率低于良好就帮我改进回答,准确率高于良好就返回评估信息就可以了";

    // 刷题次数算法
    public static final int START_CONTINUE_MEMORY_ID = 3;
    public static final int CONTINUE_INTERVAL = 2;

    // 审核专题提示词
    public static final String AUDIT_SUBJECT = "请根据以下规则对题目专题进行审核：\\n\" +\n" +
            "                \"1. 如果专题内容违反法律法规、社会公序良俗或与分类无关，请返回 {\\\"result\\\": false, \\\"reason\\\": \\\"违规原因描述\\\"}。\\n\" +\n" +
            "                \"2. 如果专题内容合法且与分类相关，请返回 {\\\"result\\\": true, \\\"reason\\\": \\\"审核通过\\\"}。\\n\" +\n" +
            "                \"3. 要求：一定不要返回多余的内容";
    // 审核分类提示词
    public static final String AUDIT_CATEGORY = "请根据规则审核分类名称：\n" +
            "1. 若名称含违法/虚假/逻辑错误内容，返回纯字符串：{\"result\":false,\"reason\":\"违规原因\"}\n" +
            "2. 若名称合法合规，返回纯字符串：{\"result\":true,\"reason\":\"审核通过\"}\n" +
            "3. 非常严格的按上述格式返回，不包含任何额外说明，以及不需要转换json";

}
