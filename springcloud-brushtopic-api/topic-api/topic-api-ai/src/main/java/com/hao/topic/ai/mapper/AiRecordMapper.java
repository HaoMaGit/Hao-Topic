package com.hao.topic.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.topic.model.entity.ai.AiRecord;
import com.hao.topic.model.vo.topic.TopicDataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/5 22:14
 */
@Mapper
public interface AiRecordMapper extends BaseMapper<AiRecord> {
    Long countAiFrequency(String date);

    List<TopicDataVo> countAiDay7();

}
