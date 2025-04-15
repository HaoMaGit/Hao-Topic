package com.hao.topic.model.dto.topic;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/15 21:12
 */
@Data
public class TopicLabelDto {
    @NotBlank(message = "标签名称不能为空")
    private String labelName;
    private Long id;
}
