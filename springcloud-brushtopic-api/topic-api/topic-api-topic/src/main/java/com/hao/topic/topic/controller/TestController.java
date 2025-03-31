package com.hao.topic.topic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author: Hao
 * Date: 2025/3/31 22:29
 */
@RestController
@RequestMapping("/user")
public class TestController {

    @GetMapping("/admin")
    public String test() {
        return "访问成功";
    }

    @PostMapping("/login")
    public String login() {
        return "登录成功";
    }
}
