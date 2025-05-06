package com.hao.topic.topic.service;

import com.hao.topic.model.vo.system.SysUserTrentVo;
import com.hao.topic.model.vo.topic.TopicCategoryDataVo;
import com.hao.topic.model.vo.topic.TopicTrendVo;
import com.hao.topic.model.vo.topic.TopicUserRankVo;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/4 22:27
 */
public interface TopicDataService {
    Map<String, Object> webHomeCount();

    List<TopicUserRankVo> rank(Integer type);

    TopicUserRankVo userRank(Integer type);

    Map<String, Object> adminHomeCount();

    List<TopicCategoryDataVo> adminHomeCategory();


    TopicTrendVo topicTrend();

    SysUserTrentVo userTrend();


}
