package com.hao.topic.topic.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.topic.service.TopicCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<Map<String, Object>> list(TopicCategoryDto topicCategoryDto) {
        Map<String, Object> map = topicCategoryService.categoryList(topicCategoryDto);
        return Result.success(map);
    }

}
