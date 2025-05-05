package com.hao.topic.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.topic.model.entity.ai.AiRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/5 22:14
 */
@Mapper
public interface AiRecordMapper extends BaseMapper<AiRecord> {
    Long countAiFrequency(String date);
}
