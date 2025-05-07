package com.hao.topic.topic.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.vo.topic.TopicUserRankVo;
import com.hao.topic.topic.mapper.*;
import com.hao.topic.topic.service.impl.TopicDataServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private final SecurityFeignClient securityFeignClient;
    private final TopicSubjectMapper topicSubjectMapper;

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
    // @Scheduled(cron = "0 * * * * ?") // 1分钟
    public void refreshUserTopicToRedis() {
        // 删除所有数据
        int delete = topicDailyStagingMapper.delete(null);

        // 查询管理员是否设置了每日必刷
        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLambdaQueryWrapper.eq(Topic::getIsEveryday, 1);
        topicLambdaQueryWrapper.eq(Topic::getIsMember, 0);
        topicLambdaQueryWrapper.eq(Topic::getStatus, StatusEnums.NORMAL.getCode());
        List<Topic> topicList = topicMapper.selectList(topicLambdaQueryWrapper);
        // 查询所有的题目
        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        topicLambdaQueryWrapper1.eq(Topic::getStatus, StatusEnums.NORMAL.getCode());
        List<Topic> topics = topicMapper.selectList(topicLambdaQueryWrapper1);
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
            // 查询出所有的用户id
            List<SysUser> allUser = securityFeignClient.getAllUser();
            if (CollectionUtils.isNotEmpty(allUser)) {
                for (SysUser sysUser : allUser) {
                    // 根据用户id查询用户刷题表
                    LambdaQueryWrapper<TopicRecord> topicRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicRecordLambdaQueryWrapper.eq(TopicRecord::getUserId, sysUser.getId());
                    List<TopicRecord> topicRecords = topicRecordMapper.selectList(topicRecordLambdaQueryWrapper);
                    if (CollectionUtils.isEmpty(topicRecords)) {
                        // 用户还没有刷过题目
                        for (int i = 0; i < randomTopicSize; i++) {
                            int randomIndex = new Random().nextInt(topics.size());
                            Topic selectedTopic = topics.get(randomIndex);
                            // 根据题目id查询专题id
                            LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                            topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, selectedTopic.getId());
                            TopicSubjectTopic topicSubjectTopicDb = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                            TopicDailyStaging topicDailyStaging = new TopicDailyStaging();
                            topicDailyStaging.setTopicId(selectedTopic.getId());
                            topicDailyStaging.setSubjectId(topicSubjectTopicDb.getSubjectId());
                            topicDailyStaging.setIsPublic(1);
                            topicDailyStagingMapper.insert(topicDailyStaging);
                        }
                    } else {
                        // 用户刷过题目分析用户的刷题记录
                        // 判断那个专题id用户刷的最多了
                        Long subjectId = topicRecordMapper.selectMaxSubject(sysUser.getId());
                        // 然后根据专题id查询专题表
                        LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        topicSubjectLambdaQueryWrapper.eq(TopicSubject::getId, subjectId);
                        TopicSubject topicSubject = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                        if (topicSubject != null) {
                            // 找到用户刷的最多的专题信息了查询该专题下的所有题目
                            // 查询题目专题表
                            LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                            topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getSubjectId, topicSubject.getId());
                            List<TopicSubjectTopic> topicSubjectTopics = topicSubjectTopicMapper.selectList(topicSubjectTopicLambdaQueryWrapper);
                            // 判断
                            if (CollectionUtils.isEmpty(topicSubjectTopics)) {
                                continue;
                            }
                            // 存放所有的题目
                            List<Topic> topicListAll = new ArrayList<>();
                            // 查询所有的题目
                            topicSubjectTopics.forEach(item -> {
                                Long topicId = item.getTopicId();
                                Topic topic = topicMapper.selectById(topicId);
                                if (topic.getStatus() == 0 && topic.getIsMember() == 0 && topic.getIsEveryday() == 0) {
                                    topicListAll.add(topic);
                                }
                            });
                            // 已经拿到所有的题目了开始随机获取randomTopicSize个题目
                            for (int i = 0; i < randomTopicSize; i++) {
                                // 使用随机数抽取topicListAll中的任意一道
                                int randomIndex = new Random().nextInt(topicListAll.size());
                                Topic selectedTopic = topicListAll.get(randomIndex);
                                TopicDailyStaging topicDailyStaging = new TopicDailyStaging();
                                topicDailyStaging.setTopicId(selectedTopic.getId());
                                topicDailyStaging.setSubjectId(subjectId);
                                topicDailyStaging.setIsPublic(2);
                                topicDailyStaging.setUserId(sysUser.getId());
                                topicDailyStagingMapper.insert(topicDailyStaging);
                            }
                        }
                    }
                }
                return;
            }
        }
        // 说明管理员没有设置每日题目
        // 开始分配 5道随机题目 4道用户经常刷题目
        if (CollectionUtils.isNotEmpty(topics)) {
            for (int i = 0; i < 5; ) {
                int randomIndex = new Random().nextInt(topics.size());
                Topic selectedTopic = topics.get(randomIndex);
                // 根据题目id查询专题id
                LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, selectedTopic.getId());
                TopicSubjectTopic topicSubjectTopicDb = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                // 查询这个公共的题目是否存在了
                // 查询这个公共的题目是否存在了
                LambdaQueryWrapper<TopicDailyStaging> topicDailyStagingLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicDailyStagingLambdaQueryWrapper.eq(TopicDailyStaging::getTopicId, selectedTopic.getId())
                        .eq(TopicDailyStaging::getIsPublic, 1);
                if (topicDailyStagingMapper.selectOne(topicDailyStagingLambdaQueryWrapper) == null) {
                    // 如果不存在，则插入新记录
                    TopicDailyStaging topicDailyStaging = new TopicDailyStaging();
                    topicDailyStaging.setTopicId(selectedTopic.getId());
                    topicDailyStaging.setSubjectId(topicSubjectTopicDb.getSubjectId());
                    topicDailyStaging.setIsPublic(1);
                    topicDailyStagingMapper.insert(topicDailyStaging);

                    // 成功插入后增加计数器
                    i++;
                }
            }
        }
        // 在分配4道用户经常刷的题目
        // 查询出所有的用户id
        List<SysUser> allUser = securityFeignClient.getAllUser();
        if (CollectionUtils.isNotEmpty(allUser)) {
            for (SysUser sysUser : allUser) {
                // 根据用户id查询用户刷题表
                LambdaQueryWrapper<TopicRecord> topicRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicRecordLambdaQueryWrapper.eq(TopicRecord::getUserId, sysUser.getId());
                List<TopicRecord> topicRecords = topicRecordMapper.selectList(topicRecordLambdaQueryWrapper);
                if (CollectionUtils.isEmpty(topicRecords)) {
                    // 用户还没有每个人分个题目
                    for (int i = 0; i < 4; ) {
                        int randomIndex = new Random().nextInt(topics.size());
                        Topic selectedTopic = topics.get(randomIndex);
                        // 根据题目id查询专题id
                        LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, selectedTopic.getId());
                        TopicSubjectTopic topicSubjectTopicDb = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                        // 查询这个用户中是否有这个题目
                        LambdaQueryWrapper<TopicDailyStaging> topicDailyStagingLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        topicDailyStagingLambdaQueryWrapper.eq(TopicDailyStaging::getTopicId, selectedTopic.getId())
                                .eq(TopicDailyStaging::getIsPublic, 2)
                                .eq(TopicDailyStaging::getUserId, sysUser.getId());
                        if (topicDailyStagingMapper.selectOne(topicDailyStagingLambdaQueryWrapper) == null) {
                            // 查询公共中是否有这个题目
                            LambdaQueryWrapper<TopicDailyStaging> topicDailyStagingLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                            topicDailyStagingLambdaQueryWrapper1.eq(TopicDailyStaging::getIsPublic, 1);
                            topicDailyStagingLambdaQueryWrapper1.eq(TopicDailyStaging::getTopicId, selectedTopic.getId());
                            if (topicDailyStagingMapper.selectOne(topicDailyStagingLambdaQueryWrapper1) == null) {
                                // 如果存在，则插入新记录
                                TopicDailyStaging topicDailyStaging = new TopicDailyStaging();
                                topicDailyStaging.setTopicId(selectedTopic.getId());
                                topicDailyStaging.setSubjectId(topicSubjectTopicDb.getSubjectId());
                                topicDailyStaging.setIsPublic(2);
                                topicDailyStaging.setUserId(sysUser.getId());
                                topicDailyStagingMapper.insert(topicDailyStaging);

                                // 成功插入后增加计数器
                                i++;
                            }

                        }
                    }
                } else {
                    // 用户刷过题目分析用户的刷题记录
                    // 判断那个专题id用户刷的最多了
                    Long subjectId = topicRecordMapper.selectMaxSubject(sysUser.getId());
                    // 然后根据专题id查询专题表
                    LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicSubjectLambdaQueryWrapper.eq(TopicSubject::getId, subjectId);
                    TopicSubject topicSubject = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                    if (topicSubject != null) {
                        // 找到用户刷的最多的专题信息了查询该专题下的所有题目
                        // 查询题目专题表
                        LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getSubjectId, topicSubject.getId());
                        List<TopicSubjectTopic> topicSubjectTopics = topicSubjectTopicMapper.selectList(topicSubjectTopicLambdaQueryWrapper);
                        // 判断
                        if (CollectionUtils.isEmpty(topicSubjectTopics)) {
                            continue;
                        }
                        // 存放所有的题目
                        List<Topic> topicListAll = new ArrayList<>();
                        // 查询所有的题目
                        topicSubjectTopics.forEach(item -> {
                            Long topicId = item.getTopicId();
                            Topic topic = topicMapper.selectById(topicId);
                            if (topic.getStatus() == 0 && topic.getIsMember() == 0 && topic.getIsEveryday() == 0) {
                                topicListAll.add(topic);
                            }
                        });
                        // 已经拿到所有的题目了开始随机获取randomTopicSize个题目
                        for (int i = 0; i < 4; ) {
                            // 使用随机数抽取topicListAll中的任意一道
                            int randomIndex = new Random().nextInt(topicListAll.size());
                            Topic selectedTopic = topicListAll.get(randomIndex);
                            // 查询一下用户中有没有重复的
                            LambdaQueryWrapper<TopicDailyStaging> eq = new LambdaQueryWrapper<TopicDailyStaging>()
                                    .eq(TopicDailyStaging::getTopicId, selectedTopic.getId())
                                    .eq(TopicDailyStaging::getUserId, sysUser.getId())
                                    .eq(TopicDailyStaging::getIsPublic, 2);
                            if (topicDailyStagingMapper.selectOne(eq) == null) {
                                // 查询公共中是否存了这个题目
                                LambdaQueryWrapper<TopicDailyStaging> topicDailyStagingLambdaQueryWrapper = new LambdaQueryWrapper<>();
                                topicDailyStagingLambdaQueryWrapper.eq(TopicDailyStaging::getIsPublic, 1);
                                topicDailyStagingLambdaQueryWrapper.eq(TopicDailyStaging::getTopicId, selectedTopic.getId());
                                if (topicDailyStagingMapper.selectOne(topicDailyStagingLambdaQueryWrapper) == null) {
                                    TopicDailyStaging topicDailyStaging = new TopicDailyStaging();
                                    topicDailyStaging.setTopicId(selectedTopic.getId());
                                    topicDailyStaging.setSubjectId(subjectId);
                                    topicDailyStaging.setIsPublic(2);
                                    topicDailyStaging.setUserId(sysUser.getId());
                                    topicDailyStagingMapper.insert(topicDailyStaging);
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
