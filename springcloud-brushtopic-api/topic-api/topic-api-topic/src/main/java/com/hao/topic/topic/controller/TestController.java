package com.hao.topic.topic.controller;

import com.hao.topic.topic.service.TopicService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * Author: Hao
 * Date: 2025/3/31 22:29
 */
@RestController
@RequestMapping("/topic/user")
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('admin')")
    public String test1() {
        return "管理员访问成功";
    }

    @GetMapping("/member")
    @PreAuthorize("hasAuthority('member')")
    public String test2() {
        return "会员访问成功";
    }

    @GetMapping("/user")
    public String test3() {
        return "用户访问成功";
    }


}
