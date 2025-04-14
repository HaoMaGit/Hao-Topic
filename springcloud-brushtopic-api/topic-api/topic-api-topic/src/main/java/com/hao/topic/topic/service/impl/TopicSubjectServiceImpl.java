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
import com.hao.topic.model.entity.topic.TopicCategorySubject;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.model.entity.topic.TopicSubjectTopic;
import com.hao.topic.topic.enums.StatusEnums;
import com.hao.topic.topic.mapper.TopicCategoryMapper;
import com.hao.topic.topic.mapper.TopicCategorySubjectMapper;
import com.hao.topic.topic.mapper.TopicSubjectMapper;
import com.hao.topic.topic.mapper.TopicSubjectTopicMapper;
import com.hao.topic.topic.service.TopicSubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TopicSubjectTopicMapper topicSubjectTopicMapper;
    private final TopicCategorySubjectMapper topicCategorySubjectMapper;
    private final TopicCategoryMapper topicCategoryMapper;


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
        topicSubjectLambdaQueryWrapper.orderByDesc(TopicSubject::getCreateTime);
        // 开始分页查询
        if (topicSubjectListDto.getPageNum() == null || topicSubjectListDto.getPageSize() == null) {
            List<TopicSubject> topicSubjects = topicSubjectMapper.selectList(topicSubjectLambdaQueryWrapper);
            // 封装返回结果
            for (TopicSubject topicSubject : topicSubjects) {
                // 根据专题id查询分类专题表
                LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getSubjectId, topicSubject.getId());
                TopicCategorySubject topicCategorySubject = topicCategorySubjectMapper.selectOne(topicCategorySubjectLambdaQueryWrapper);
                if (topicCategorySubject != null) {
                    TopicCategory topicCategory = topicCategoryMapper.selectById(topicCategorySubject.getCategoryId());
                    if (topicCategory != null) {
                        topicSubject.setCategoryName(topicCategory.getCategoryName());
                    }
                }
            }
            // 校验参数
            if (topicSubjectListDto.getCategoryName() != null) {
                // 模糊匹配过滤分类名称
                topicSubjects.removeIf(topicSubject -> !topicSubject.getCategoryName().contains(topicSubjectListDto.getCategoryName()));
            }
            return Map.of("total", topicSubjects.size(), "rows", topicSubjects);
        } else {
            // 设置分页参数
            Page<TopicSubject> topicSubjectPage = new Page<>(topicSubjectListDto.getPageNum(), topicSubjectListDto.getPageSize());
            // 开始查询
            Page<TopicSubject> topicSubjectPageResult = topicSubjectMapper.selectPage(topicSubjectPage, topicSubjectLambdaQueryWrapper);
            topicSubjectPageResult.getRecords().forEach(topicSubject -> {
                // 根据专题id查询分类专题表
                LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getSubjectId, topicSubject.getId());
                TopicCategorySubject topicCategorySubject = topicCategorySubjectMapper.selectOne(topicCategorySubjectLambdaQueryWrapper);
                if (topicCategorySubject != null) {
                    TopicCategory topicCategory = topicCategoryMapper.selectById(topicCategorySubject.getCategoryId());
                    if (topicCategory != null) {
                        topicSubject.setCategoryName(topicCategory.getCategoryName());
                    }
                }
            });
            // 校验参数
            if (topicSubjectListDto.getCategoryName() != null) {
                // 模糊匹配过滤分类名称
                topicSubjectPageResult.getRecords().removeIf(topicSubject -> !topicSubject.getCategoryName().contains(topicSubjectListDto.getCategoryName()));
            }
            return Map.of("total", topicSubjectPageResult.getTotal(), "rows", topicSubjectPageResult.getRecords());
        }
    }

    /**
     * 新增专题
     *
     * @param topicSubjectDto
     */
    @Transactional
    public void add(TopicSubjectDto topicSubjectDto) {
        // 查询
        TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubjectDto.getId());
        if (topicSubjectDb != null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_NAME_EXIST);
        }
        //  根据分类名称查询分类
        TopicCategory topicCategoryDb = topicCategoryMapper
                .selectOne(new LambdaQueryWrapper<TopicCategory>().
                        eq(TopicCategory::getCategoryName, topicSubjectDto.getCategoryName()));
        if (topicCategoryDb == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_SELECT_ERROR);
        }
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        TopicSubject topicSubject = new TopicSubject();
        BeanUtils.copyProperties(topicSubjectDto, topicSubject);
        topicSubject.setCreateBy(username);
        topicSubjectMapper.insert(topicSubject);
        // TODO 异步发送消息给AI审核
        // 插入到关系表中
        TopicCategorySubject topicCategorySubject = new TopicCategorySubject();
        topicCategorySubject.setCategoryId(topicCategoryDb.getId());
        topicCategorySubject.setSubjectId(topicSubject.getId());
        topicCategorySubjectMapper.insert(topicCategorySubject);
    }


    /**
     * 修改题目专题
     *
     * @param topicSubjectDto
     */
    @Transactional
    public void update(TopicSubjectDto topicSubjectDto) {
        // 查询
        TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubjectDto.getId());
        if (topicSubjectDb == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_UPDATE_IS_NULL);
        }
        //  根据分类名称查询分类
        TopicCategory topicCategoryDb = topicCategoryMapper
                .selectOne(new LambdaQueryWrapper<TopicCategory>().
                        eq(TopicCategory::getCategoryName, topicSubjectDto.getCategoryName()));
        if (topicCategoryDb == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_SELECT_ERROR);
        }
        // 删除关系表
        topicCategorySubjectMapper
                .delete(new LambdaQueryWrapper<TopicCategorySubject>()
                        .eq(TopicCategorySubject::getSubjectId, topicSubjectDto.getId()));
        BeanUtils.copyProperties(topicSubjectDto, topicSubjectDb);
        //  TODO 异步发送消息给AI审核
        topicSubjectDb.setStatus(StatusEnums.AUDITING.getCode());
        topicSubjectMapper.updateById(topicSubjectDb);
        // 插入到关系表中
        TopicCategorySubject topicCategorySubject = new TopicCategorySubject();
        topicCategorySubject.setCategoryId(topicCategoryDb.getId());
        topicCategorySubject.setSubjectId(topicSubjectDb.getId());
        topicCategorySubjectMapper.insert(topicCategorySubject);
    }


    /**
     * 删除题目专题
     *
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        // 校验
        if (ids == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_DELETE_IS_NULL);
        }
        for (Long id : ids) {
            // 查询题目与专题关系表
            LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getSubjectId, id);
            TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
            if (topicSubjectTopic != null) {
                throw new TopicException(ResultCodeEnum.SUBJECT_DELETE_TOPIC_ERROR);
            }
            // 删除
            topicSubjectMapper.deleteById(id);
        }
    }
}
