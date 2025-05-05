package com.hao.topic.topic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.topic.model.entity.topic.TopicRecord;
import com.hao.topic.model.vo.topic.TopicUserRankVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/4 21:59
 */
@Mapper
public interface TopicRecordMapper extends BaseMapper<TopicRecord> {
    Long getRank(Long userId);


    List<TopicUserRankVo> getCountRank(String topicTime);

    TopicUserRankVo getUserCountRank(String topicTime, Long userId);

    Long countTopicFrequency(String date);
}
