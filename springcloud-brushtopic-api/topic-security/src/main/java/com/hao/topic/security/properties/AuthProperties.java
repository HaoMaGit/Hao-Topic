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
public class IgnoreWhiteProperties {
    /**
     * 放行白名单配置不校验此处的白名单
     */
    @Value("${security.ignore.whites}")
    private List<String> whites = new ArrayList<>();
    /**
     * 登录超时时间
     */
    @Value("${security.login.timeout}")
    private int timeout;
    /**
     * 记住我时间
     */
    @Value("${security.login.rememberMe}")
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

    public List<String> getWhites() {
        return whites;
    }

    public void setWhites(List<String> whites) {
        this.whites = whites;
    }
}
