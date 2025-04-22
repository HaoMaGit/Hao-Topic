package com.hao.topic.model.vo.ai;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/22 11:59
 */
@Data
public class AiHistoryListVo {
    // 日期年月日形式
    private String date;
    // 标题和对话id
    List<AiHistoryVo> aiHistoryVos;
}
