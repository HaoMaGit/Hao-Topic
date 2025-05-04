package com.hao.topic.system.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.system.SysNoticeDto;
import com.hao.topic.model.vo.system.SysNoticeVo;
import com.hao.topic.system.service.SysNoticeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: 通知控制层
 * Author: Hao
 * Date: 2025/5/3 16:28
 */
@RestController
@RequestMapping("/system/notice")
@AllArgsConstructor
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    /**
     * 记录通知
     */
    @PostMapping("/record")
    public Result recordNotice(@RequestBody SysNoticeDto sysNoticeDto) {
        sysNoticeService.recordNotice(sysNoticeDto);
        return Result.success();
    }

    /**
     * 查询反馈通知列表
     */
    @GetMapping("/list")
    public Result list() {
        List<SysNoticeVo> sysNoticeVos = sysNoticeService.list();
        return Result.success(sysNoticeVos);
    }

    /**
     * 查询反馈通知是否有
     */
    @GetMapping("/has")
    public Result has() {
        Boolean isHas = sysNoticeService.has();
        return Result.success(isHas);
    }
}
