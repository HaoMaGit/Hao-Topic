package com.hao.topic.topic.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.topic.service.TopicCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Description: 题目分类控制器
 * Author: Hao
 * Date: 2025/4/13 18:22
 */
@RestController
@RequestMapping("/topic/category")
@AllArgsConstructor
public class TopicCategoryController {

    private final TopicCategoryService topicCategoryService;

    /**
     * 获取题目分类列表
     *
     * @param topicCategoryDto
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result<Map<String, Object>> list(TopicCategoryListDto topicCategoryDto) {
        Map<String, Object> map = topicCategoryService.categoryList(topicCategoryDto);
        return Result.success(map);
    }

    /**
     * 添加题目分类
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result add(@RequestBody TopicCategoryDto topicCategoryDto) {
        topicCategoryService.add(topicCategoryDto);
        return Result.success();
    }

    /**
     * 修改题目分类
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result update(@RequestBody TopicCategoryDto topicCategoryDto) {
        topicCategoryService.update(topicCategoryDto);
        return Result.success();
    }

}
