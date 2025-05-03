package com.hao.topic.system.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.entity.system.SysFeedback;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.vo.system.SysFeedbackUserVo;
import com.hao.topic.model.vo.system.SysFeedbackVo;
import com.hao.topic.model.vo.system.SysMenuListVo;
import com.hao.topic.system.service.SysFeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询反馈列表
     *
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin')")
    public Result<Map<String, Object>> list(SysFeedback sysFeedback) {
        Map<String, Object> map = sysFeedbackService.list(sysFeedback);
        return Result.success(map);
    }

    /**
     * h5查询反馈列表
     */
    @GetMapping("/feedback")
    public Result<List<SysFeedbackUserVo>> feedback() {
        List<SysFeedbackUserVo> list = sysFeedbackService.feedback();
        return Result.success(list);
    }
}
