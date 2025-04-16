package com.hao.topic.topic.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.dto.topic.TopicDto;
import com.hao.topic.model.dto.topic.TopicListDto;
import com.hao.topic.topic.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/16 20:08
 */
@RestController
@RequestMapping("/topic/topic")
@AllArgsConstructor
public class TopicController {
    private final TopicService topicService;

    /**
     * 获取题目列表
     *
     * @param topicListDto
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result<Map<String, Object>> list(TopicListDto topicListDto) {
        Map<String, Object> map = topicService.topicList(topicListDto);
        return Result.success(map);
    }

    /**
     * 添加题目分类
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result add(@RequestBody TopicDto topicDto) {
        topicService.add(topicDto);
        return Result.success();
    }


}
