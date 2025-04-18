package com.hao.topic.ai.controller;

import com.hao.topic.ai.service.ManageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 用户ai管理
 * Author: Hao
 * Date: 2025/4/18 22:29
 */
@RestController
@RequestMapping("ai/manage")
@AllArgsConstructor
public class ManageController {

    private final ManageService manageService;
}
