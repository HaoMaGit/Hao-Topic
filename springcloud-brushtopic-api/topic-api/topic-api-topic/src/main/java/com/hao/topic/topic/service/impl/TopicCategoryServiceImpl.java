package com.hao.topic.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.entity.topic.TopicCategorySubject;
import com.hao.topic.topic.mapper.TopicCategoryMapper;
import com.hao.topic.topic.mapper.TopicCategorySubjectMapper;
import com.hao.topic.topic.service.TopicCategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final TopicCategorySubjectMapper topicCategorySubjectMapper;

    /**
     * 分页查询分类列表
     *
     * @param topicCategoryDto
     * @return
     */
    public Map<String, Object> categoryList(TopicCategoryListDto topicCategoryDto) {
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
        // 判断创建时间
        if (!StringUtils.isEmpty(topicCategoryDto.getBeginCreateTime()) && !StringUtils.isEmpty(topicCategoryDto.getEndCreateTime())) {
            topicCategoryLambdaQueryWrapper.between(TopicCategory::getCreateTime, topicCategoryDto.getBeginCreateTime(), topicCategoryDto.getEndCreateTime());
        }
        // 判断修改时间
        if (!StringUtils.isEmpty(topicCategoryDto.getBeginUpdateTime()) && !StringUtils.isEmpty(topicCategoryDto.getEndUpdateTime())) {
            topicCategoryLambdaQueryWrapper.between(TopicCategory::getUpdateTime, topicCategoryDto.getBeginUpdateTime(), topicCategoryDto.getEndUpdateTime());
        }
        topicCategoryLambdaQueryWrapper.orderByDesc(TopicCategory::getCreateTime);
        // 开始查询
        Page<TopicCategory> topicCategoryList = topicCategoryMapper.selectPage(topicCategoryPage, topicCategoryLambdaQueryWrapper);
        return Map.of("total", topicCategoryList.getTotal(), "rows", topicCategoryList.getRecords());
    }

    /**
     * 添加题目分类
     *
     * @param topicCategoryDto
     */
    public void add(TopicCategoryDto topicCategoryDto) {
        // 校验分类名称
        if (StringUtils.isEmpty(topicCategoryDto.getCategoryName())) {
            throw new TopicException(ResultCodeEnum.CATEGORY_NAME_IS_NULL);
        }
        // 查询
        TopicCategory topicCategoryDb = topicCategoryMapper.selectById(topicCategoryDto.getId());
        if (topicCategoryDb != null) {
            throw new TopicException(ResultCodeEnum.CATEGORY_NAME_EXIST);
        }
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        TopicCategory topicCategory = new TopicCategory();
        topicCategory.setCategoryName(topicCategoryDto.getCategoryName());
        topicCategory.setCreateBy(username);
        topicCategoryMapper.insert(topicCategory);
    }

    /**
     * 修改题目分类
     *
     * @param topicCategoryDto
     */
    public void update(TopicCategoryDto topicCategoryDto) {
        // 校验分类名称
        if (StringUtils.isEmpty(topicCategoryDto.getCategoryName())) {
            throw new TopicException(ResultCodeEnum.CATEGORY_NAME_IS_NULL);
        }
        // 查询
        TopicCategory topicCategory = topicCategoryMapper.selectById(topicCategoryDto.getId());
        if (topicCategory == null) {
            throw new TopicException(ResultCodeEnum.CATEGORY_UPDATE_IS_NULL);
        }
        // 开始修改
        topicCategory.setCategoryName(topicCategoryDto.getCategoryName());
        topicCategoryMapper.updateById(topicCategory);
    }

    /**
     * 删除题目分类
     *
     * @param ids
     */
    public void delete(Long[] ids) {
        // 校验
        if (ids == null) {
            throw new TopicException(ResultCodeEnum.CATEGORY_DELETE_IS_NULL);
        }
        LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        for (Long id : ids) {
            // 查询分类与专题关系表
            topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getCategoryId, id);
            TopicCategorySubject topicCategorySubject = topicCategorySubjectMapper.selectOne(topicCategorySubjectLambdaQueryWrapper);
            if (topicCategorySubject != null) {
                throw new TopicException(ResultCodeEnum.CATEGORY_DELETE_TOPIC_ERROR);
            }
            // 删除
            topicCategoryMapper.deleteById(id);
        }
    }
}
