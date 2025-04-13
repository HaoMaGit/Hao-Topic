package com.hao.topic.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.topic.mapper.TopicCategoryMapper;
import com.hao.topic.topic.service.TopicCategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/13 18:26
 */
@Service
@AllArgsConstructor
@Slf4j
public class TopicCategoryServiceImpl implements TopicCategoryService {
    private final TopicCategoryMapper topicCategoryMapper;

    /**
     * 分页查询分类列表
     *
     * @param topicCategoryDto
     * @return
     */
    public Map<String, Object> categoryList(TopicCategoryDto topicCategoryDto) {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页参数
        Page<TopicCategory> topicCategoryPage = new Page<>(topicCategoryDto.getPageNum(), topicCategoryDto.getPageSize());
        // 设置分页条件
        LambdaQueryWrapper<TopicCategory> topicCategoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断是否为Hao
        if (currentId != 1L) {
            // 根据当前登录用户查询
            topicCategoryLambdaQueryWrapper.like(TopicCategory::getCategoryName, topicCategoryDto.getCategoryName());
        }
        topicCategoryLambdaQueryWrapper.orderByDesc(TopicCategory::getCreateTime);
        // 开始查询
        Page<TopicCategory> topicCategoryList = topicCategoryMapper.selectPage(topicCategoryPage, topicCategoryLambdaQueryWrapper);
        return Map.of("total", topicCategoryList.getTotal(), "rows", topicCategoryList.getRecords());
    }
}
