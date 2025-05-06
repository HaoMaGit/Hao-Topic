package com.hao.topic.topic.service.impl;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.ai.client.ai.AiFeignClient;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.constant.RedisConstant;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.vo.topic.TopicCategoryDataVo;
import com.hao.topic.model.vo.topic.TopicSubjectDetailAndTopicVo;
import com.hao.topic.model.vo.topic.TopicUserRankVo;
import com.hao.topic.topic.mapper.*;
import com.hao.topic.topic.service.TopicDataService;
import com.hao.topic.topic.service.TopicSubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final StringRedisTemplate stringRedisTemplate;
    private final TopicSubjectMapper subjectMapper;
    private final TopicLabelMapper topicLabelMapper;
    private final AiFeignClient aiFeignClient;
    private final TopicCategoryMapper topicCategoryMapper;
    private final TopicCategorySubjectMapper topicCategorySubjectMapper;
    private final TopicSubjectMapper topicSubjectMapper;
    private final TopicSubjectService topicSubjectService;

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
        Long totalTopicRecordCountSize = null;
        LambdaQueryWrapper<TopicRecord> topicRecordLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        topicRecordLambdaQueryWrapper1.eq(TopicRecord::getUserId, userId);
        topicRecordLambdaQueryWrapper1.groupBy(TopicRecord::getTopicId);
        List<TopicRecord> totalTopicRecordCount = topicRecordMapper.selectList(topicRecordLambdaQueryWrapper1);
        if (CollectionUtils.isEmpty(totalTopicRecordCount)) {
            totalTopicRecordCountSize = 0L;
        } else {
            totalTopicRecordCountSize = (long) totalTopicRecordCount.size();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userCount", count);
        map.put("rank", rank);
        map.put("todayTopicCount", todayTopicCount);
        map.put("totalTopicCount", totalTopicCount);
        map.put("totalTopicRecordCount", totalTopicRecordCountSize);
        map.put("todayCount", todayCount);

        return map;
    }

    /**
     * 查询排行榜
     *
     * @param type
     * @return
     */
    public List<TopicUserRankVo> rank(Integer type) {
        if (type == null) {
            return null;
        }
        // 封装返回数据
        List<TopicUserRankVo> rankList = new ArrayList<>();
        if (type == 1) {
            // 今日
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String todayKey = RedisConstant.TOPIC_RANK_TODAY_PREFIX + date;
            // 获取排名前100的用户ID和分数
            Set<ZSetOperations.TypedTuple<String>> tuples = stringRedisTemplate.opsForZSet()
                    .reverseRangeWithScores(todayKey, 0, 99);
            if (CollectionUtils.isEmpty(tuples)) {
                // 为空查数据库
                List<TopicUserRankVo> countRank = topicRecordMapper.getCountRank(date);
                if (CollectionUtils.isNotEmpty(countRank)) {
                    readRankTodayCache(countRank);
                }
                return countRank;
            }
            getRankVo(rankList, tuples);
        } else {
            // 总排行榜
            // 获取排名前100的用户ID和分数
            Set<ZSetOperations.TypedTuple<String>> tuples = stringRedisTemplate.opsForZSet()
                    .reverseRangeWithScores(RedisConstant.TOPIC_RANK_PREFIX, 0, 99);
            if (CollectionUtils.isEmpty(tuples)) {
                // 为空查数据库
                List<TopicUserRankVo> countRank = topicRecordMapper.getCountRank(null);
                if (CollectionUtils.isNotEmpty(countRank)) {
                    readRankTodayCache(countRank);
                    readRankCache(countRank);
                }
                return countRank;
            }
            getRankVo(rankList, tuples);
        }
        return rankList;
    }


    /**
     * 获取排行榜数据
     *
     * @param rankList
     * @param tuples
     */
    private void getRankVo(List<TopicUserRankVo> rankList, Set<ZSetOperations.TypedTuple<String>> tuples) {
        if (tuples != null) {
            // 遍历排名100的用户
            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                // 获取用户ID和分数
                String userId = tuple.getValue();
                Double score = tuple.getScore();
                log.info("用户ID:{},分数:{}", userId, score);
                // 从Hash中获取用户详细信息
                Map<Object, Object> userInfo = stringRedisTemplate.opsForHash()
                        .entries(RedisConstant.USER_RANK_PREFIX + userId);

                // 封装
                TopicUserRankVo topicUserRankVo = new TopicUserRankVo();
                if (score != null) {
                    topicUserRankVo.setScope(score.longValue());
                }
                if (userInfo.get("avatar") == null) {
                    topicUserRankVo.setAvatar(null);
                } else {
                    topicUserRankVo.setAvatar((String) userInfo.get("avatar"));
                }
                topicUserRankVo.setNickname((String) userInfo.get("nickname"));
                if (userId != null) {
                    topicUserRankVo.setUserId(Long.valueOf(userId));
                }
                topicUserRankVo.setRole((String) userInfo.get("role"));

                rankList.add(topicUserRankVo);
            }
        }
    }

    /**
     * 获取当前用户排名信息
     *
     * @return TopicUserRankVo 包含用户排名、昵称、头像、分数等信息
     */
    public TopicUserRankVo userRank(Integer type) {
        // 获取当前登录用户id
        Long userId = SecurityUtils.getCurrentId();
        if (userId == null) {
            return null;
        }
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String key;
        if (type == 1) {
            // 今日排行榜 Key
            key = RedisConstant.TOPIC_RANK_TODAY_PREFIX + date;
        } else {
            // 总排行榜 Key
            key = RedisConstant.TOPIC_RANK_PREFIX;
        }

        // 查询用户排名（注意：reverseRank 是从高到低排序）
        Long rank = stringRedisTemplate.opsForZSet().reverseRank(key, userId.toString());
        if (type == 1) {
            // 判断用户排名是否为空
            if (rank == null) {
                //  为空查数据库
                return topicRecordMapper.getUserCountRank(date, userId);
            }
        } else {
            // 判断用户排名是否为空
            if (rank == null) {
                //  为空查数据库
                return topicRecordMapper.getUserCountRank(null, userId);
            }
        }


        // 查询用户积分
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());

        // 如果用户不在排行榜中（可能没有刷题记录）
        if (score == null) {
            return null;
        }

        // 获取用户详细信息
        Map<Object, Object> userInfo = stringRedisTemplate.opsForHash()
                .entries(RedisConstant.USER_RANK_PREFIX + userId);

        // 封装返回对象
        TopicUserRankVo topicUserRankVo = new TopicUserRankVo();
        topicUserRankVo.setUserId(userId);
        topicUserRankVo.setNickname((String) userInfo.get("nickname"));
        topicUserRankVo.setAvatar((String) userInfo.get("avatar"));
        topicUserRankVo.setScope(score.longValue());
        topicUserRankVo.setRank(rank + 1);
        topicUserRankVo.setRole((String) userInfo.get("role"));

        return topicUserRankVo;
    }


    /**
     * 将今日排行榜缓存数据写入redis
     */
    public void readRankTodayCache(List<TopicUserRankVo> countRank) {
        // 获取今日日期
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 存入redis
        for (TopicUserRankVo topicUserRankVo : countRank) {
            // 存储用户信息到Hash
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("nickname", topicUserRankVo.getNickname());
            userInfo.put("avatar", topicUserRankVo.getAvatar());
            userInfo.put("role", topicUserRankVo.getRole());
            stringRedisTemplate.opsForHash().putAll(RedisConstant.USER_RANK_PREFIX + topicUserRankVo.getUserId(), userInfo);
            // 存今日信息
            if (date.equals(topicUserRankVo.getTopicTime())) {
                stringRedisTemplate.opsForZSet().add(RedisConstant.TOPIC_RANK_TODAY_PREFIX + date, String.valueOf(topicUserRankVo.getUserId()), topicUserRankVo.getScope());
            }
        }
    }

    /**
     * 将总排行榜缓存数据写入redis
     */
    public void readRankCache(List<TopicUserRankVo> countRank) {
        for (TopicUserRankVo topicUserRankVo : countRank) {
            // 存全部信息
            stringRedisTemplate.opsForZSet().add(RedisConstant.TOPIC_RANK_PREFIX, String.valueOf(topicUserRankVo.getUserId()), topicUserRankVo.getScope());
        }
    }


    /**
     * 管理员顶部左侧数据统计
     *
     * @return
     */
    public Map<String, Object> adminHomeCount() {
        // 封装返回数据
        Map<String, Object> map = new HashMap<>();
        // 获取今日日期
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 获取昨日日期
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 1.刷题总数和比昨日少或多多少
        // 查询刷题次数总数
        Long count = topicRecordMapper.countTopicFrequency(null);
        // 查询今天的刷题总数
        Long countTodayTopicFrequency = topicRecordMapper.countTopicFrequency(date);
        countTodayTopicFrequency = countTodayTopicFrequency == null ? 0L : countTodayTopicFrequency;
        // 查询昨天的刷题总数
        Long countYesterdayTopicFrequency = topicRecordMapper.countTopicFrequency(yesterday);
        countYesterdayTopicFrequency = countYesterdayTopicFrequency == null ? 0L : countYesterdayTopicFrequency;
        // 计算差值（今天 - 昨天）
        long topicGrowthRate = countTodayTopicFrequency - countYesterdayTopicFrequency;

        // 2. AI调用总次数和比昨日少或多多少
        Long aiCount = aiFeignClient.count();
        // 查询今日
        Long aLong1 = aiFeignClient.countDate(date);
        // 查询昨日
        Long aLong = aiFeignClient.countDate(yesterday);
        // 计算差值（今天 - 昨天）
        long aiGrowthRate = aLong1 - aLong;

        // 3.用户总数和昨日幅度
        // 统计用户数量
        Long userCount = securityFeignClient.count();
        // 统计今日用户数量
        Long tCount = securityFeignClient.countDate(date);
        // 统计昨日用户数量
        Long yCount = securityFeignClient.countDate(yesterday);
        // 计算差值（今天 - 昨天）
        long userGrowthRate = tCount - yCount;

        // 4.题目总数量
        // 用户直接查询系统数量
        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLambdaQueryWrapper.eq(Topic::getCreateBy, "admin");
        topicLambdaQueryWrapper.eq(Topic::getStatus, StatusEnums.NORMAL.getCode());
        Long totalTopicCount = topicMapper.selectCount(topicLambdaQueryWrapper);

        // 5.专题总数量
        LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicSubjectLambdaQueryWrapper.eq(TopicSubject::getStatus, StatusEnums.NORMAL.getCode());
        topicLambdaQueryWrapper.eq(Topic::getCreateBy, "admin");
        Long totalSubjectCount = subjectMapper.selectCount(topicSubjectLambdaQueryWrapper);

        // 6.标签总数量
        LambdaQueryWrapper<TopicLabel> topicTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicTagLambdaQueryWrapper.eq(TopicLabel::getStatus, StatusEnums.NORMAL.getCode());
        topicLambdaQueryWrapper.eq(Topic::getCreateBy, "admin");
        Long topicLabelCount = topicLabelMapper.selectCount(topicTagLambdaQueryWrapper);

        map.put("countTodayFrequency", count);
        map.put("topicGrowthRate", topicGrowthRate);
        map.put("userCount", userCount);
        map.put("userGrowthRate", userGrowthRate);
        map.put("totalTopicCount", totalTopicCount);
        map.put("totalSubjectCount", totalSubjectCount);
        map.put("topicLabelCount", topicLabelCount);
        map.put("aiCount", aiCount);
        map.put("aiGrowthRate", aiGrowthRate);

        return map;
    }


    /**
     * 查询分类名称和分类名称下的题目总数量
     *
     * @return
     */
    public List<TopicCategoryDataVo> adminHomeCategory() {
        // 1.查询所有的分类
        LambdaQueryWrapper<TopicCategory> topicCategoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicCategoryLambdaQueryWrapper.eq(TopicCategory::getStatus, StatusEnums.NORMAL.getCode());
        topicCategoryLambdaQueryWrapper.eq(TopicCategory::getCreateBy, "admin");
        List<TopicCategory> topicCategoryList = topicCategoryMapper.selectList(topicCategoryLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(topicCategoryList)) {
            return null;
        }
        // 2.查询分类专题表
        return topicCategoryList.stream().map(topicCategory -> {
            TopicCategoryDataVo topicCategoryDataVo = new TopicCategoryDataVo();
            topicCategoryDataVo.setCategoryName(topicCategory.getCategoryName());
            LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getCategoryId, topicCategory.getId());
            List<TopicCategorySubject> topicCategorySubjects = topicCategorySubjectMapper.selectList(topicCategorySubjectLambdaQueryWrapper);
            if (CollectionUtils.isEmpty(topicCategorySubjects)) {
                topicCategoryDataVo.setCount(0L);
                return topicCategoryDataVo;
            }
            long count = 0L;
            // 3.查询专题表
            for (TopicCategorySubject topicCategorySubject : topicCategorySubjects) {
                LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectLambdaQueryWrapper.eq(TopicSubject::getId, topicCategorySubject.getSubjectId());
                topicSubjectLambdaQueryWrapper.eq(TopicSubject::getStatus, StatusEnums.NORMAL.getCode());
                topicSubjectLambdaQueryWrapper.eq(TopicSubject::getCreateBy, "admin");
                TopicSubject topicSubject = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                if (topicSubject != null) {
                    // 查询题目转
                    TopicSubjectDetailAndTopicVo topicSubjectDetailAndTopicVo = topicSubjectService.subjectDetail(topicSubject.getId());
                    if (topicSubjectDetailAndTopicVo != null) {
                        if (CollectionUtils.isNotEmpty(topicSubjectDetailAndTopicVo.getTopicNameVos())) {
                            count += topicSubjectDetailAndTopicVo.getTopicNameVos().size();
                        }
                    }

                }
            }

            topicCategoryDataVo.setCount(count);
            return topicCategoryDataVo;
        }).toList();
    }
}
