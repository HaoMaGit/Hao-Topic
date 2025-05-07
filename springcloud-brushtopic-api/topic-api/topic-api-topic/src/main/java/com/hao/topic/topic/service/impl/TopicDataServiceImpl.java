package com.hao.topic.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.ai.client.ai.AiFeignClient;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.constant.RedisConstant;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.vo.ai.AiTrendVo;
import com.hao.topic.model.vo.system.SysUserTrentVo;
import com.hao.topic.model.vo.topic.*;
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
import java.util.stream.Collectors;

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
    private final TopicDailyStagingMapper topicDailyStagingMapper;
    private final TopicLabelTopicMapper topicLabelTopicMapper;
    private final TopicDailyBrushMapper topicDailyBrushMapper;

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
                    // 查询题目
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

    /**
     * 刷题题目和刷题人数趋势图
     *
     * @return
     */
    public TopicTrendVo topicTrend() {
        Map<String, Object> map = new HashMap<>();
        // 查询近15日的刷题统计数据
        List<TopicDataVo> topicDataTopicList = topicRecordMapper.countTopicDay15();
        // 查询近15日的刷题人数数据
        List<TopicDataVo> topicDataVoUserList = topicRecordMapper.countUserDay15();
        // 映射日期
        List<String> dateList = topicDataTopicList.stream().map(TopicDataVo::getDate).toList();
        // 映射刷题数据
        List<Integer> countTopicList = topicDataTopicList.stream().map(TopicDataVo::getCount).toList();
        // 映射刷题人数数据
        List<Integer> countUserList = topicDataVoUserList.stream().map(TopicDataVo::getCount).toList();
        TopicTrendVo topicTrendVo = new TopicTrendVo();
        topicTrendVo.setDateList(dateList);
        topicTrendVo.setCountTopicList(countTopicList);
        topicTrendVo.setCountUserList(countUserList);

        return topicTrendVo;
    }

    /**
     * 用户趋势图
     *
     * @return
     */
    public SysUserTrentVo userTrend() {
        /// 获取用户数据
        List<TopicDataVo> topicDataVoList = securityFeignClient.countUserDay7();
        // 获取日期数据
        List<String> dateList = topicDataVoList.stream().map(TopicDataVo::getDate).toList();
        List<Integer> countList = topicDataVoList.stream().map(TopicDataVo::getCount).toList();
        SysUserTrentVo sysUserTrentVo = new SysUserTrentVo();
        sysUserTrentVo.setDateList(dateList);
        sysUserTrentVo.setCountList(countList);
        return sysUserTrentVo;
    }

    /**
     * AI趋势图
     *
     * @return
     */
    public AiTrendVo aiTrend() {
        List<TopicDataVo> topicDataVoList = aiFeignClient.countAiDay7();
        List<String> dateList = topicDataVoList.stream().map(TopicDataVo::getDate).toList();
        List<Integer> countList = topicDataVoList.stream().map(TopicDataVo::getCount).toList();
        AiTrendVo aiTrendVo = new AiTrendVo();
        aiTrendVo.setDateList(dateList);
        aiTrendVo.setCountList(countList);
        return aiTrendVo;
    }


    /**
     * 用户首页左侧顶部系统数据
     *
     * @return
     */
    public Map<String, Object> userHomeData() {
        // 封装返回数据
        Map<String, Object> map = new HashMap<>();
        Long currentId = SecurityUtils.getCurrentId();
        String role = SecurityUtils.getCurrentRole();
        String currentName = SecurityUtils.getCurrentName();

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 1.用户已刷题次数
        Long topicFrequencyCount = topicRecordMapper.countTopicUserRecord(currentId);

        // 2.用户昨日和今日刷题次数
        Long aLong = topicRecordMapper.countTopicFrequency(today);
        Long aLong1 = topicRecordMapper.countTopicFrequency(yesterday);
        if (aLong == null) {
            aLong = 0L;
        }
        if (aLong1 == null) {
            aLong1 = 0L;
        }
        // 计算差值（今天 - 昨天）
        long topicFrequencyGrowthRate = aLong - aLong1;

        // 3.刷题次数排名
        // 统计用户排名
        Long rank = topicRecordMapper.getRank(currentId);

        // 4.昨日排名和今日排名
        long todayRank = topicRecordMapper.getDateRank(currentId, today) == null ? 0L : topicRecordMapper.getDateRank(currentId, today);
        long yesterdayRank = topicRecordMapper.getDateRank(currentId, yesterday) == null ? 0L : topicRecordMapper.getDateRank(currentId, yesterday);

        // 计算差值
        long rankGrowthRate = todayRank - yesterdayRank;

        // 5.已刷题目
        // 统计用户已刷总数
        Long totalTopicRecordCountSize = null;
        LambdaQueryWrapper<TopicRecord> topicRecordLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        topicRecordLambdaQueryWrapper1.eq(TopicRecord::getUserId, currentId);
        topicRecordLambdaQueryWrapper1.groupBy(TopicRecord::getTopicId);
        List<TopicRecord> totalTopicRecordCount = topicRecordMapper.selectList(topicRecordLambdaQueryWrapper1);
        if (CollectionUtils.isEmpty(totalTopicRecordCount)) {
            totalTopicRecordCountSize = 0L;
        } else {
            totalTopicRecordCountSize = (long) totalTopicRecordCount.size();
        }

        // 6.总共题目数量
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
            topicLambdaQueryWrapper.in(Topic::getCreateBy, "admin", currentName);
            totalTopicCount = topicMapper.selectCount(topicLambdaQueryWrapper);
        }

        // 7.用户ai总次数
        Long aiCount = aiFeignClient.countAi(currentId);

        // 8.最长连续刷题次数
        Long maxConsecutiveCount = topicRecordMapper.selectMaximumCount(currentId);

        // 9.最近连续刷题次数
        Long recentConsecutiveCount = topicRecordMapper.selectRecentConsecutiveCount(currentId);

        map.put("topicFrequencyCount", topicFrequencyCount);
        map.put("topicFrequencyGrowthRate", topicFrequencyGrowthRate);
        map.put("rank", rank);
        map.put("rankGrowthRate", rankGrowthRate);
        map.put("totalTopicRecordCountSize", totalTopicRecordCountSize);
        map.put("totalTopicCount", totalTopicCount);
        map.put("aiCount", aiCount == null ? 0L : aiCount);
        map.put("maxConsecutiveCount", maxConsecutiveCount == null ? 0L : maxConsecutiveCount);
        map.put("recentConsecutiveCount", recentConsecutiveCount == null ? 0L : recentConsecutiveCount);

        return map;
    }

    /**
     * 查询用户分类数据
     *
     * @return
     */
    public List<TopicCategoryUserDataVo> userHomeCategory() {
        // 封装返回数据
        List<TopicCategoryUserDataVo> topicCategoryUserDataVos = new ArrayList<>();
        Long currentId = SecurityUtils.getCurrentId();
        String role = SecurityUtils.getCurrentRole();
        String currentName = SecurityUtils.getCurrentName();
        // 1.查询所有的分类
        LambdaQueryWrapper<TopicCategory> topicCategoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (role.equals("user")) {
            // 用户直接查询系统数量
            topicCategoryLambdaQueryWrapper.eq(TopicCategory::getCreateBy, "admin");
            topicCategoryLambdaQueryWrapper.eq(TopicCategory::getStatus, StatusEnums.NORMAL.getCode());
        } else {
            // 管理员和会员可以查自己的
            topicCategoryLambdaQueryWrapper.eq(TopicCategory::getStatus, StatusEnums.NORMAL.getCode());
            topicCategoryLambdaQueryWrapper.in(TopicCategory::getCreateBy, "admin", currentName);
        }
        // 查询分类
        List<TopicCategory> topicCategories = topicCategoryMapper.selectList(topicCategoryLambdaQueryWrapper);
        // 校验
        if (CollectionUtils.isEmpty(topicCategories)) {
            return null;
        }
        // 遍历分类
        for (TopicCategory topicCategory : topicCategories) {
            // 2.查询专题下有多少个题目
            // 封装返回数据
            TopicCategoryUserDataVo topicCategoryUserDataVo = new TopicCategoryUserDataVo();
            // 根据分类id查询专题分类表
            LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getCategoryId, topicCategory.getId());
            List<TopicCategorySubject> topicCategorySubjects = topicCategorySubjectMapper.selectList(topicCategorySubjectLambdaQueryWrapper);
            // 封装分类名称
            topicCategoryUserDataVo.setCategoryName(topicCategory.getCategoryName());
            if (CollectionUtils.isEmpty(topicCategorySubjects)) {
                continue;
            }
            long totalCount = 0L;
            long count = 0L;
            // 有专题id
            for (TopicCategorySubject topicCategorySubject : topicCategorySubjects) {
                // 根据专题id查询专题表
                LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectLambdaQueryWrapper.eq(TopicSubject::getId, topicCategorySubject.getSubjectId());
                topicSubjectLambdaQueryWrapper.eq(TopicSubject::getStatus, StatusEnums.NORMAL.getCode());
                TopicSubject topicSubject = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                if (StringUtils.isNull(topicSubject)) {
                    continue;
                }
                // 查询题目
                TopicSubjectDetailAndTopicVo topicSubjectDetailAndTopicVo = topicSubjectService.subjectDetail(topicSubject.getId());
                if (topicSubjectDetailAndTopicVo != null) {
                    if (CollectionUtils.isNotEmpty(topicSubjectDetailAndTopicVo.getTopicNameVos())) {
                        totalCount += topicSubjectDetailAndTopicVo.getTopicNameVos().size();
                        // 提出该专题下所有的题目id
                        List<Long> topicSubjectIds = topicSubjectDetailAndTopicVo.getTopicNameVos().stream().map(TopicNameVo::getId).toList();
                        // 3.查询用户刷了这个分类下的多少题
                        // 查询用户刷的所有题目id
                        LambdaQueryWrapper<TopicRecord> topicRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        topicRecordLambdaQueryWrapper.eq(TopicRecord::getUserId, currentId);
                        topicRecordLambdaQueryWrapper.eq(TopicRecord::getSubjectId, topicCategorySubject.getSubjectId());
                        topicRecordLambdaQueryWrapper.groupBy(TopicRecord::getTopicId);
                        List<TopicRecord> topicRecords = topicRecordMapper.selectList(topicRecordLambdaQueryWrapper);
                        if (CollectionUtils.isNotEmpty(topicRecords)) {
                            // 提取所有的题目id
                            List<Long> topicIds = topicRecords.stream().map(TopicRecord::getTopicId).toList();
                            if (CollectionUtils.isNotEmpty(topicIds)) {
                                // 判断该专题下有多少个题目被刷过
                                // 将 List 转换为 Set 以提高查找效率
                                Set<Long> topicSubjectIdSet = new HashSet<>(topicSubjectIds);
                                // 计算在 topicIds 中也出现在 topicSubjectIds 中的元素数量
                                long matchingCount = topicIds.stream()
                                        .filter(topicSubjectIdSet::contains)
                                        .count();
                                if (matchingCount != 0) {
                                    count += matchingCount;
                                }
                            }
                        }
                    }
                }
            }
            topicCategoryUserDataVo.setTotalCount(totalCount);
            topicCategoryUserDataVo.setCount(count);
            topicCategoryUserDataVos.add(topicCategoryUserDataVo);
        }
        return topicCategoryUserDataVos;
    }

    /**
     * 根据年份统计每日用户刷题次数
     *
     * @param date
     * @return
     */
    public List<TopicDataVo> userTopicDateCount(String date) {
        // 获取当前登录用户id
        Long currentId = SecurityUtils.getCurrentId();
        // 开始查询
        return topicRecordMapper.userTopicDateCount(date, currentId);
    }

    /**
     * 查询每日必刷
     *
     * @return
     */
    public List<TopicTodayVo> topicTodayVo() {
        // 查询公共的
        LambdaQueryWrapper<TopicDailyStaging> topicDailyStagingLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicDailyStagingLambdaQueryWrapper.eq(TopicDailyStaging::getIsPublic, 1);
        List<TopicDailyStaging> topicDailyStagings = topicDailyStagingMapper.selectList(topicDailyStagingLambdaQueryWrapper);
        // 查询用户的
        // 获取当前登录用户id
        Long currentId = SecurityUtils.getCurrentId();
        // 查询用户刷过的题目
        LambdaQueryWrapper<TopicDailyStaging> topicDailyStagingLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        topicDailyStagingLambdaQueryWrapper1.eq(TopicDailyStaging::getUserId, currentId);
        topicDailyStagingLambdaQueryWrapper1.eq(TopicDailyStaging::getIsPublic, 2);
        List<TopicDailyStaging> topicDailyStagings1 = topicDailyStagingMapper.selectList(topicDailyStagingLambdaQueryWrapper1);
        // 合并一起
        topicDailyStagings.addAll(topicDailyStagings1);
        // 遍历封装返回数据
        return topicDailyStagings.stream().map(topicDailyStaging -> {
            // 封装返回数据
            TopicTodayVo topicTodayVo = new TopicTodayVo();
            // 根据题目id查询题目
            Topic topic = topicMapper.selectById(topicDailyStaging.getTopicId());
            // 根据题目id查询题目标签题目关系表
            LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topicDailyStaging.getTopicId());
            List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
            if (CollectionUtils.isEmpty(topicLabelTopics)) {
                return null;
            }
            // 有标签
            // 收集所有的id
            List<Long> labelIds = topicLabelTopics.stream().map(TopicLabelTopic::getLabelId).toList();
            // 存放标签名称
            List<String> labelNames = new ArrayList<>();
            // 查询
            for (Long labelId : labelIds) {
                LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLabelLambdaQueryWrapper.eq(TopicLabel::getId, labelId);
                topicLabelLambdaQueryWrapper.eq(TopicLabel::getStatus, StatusEnums.NORMAL.getCode());
                topicLabelLambdaQueryWrapper.orderByDesc(TopicLabel::getCreateTime);
                TopicLabel topicLabel = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
                if (topicLabel != null) {
                    labelNames.add(topicLabel.getLabelName());
                }
            }
            // 查询是否刷的关联表
            LambdaQueryWrapper<TopicDailyBrush> topicDailyBrushLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicDailyBrushLambdaQueryWrapper.eq(TopicDailyBrush::getUserId, currentId);
            topicDailyBrushLambdaQueryWrapper.eq(TopicDailyBrush::getDailyId, topicDailyStaging.getId());
            if (topicDailyBrushMapper.selectOne(topicDailyBrushLambdaQueryWrapper) != null) {
                topicTodayVo.setStatus(1);
            } else {
                topicTodayVo.setStatus(0);
            }
            topicTodayVo.setTopicName(topic.getTopicName());
            topicTodayVo.setLabelNames(labelNames);
            topicTodayVo.setId(topicDailyStaging.getId());
            topicTodayVo.setSubjectId(topicDailyStaging.getSubjectId());
            topicTodayVo.setTopicId(topicDailyStaging.getTopicId());
            return topicTodayVo;
        }).toList();
    }

    /**
     * 刷每日题
     *
     * @param id
     */
    public void flushTopic(Long id) {
        // 查询
        LambdaQueryWrapper<TopicDailyBrush> topicDailyBrushLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicDailyBrushLambdaQueryWrapper.eq(TopicDailyBrush::getDailyId, id);
        topicDailyBrushLambdaQueryWrapper.eq(TopicDailyBrush::getUserId, SecurityUtils.getCurrentId());
        TopicDailyBrush topicDailyBrush = topicDailyBrushMapper.selectOne(topicDailyBrushLambdaQueryWrapper);
        if (topicDailyBrush == null) {
            topicDailyBrush = new TopicDailyBrush();
            topicDailyBrush.setDailyId(id);
            topicDailyBrush.setUserId(SecurityUtils.getCurrentId());
            topicDailyBrushMapper.insert(topicDailyBrush);
        }
    }
}
