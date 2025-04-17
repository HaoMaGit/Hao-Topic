package com.hao.topic.topic.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.topic.TopicDto;
import com.hao.topic.model.dto.topic.TopicListDto;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.vo.topic.TopicVo;
import com.hao.topic.topic.enums.StatusEnums;
import com.hao.topic.topic.mapper.*;
import com.hao.topic.topic.service.TopicService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/2 17:47
 */
@Service
@Slf4j
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicMapper topicMapper;
    private final TopicSubjectTopicMapper topicSubjectTopicMapper;
    private final TopicSubjectMapper topicSubjectMapper;
    private final TopicLabelMapper topicLabelMapper;
    private final TopicLabelTopicMapper topicLabelTopicMapper;

    /**
     * 查询题目列表
     *
     * @param topicListDto
     * @return
     */
    public Map<String, Object> topicList(TopicListDto topicListDto) {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页条件
        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断是否为Hao
        if (currentId != 1L) {
            // 根据当前登录用户查询
            topicLambdaQueryWrapper.like(Topic::getCreateBy, username);
        } else {
            // 是超管
            // 判断是否查询创建人
            if (!StringUtils.isEmpty(topicListDto.getCreateBy())) {
                topicLambdaQueryWrapper.like(Topic::getCreateBy, topicListDto.getCreateBy());
            }
        }
        // 判断题目名称
        if (!StringUtils.isEmpty(topicListDto.getTopicName())) {
            topicLambdaQueryWrapper.like(Topic::getTopicName, topicListDto.getTopicName());
        }
        // 判断创建时间
        if (!StringUtils.isEmpty(topicListDto.getBeginCreateTime()) && !StringUtils.isEmpty(topicListDto.getEndCreateTime())) {
            topicLambdaQueryWrapper.between(Topic::getCreateTime, topicListDto.getBeginCreateTime(), topicListDto.getEndCreateTime());
        }
        topicLambdaQueryWrapper.orderByDesc(Topic::getCreateTime);
        if (topicListDto.getPageNum() == null || topicListDto.getPageSize() == null) {
            List<Topic> topics = topicMapper.selectList(topicLambdaQueryWrapper);
            List<TopicVo> list = topics.stream().map(topic -> {
                TopicVo topicVo = new TopicVo();
                BeanUtils.copyProperties(topic, topicVo);
                // 根据专题id查询专题
                LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topic.getId());
                TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                if (topicSubjectTopic != null) {
                    TopicSubject topicSubject = topicSubjectMapper.selectById(topicSubjectTopic.getSubjectId());
                    if (topicSubject != null) {
                        topicVo.setSubject(topicSubject.getSubjectName());
                    }
                }
                List<String> stringList = new ArrayList<>();
                // 封装标签
                LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topic.getId());
                List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
                if (CollectionUtils.isNotEmpty(topicLabelTopics)) {
                    topicLabelTopics.forEach(topicLabelTopic -> {
                        TopicLabel topicLabel = topicLabelMapper.selectById(topicLabelTopic.getLabelId());
                        if (topicLabel != null) {
                            stringList.add(topicLabel.getLabelName());
                        }
                    });
                }
                topicVo.setLabels(stringList);
                return topicVo;
            }).toList();
            // 查询标签
            return Map.of("total", list.size(), "rows", list);
        } else {
            // 设置分页参数
            Page<Topic> topicPage = new Page<>(topicListDto.getPageNum(), topicListDto.getPageSize());
            // 开始查询
            Page<Topic> topicListPage = topicMapper.selectPage(topicPage, topicLambdaQueryWrapper);
            List<TopicVo> list = topicListPage.getRecords().stream().map(topic -> {
                TopicVo topicVo = new TopicVo();
                BeanUtils.copyProperties(topic, topicVo);
                // 根据专题id查询专题
                LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topic.getId());
                TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                if (topicSubjectTopic != null) {
                    TopicSubject topicSubject = topicSubjectMapper.selectById(topicSubjectTopic.getSubjectId());
                    if (topicSubject != null) {
                        topicVo.setSubject(topicSubject.getSubjectName());
                    }
                }
                List<String> stringList = new ArrayList<>();
                // 封装标签
                LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topic.getId());
                List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
                if (CollectionUtils.isNotEmpty(topicLabelTopics)) {
                    topicLabelTopics.forEach(topicLabelTopic -> {
                        TopicLabel topicLabel = topicLabelMapper.selectById(topicLabelTopic.getLabelId());
                        if (topicLabel != null) {
                            stringList.add(topicLabel.getLabelName());
                        }
                    });
                }
                topicVo.setLabels(stringList);
                return topicVo;
            }).toList();
            return Map.of("total", topicListPage.getTotal(), "rows", list);
        }
    }

    /**
     * 新增题目
     *
     * @param topicDto
     */
    @Transactional
    public void add(TopicDto topicDto) {
        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLambdaQueryWrapper.eq(Topic::getTopicName, topicDto.getTopicName());
        // 查询
        Topic topicDb = topicMapper.selectOne(topicLambdaQueryWrapper);
        if (topicDb != null) {
            throw new TopicException(ResultCodeEnum.TOPIC_NAME_EXIST);
        }
        // 查询专题
        TopicSubject topicSubject = topicSubjectMapper.selectById(topicDto.getSubjectId());
        // 判断
        if (topicSubject == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_NOT_EXIST);
        }
        // 查询标签
        List<TopicLabel> topicLabels = topicLabelMapper.selectBatchIds(topicDto.getLabelIds());
        if (CollectionUtils.isEmpty(topicLabels)) {
            throw new TopicException(ResultCodeEnum.LABEL_NOT_EXIST);
        }

        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 新增题目
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicDto, topic);
        // 设置创建人
        topic.setCreateBy(username);
        // TODO 生成AI题目
        // TODO 异步发送信息给AI审核
        // 开始插入
        topicMapper.insert(topic);

        // 插入到专题题目关系表中
        TopicSubjectTopic topicSubjectTopic = new TopicSubjectTopic();
        topicSubjectTopic.setTopicId(topic.getId());
        topicSubjectTopic.setSubjectId(topicDto.getSubjectId());
        topicSubjectTopicMapper.insert(topicSubjectTopic);

        // 更新专题数量
        topicSubject.setTopicCount(topicSubject.getTopicCount() + 1);
        topicSubjectMapper.updateById(topicSubject);
        for (TopicLabel topicLabel : topicLabels) {
            // 插入到题目标签关系表中
            TopicLabelTopic topicLabelTopic = new TopicLabelTopic();
            topicLabelTopic.setTopicId(topic.getId());
            topicLabelTopic.setLabelId(topicLabel.getId());
            topicLabelTopicMapper.insert(topicLabelTopic);

            // 更新标签被使用次数
            topicLabel.setUseCount(topicLabel.getUseCount() + 1);
            topicLabelMapper.updateById(topicLabel);
        }
    }

    /**
     * 修改题目
     *
     * @param topicDto
     */
    @Transactional
    public void update(TopicDto topicDto) {
        // 根据id查询
        Topic oldTopic = topicMapper.selectById(topicDto.getId());
        if (oldTopic == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_UPDATE_IS_NULL);
        }
        // 查询专题
        TopicSubject topicSubject = topicSubjectMapper.selectById(topicDto.getSubjectId());
        // 判断
        if (topicSubject == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_NOT_EXIST);
        }
        // 查询标签
        List<TopicLabel> topicLabels = topicLabelMapper.selectBatchIds(topicDto.getLabelIds());
        if (CollectionUtils.isEmpty(topicLabels)) {
            throw new TopicException(ResultCodeEnum.LABEL_NOT_EXIST);
        }
        // 修改题目
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicDto, topic);


        // TODO 生成AI题目
        // TODO 异步发送信息给AI审核
        topic.setStatus(StatusEnums.AUDITING.getCode());
        // 开始更新
        topicMapper.updateById(topic);

        // 查询专题题目关系表
        LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, oldTopic.getId());
        TopicSubjectTopic topicSubjectTopicDb = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
        if (topicSubjectTopicDb != null) {
            // 查询专题
            TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubjectTopicDb.getSubjectId());
            if (topicSubjectDb != null) {
                // 更新专题数量
                topicSubjectDb.setTopicCount(topicSubjectDb.getTopicCount() - 1);
                topicSubjectMapper.updateById(topicSubjectDb);
            }
            // 删除
            topicSubjectTopicMapper.deleteById(topicSubjectTopicDb);
        }

        // 插入到专题题目关系表中
        TopicSubjectTopic topicSubjectTopic = new TopicSubjectTopic();
        topicSubjectTopic.setTopicId(topic.getId());
        topicSubjectTopic.setSubjectId(topicDto.getSubjectId());
        topicSubjectTopicMapper.insert(topicSubjectTopic);
        // 更新次数
        topicSubject.setTopicCount(topicSubject.getTopicCount() + 1);
        topicSubjectMapper.updateById(topicSubject);

        // 查询标签题目关系表
        LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, oldTopic.getId());
        List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(topicLabelTopics)) {
            for (TopicLabelTopic topicLabelTopic : topicLabelTopics) {
                // 查询标签
                TopicLabel topicLabelDb = topicLabelMapper.selectById(topicLabelTopic.getLabelId());
                if (topicLabelDb != null) {
                    // 更新标签被使用次数
                    topicLabelDb.setUseCount(topicLabelDb.getUseCount() - 1);
                    topicLabelMapper.updateById(topicLabelDb);
                }
                // 删除
                topicLabelTopicMapper.deleteById(topicLabelTopic);
            }
        }

        for (TopicLabel topicLabel : topicLabels) {
            // 插入到题目标签关系表中
            TopicLabelTopic topicLabelTopic = new TopicLabelTopic();
            topicLabelTopic.setTopicId(topic.getId());
            topicLabelTopic.setLabelId(topicLabel.getId());
            topicLabelTopicMapper.insert(topicLabelTopic);

            // 更新标签被使用次数
            topicLabel.setUseCount(topicLabel.getUseCount() + 1);
            topicLabelMapper.updateById(topicLabel);
        }
    }
}
