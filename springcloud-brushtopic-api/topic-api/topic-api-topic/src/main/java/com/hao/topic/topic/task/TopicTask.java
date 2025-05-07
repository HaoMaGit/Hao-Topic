package com.hao.topic.topic.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.entity.topic.TopicDailyStaging;
import com.hao.topic.model.entity.topic.TopicSubjectTopic;
import com.hao.topic.model.vo.topic.TopicUserRankVo;
import com.hao.topic.topic.mapper.TopicDailyStagingMapper;
import com.hao.topic.topic.mapper.TopicMapper;
import com.hao.topic.topic.mapper.TopicRecordMapper;
import com.hao.topic.topic.mapper.TopicSubjectTopicMapper;
import com.hao.topic.topic.service.impl.TopicDataServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: 题目相关定时任务
 * Author: Hao
 * Date: 2025/5/7 14:35
 */
@EnableScheduling
@Component
@Slf4j
@AllArgsConstructor
public class TopicTask {
    private final TopicRecordMapper topicRecordMapper;
    private final TopicDataServiceImpl topicDataService;
    private final TopicMapper topicMapper;
    private final TopicDailyStagingMapper topicDailyStagingMapper;
    private final TopicSubjectTopicMapper topicSubjectTopicMapper;

    /**
     * 每天凌晨 12:00 执行 - 查询排行榜数据并重新写入Redis防止redis挂了导致数据丢失
     */
    @Scheduled(cron = "0 0 0 * * ?")
    // @Scheduled(cron = "0 * * * * ?") // 1分钟
    public void refreshRankingToRedis() {
        log.info("refreshRankingToRedis===========>我执行了");
        // 为空查数据库
        List<TopicUserRankVo> countRank = topicRecordMapper.getCountRank(null);
        if (CollectionUtils.isNotEmpty(countRank)) {
            // 写入今日用户信息和数据
            topicDataService.readRankTodayCache(countRank);
            // 写入全部排行榜数据
            topicDataService.readRankCache(countRank);
        }
    }

    /**
     * 每天凌晨 12:00 执行 - 删除用户每日必刷并将所有用户的每日必刷题数据写入Redis和数据库
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshUserTopicToRedis() {
        // 删除所有数据
        int delete = topicDailyStagingMapper.delete(null);

        // 查询管理员是否设置了每日必刷
        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLambdaQueryWrapper.eq(Topic::getIsEveryday, 1);
        topicLambdaQueryWrapper.eq(Topic::getIsMember, 0);
        topicLambdaQueryWrapper.eq(Topic::getStatus, StatusEnums.NORMAL.getCode());
        List<Topic> topicList = topicMapper.selectList(topicLambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(topicList)) {
            // 获取到数量
            int size = topicList.size();
            // 不为空判断数量是否大于9个
            if (size == 9) {
                // 等于9个说明当日必刷全是管理员选的直接存公共的
                for (Topic topic : topicList) {
                    // 查询题目专题表
                    LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topic.getId());
                    TopicSubjectTopic topicSubjectTopicDb = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                    TopicDailyStaging topicDailyStaging = new TopicDailyStaging();
                    topicDailyStaging.setTopicId(topic.getId());
                    topicDailyStaging.setSubjectId(topicSubjectTopicDb.getSubjectId());
                    topicDailyStaging.setIsPublic(1);
                    // 插入到每日必刷
                    topicDailyStagingMapper.insert(topicDailyStaging);
                }
                return;
            }
            // 不是等于9说明还有空间算出剩余多少个
            int randomTopicSize = 9 - size;
            for (int i = 0; i < randomTopicSize; i++) {
                // 查询出所有的用户id
            }
        }
    }
}
