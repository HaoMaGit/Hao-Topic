package com.hao.topic.topic.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * Author: Hao
 * Date: 2025/3/31 22:29
 */
@RestController
@RequestMapping("/system/user")
@PreAuthorize("hasAuthority('admin')")
public class TestController {

    @GetMapping("/admin")
    public String test1() {
        return "管理员访问成功";
    }

    @GetMapping("/member")
    public String test2() {
        return "会员访问成功";
    }

    @GetMapping("/user")
    public String test3() {
        return "用户访问成功";
    }
}
