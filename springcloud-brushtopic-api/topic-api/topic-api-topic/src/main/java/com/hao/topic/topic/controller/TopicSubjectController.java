package com.hao.topic.topic.controller;

import com.hao.topic.topic.service.TopicSubjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 题目专题控制器
 * Author: Hao
 * Date: 2025/4/14 10:37
 */
@RestController
@RequestMapping("/topic/subject")
@AllArgsConstructor
public class TopicSubjectController {

    private final TopicSubjectService topicSubjectService;

}
