package com.hao.topic.system.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.entity.system.SysFeedback;
import com.hao.topic.system.service.SysFeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 反馈管理
 * Author: Hao
 * Date: 2025/5/2 22:53
 */
@RestController
@RequestMapping("/system/feedback")
@AllArgsConstructor
public class SysFeedbackController {

    private SysFeedbackService sysFeedbackService;

    /**
     * 发送反馈
     */
    @PostMapping("/send")
    public Result sendFeedback(@RequestBody SysFeedback sysFeedback) {
        sysFeedbackService.saveFeedback(sysFeedback);
        return Result.success();
    }
}
