package com.hao.topic.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.entity.topic.TopicRecord;
import com.hao.topic.topic.mapper.TopicMapper;
import com.hao.topic.topic.mapper.TopicRecordMapper;
import com.hao.topic.topic.service.TopicDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/4 22:27
 */
@Service
@AllArgsConstructor
@Slf4j
public class TopicDataServiceImpl implements TopicDataService {
    private final SecurityFeignClient securityFeignClient;
    private final TopicRecordMapper topicRecordMapper;
    private final TopicMapper topicMapper;

    /**
     * 查询题目刷题数据以及刷题排名和用户数量
     *
     * @return
     */
    public Map<String, Object> webHomeCount() {
        // 当前用户id
        Long userId = SecurityUtils.getCurrentId();
        // 当前用户身份
        String role = SecurityUtils.getCurrentRole();
        // 获取当前用户账户
        String username = SecurityUtils.getCurrentName();
        // 统计用户数量
        Long count = securityFeignClient.count();
        // 统计用户排名
        Long rank = topicRecordMapper.getRank(userId);
        // 统计用户今日刷题次数
        Long todayCount = null;
        LambdaQueryWrapper<TopicRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TopicRecord::getUserId, userId);
        wrapper.eq(TopicRecord::getTopicTime, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<TopicRecord> topicRecords = topicRecordMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(topicRecords)) {
            todayCount = topicRecords.stream().mapToLong(TopicRecord::getCount).sum();
        } else {
            todayCount = 0L;
        }
        // 统计用户今日刷题数量
        LambdaQueryWrapper<TopicRecord> topicRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicRecordLambdaQueryWrapper.eq(TopicRecord::getUserId, userId);
        topicRecordLambdaQueryWrapper.eq(TopicRecord::getTopicTime, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Long todayTopicCount = topicRecordMapper.selectCount(topicRecordLambdaQueryWrapper);
        // 统计题目数量总数
        Long totalTopicCount = null;
        if (role.equals("user")) {
            // 用户直接查询系统数量
            LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicLambdaQueryWrapper.eq(Topic::getCreateBy, "admin");
            topicLambdaQueryWrapper.eq(Topic::getStatus, StatusEnums.NORMAL.getCode());
            totalTopicCount = topicMapper.selectCount(topicLambdaQueryWrapper);
        } else {
            // 管理员和会员可以查自己的
            LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicLambdaQueryWrapper.eq(Topic::getStatus, StatusEnums.NORMAL.getCode());
            topicLambdaQueryWrapper.in(Topic::getCreateBy, "admin", username);
            totalTopicCount = topicMapper.selectCount(topicLambdaQueryWrapper);
        }
        // 统计用户已刷总数
        LambdaQueryWrapper<TopicRecord> topicRecordLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        topicRecordLambdaQueryWrapper1.eq(TopicRecord::getUserId, userId);
        topicRecordLambdaQueryWrapper1.groupBy(TopicRecord::getTopicId);
        List<TopicRecord> totalTopicRecordCount = topicRecordMapper.selectList(topicRecordLambdaQueryWrapper1);
        return Map.of(
                "userCount", count,
                "rank", rank,
                "todayTopicCount", todayTopicCount,
                "totalTopicCount", totalTopicCount,
                "totalTopicRecordCount", totalTopicRecordCount.size(),
                "todayCount", todayCount
        );
    }
}
