package com.hao.topic.topic.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.topic.mapper.TopicRecordMapper;
import com.hao.topic.topic.service.TopicDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * TODO 查询排行榜
     */

    /**
     * TODO 查询每日必刷
     */


}
