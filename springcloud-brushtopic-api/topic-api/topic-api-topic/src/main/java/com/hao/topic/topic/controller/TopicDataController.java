package com.hao.topic.topic.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.vo.system.SysUserTrentVo;
import com.hao.topic.model.vo.topic.*;
import com.hao.topic.topic.mapper.TopicRecordMapper;
import com.hao.topic.topic.service.TopicDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Description: 用于统计数据相关的控制器
 * Author: Hao
 * Date: 2025/5/4 22:26
 */
@RestController
@RequestMapping("/topic/data")
@AllArgsConstructor
public class TopicDataController {
    private final TopicDataService topicDataService;

    /**
     * 统计h5首页刷题数据以及用户数量和排名
     */
    @GetMapping("/webHomeCount")
    public Result webTopicCount() {
        Map<String, Object> map = topicDataService.webHomeCount();
        return Result.success(map);
    }

    /**
     * 查询排行榜
     */
    @GetMapping("/rank/{type}")
    public Result<List<TopicUserRankVo>> rank(@PathVariable Integer type) {
        List<TopicUserRankVo> topicUserRankVos = topicDataService.rank(type);
        return Result.success(topicUserRankVos);
    }

    /**
     * 获取当前用户排名信息
     */
    @GetMapping("/userRank/{type}")
    public Result<TopicUserRankVo> userRank(@PathVariable Integer type) {
        TopicUserRankVo topicUserRankVo = topicDataService.userRank(type);
        return Result.success(topicUserRankVo);
    }

    /**
     * TODO 查询每日必刷
     */


    /**
     * 管理员首页左侧顶部系统数据
     */
    @GetMapping("/adminHomeCount")
    public Result<Map<String, Object>> adminHomeData() {
        Map<String, Object> map = topicDataService.adminHomeCount();
        return Result.success(map);
    }

    /**
     * 管理员首页右侧分类数据
     */
    @GetMapping("/adminHomeCategory")
    public Result<List<TopicCategoryDataVo>> adminHomeCategory() {
        List<TopicCategoryDataVo> list = topicDataService.adminHomeCategory();
        return Result.success(list);
    }

    /**
     * 刷题题目和刷题人数趋势图
     */
    @GetMapping("/topicTrend")
    public Result<TopicTrendVo> topicTrend() {
        TopicTrendVo topicTrend = topicDataService.topicTrend();
        return Result.success(topicTrend);
    }


    /**
     * 用户增长趋势图
     */
    @GetMapping("/userTrend")
    public Result<SysUserTrentVo> userTrend() {
        SysUserTrentVo SysUserTrentVo = topicDataService.userTrend();
        return Result.success(SysUserTrentVo);
    }


    /**
     * TODO AI调用次数趋势图
     */
}
