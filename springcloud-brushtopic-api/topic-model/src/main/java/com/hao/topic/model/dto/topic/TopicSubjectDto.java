package com.hao.topic.model.dto.topic;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/14 21:59
 */
@Data
public class TopicSubjectDto {
    @NotBlank(message = "题目专题名称不能为空")
    private String subjectName;

    private Long id;
    @NotBlank(message = "题目专题图片不能为空")
    private String imageUrl;
    @NotBlank(message = "题目专题描述不能为空")
    private String subjectDesc;
}
