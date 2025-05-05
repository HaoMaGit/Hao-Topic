package com.hao.topic.model.vo.topic;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/5 11:37
 */
@Data
public class TopicUserRankVo {
    private String avatar;
    private String nickname;
    private Long scope;
    private Long userId;
    private Long rank;
    private String role;
    private String topicTime;
}
