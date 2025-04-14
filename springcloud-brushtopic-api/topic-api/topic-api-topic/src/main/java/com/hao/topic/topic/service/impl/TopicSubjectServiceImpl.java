package com.hao.topic.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.topic.TopicSubjectDto;
import com.hao.topic.model.dto.topic.TopicSubjectListDto;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.topic.mapper.TopicSubjectMapper;
import com.hao.topic.topic.service.TopicSubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/14 10:39
 */
@Service
@AllArgsConstructor
@Slf4j
public class TopicSubjectServiceImpl implements TopicSubjectService {
    private final TopicSubjectMapper topicSubjectMapper;


    /**
     * 查询专题列表
     *
     * @param topicSubjectListDto
     * @return
     */
    public Map<String, Object> subjectList(TopicSubjectListDto topicSubjectListDto) {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页条件
        LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断是否为Hao
        if (currentId != 1L) {
            // 根据当前登录用户查询
            topicSubjectLambdaQueryWrapper.like(TopicSubject::getCreateBy, username);
        } else {
            // 是超管
            // 判断是否查询创建人
            if (!StringUtils.isEmpty(topicSubjectListDto.getCreateBy())) {
                topicSubjectLambdaQueryWrapper.like(TopicSubject::getCreateBy, topicSubjectListDto.getCreateBy());
            }
        }
        // 判断专题名称
        if (!StringUtils.isEmpty(topicSubjectListDto.getSubjectName())) {
            topicSubjectLambdaQueryWrapper.like(TopicSubject::getSubjectName, topicSubjectListDto.getSubjectName());
        }
        // 判断创建时间
        if (!StringUtils.isEmpty(topicSubjectListDto.getBeginCreateTime()) && !StringUtils.isEmpty(topicSubjectListDto.getEndCreateTime())) {
            topicSubjectLambdaQueryWrapper.between(TopicSubject::getCreateTime, topicSubjectListDto.getBeginCreateTime(), topicSubjectListDto.getEndCreateTime());
        }
        // 判断修改时间
        if (!StringUtils.isEmpty(topicSubjectListDto.getBeginUpdateTime()) && !StringUtils.isEmpty(topicSubjectListDto.getEndUpdateTime())) {
            topicSubjectLambdaQueryWrapper.between(TopicSubject::getUpdateTime, topicSubjectListDto.getBeginUpdateTime(), topicSubjectListDto.getEndUpdateTime());
        }
        topicSubjectLambdaQueryWrapper.orderByDesc(TopicSubject::getCreateTime);
        // 开始分页查询
        if (topicSubjectListDto.getPageNum() == null || topicSubjectListDto.getPageSize() == null) {
            List<TopicSubject> categories = topicSubjectMapper.selectList(topicSubjectLambdaQueryWrapper);
            return Map.of("total", categories.size(), "rows", categories);
        } else {
            // 设置分页参数
            Page<TopicSubject> topicSubjectPage = new Page<>(topicSubjectListDto.getPageNum(), topicSubjectListDto.getPageSize());
            // 开始查询
            Page<TopicSubject> topicSubjectPageResult = topicSubjectMapper.selectPage(topicSubjectPage, topicSubjectLambdaQueryWrapper);
            return Map.of("total", topicSubjectPageResult.getTotal(), "rows", topicSubjectPageResult.getRecords());
        }
    }

    /**
     * 新增专题
     *
     * @param topicCategoryDto
     */
    public void add(TopicSubjectDto topicCategoryDto) {
        // 查询
        TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicCategoryDto.getId());
        if (topicSubjectDb != null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_NAME_EXIST);
        }
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        TopicSubject topicSubject = new TopicSubject();
        BeanUtils.copyProperties(topicCategoryDto, topicSubject);
        topicSubject.setCreateBy(username);

        // TODO 异步发送消息给AI审核
        topicSubjectMapper.insert(topicSubject);
    }
}
