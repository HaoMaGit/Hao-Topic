package com.hao.topic.model.vo.topic;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/6 10:16
 */
@Data
public class TopicTrendVo {
    private List<String> dateList;
    private List<Integer> countUserList;
    private List<Integer> countTopicList;

}
