package com.hao.topic.security.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 读取配置白名单
 * Author: Hao
 * Date: 2025/3/31 20:54
 */
@Configuration
@ConfigurationProperties(prefix = "security.auth")
public class AuthProperties {


    /**
     * 登录超时时间
     */
    private int timeout;

    /**
     * 记住我时间
     */
    private int rememberMe;


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(int rememberMe) {
        this.rememberMe = rememberMe;
    }
}
