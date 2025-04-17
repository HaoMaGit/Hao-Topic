package com.hao.topic.model.dto.topic;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/16 21:46
 */
@Data
public class TopicDto {
    private Long id;
    @NotBlank(message = "题目名称不能为空")
    private String topicName;
    @NotBlank(message = "题目答案不能为空")
    private String answer;
    private Integer sorted;
    private Integer isEveryday;
    private Integer isMember;
    @NotBlank(message = "题目关联专题不能为空")
    private Long subjectId;
    @NotBlank(message = "题目关联标签不能为空")
    private List<Long> labelIds;
}
