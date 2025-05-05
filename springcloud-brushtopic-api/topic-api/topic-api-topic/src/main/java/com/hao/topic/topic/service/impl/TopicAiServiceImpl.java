package com.hao.topic.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.model.entity.topic.TopicSubjectTopic;
import com.hao.topic.model.vo.topic.TopicSubjectVo;
import com.hao.topic.topic.mapper.TopicMapper;
import com.hao.topic.topic.mapper.TopicSubjectMapper;
import com.hao.topic.topic.mapper.TopicSubjectTopicMapper;
import com.hao.topic.topic.service.TopicAiService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/5 17:13
 */
@Service
@AllArgsConstructor
public class TopicAiServiceImpl implements TopicAiService {
    private final TopicMapper topicMapper;
    private final TopicSubjectTopicMapper topicSubjectTopicMapper;
    private final TopicSubjectMapper topicSubjectMapper;

    /**
     * 根据专题id查询题目列表
     *
     * @param subjectId
     * @return
     */
    public List<Topic> getSubjectIdByTopicList(Long subjectId) {
        // 查询题目专题关系表
        LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getSubjectId, subjectId);
        List<TopicSubjectTopic> topicSubjectTopics = topicSubjectTopicMapper.selectList(topicSubjectTopicLambdaQueryWrapper);
        if (topicSubjectTopics.size() == 0) {
            return null;
        }
        // 获取所有的题目id
        List<Long> topicIds = topicSubjectTopics.stream()
                .map(TopicSubjectTopic::getTopicId).toList();
        // 查询所有的题目
        return topicMapper.selectBatchIds(topicIds);
    }

    /**
     * 查询全部专题或会员专题
     *
     * @param role
     * @param createBy
     * @return
     */
    public List<TopicSubjectVo> getSubject(String role, String createBy) {

        // 全部数据
        List<TopicSubject> topicSubjectList = null;
        // 会员数据
        List<TopicSubject> topicSubjects = null;
        // 查公共的数据
        LambdaQueryWrapper<TopicSubject> subjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper.eq(TopicSubject::getStatus, StatusEnums.NORMAL.getCode())
                .eq(TopicSubject::getCreateBy, "admin")
                .orderByDesc(TopicSubject::getCreateTime);
        topicSubjectList = topicSubjectMapper.selectList(subjectLambdaQueryWrapper);
        if (role.equals("member")) {
            // 是会员可以查自己专属的
            // 为true说明开启了可以查自己的
            LambdaQueryWrapper<TopicSubject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(TopicSubject::getStatus, StatusEnums.NORMAL.getCode());
            lambdaQueryWrapper.orderByDesc(TopicSubject::getCreateTime);
            lambdaQueryWrapper.eq(TopicSubject::getCreateBy, createBy);
            topicSubjects = topicSubjectMapper.selectList(lambdaQueryWrapper);
        }
        // 判断会员数据是否为空
        if (!CollectionUtils.isEmpty(topicSubjects)) {
            // 不为空将会员数据放在全部数据的前面
            topicSubjectList.addAll(0, topicSubjects);
        }
        return topicSubjectList.stream().map(topicSubject -> {
            TopicSubjectVo topicSubjectVo = new TopicSubjectVo();
            BeanUtils.copyProperties(topicSubject, topicSubjectVo);
            return topicSubjectVo;
        }).toList();
    }
}
