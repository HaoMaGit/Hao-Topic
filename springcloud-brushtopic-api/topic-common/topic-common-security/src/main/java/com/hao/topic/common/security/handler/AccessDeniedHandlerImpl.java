package com.hao.topic.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义的访问拒绝处理器和认证入口点处理器。
 * 实现了AccessDeniedHandler和AuthenticationEntryPoint接口。
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler, AuthenticationEntryPoint {

    /**
     * 处理访问被拒绝的异常。
     * 当用户尝试访问没有权限的资源时，将调用此方法。
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param e        访问被拒绝异常对象
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生Servlet异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        // 记录访问被拒绝的日志信息
        log.error("认证失败：{}", e.getMessage());

        // 设置响应内容类型为JSON，并设置字符编码为UTF-8
        response.setContentType("application/json;charset=UTF-8");

        // 设置HTTP响应状态码为401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 创建ObjectMapper对象，用于将Java对象转换为JSON格式
        ObjectMapper mapper = new ObjectMapper();

        // 将访问被拒绝的结果以JSON格式写入响应输出流
        mapper.writeValue(response.getOutputStream(),
                Result.fail(ResultCodeEnum.LOGIN_ERROR));
    }

    /**
     * 处理认证失败的异常。
     * 当用户尝试访问需要认证的资源但未通过认证时，将调用此方法。
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param e        认证异常对象
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生Servlet异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        // 记录认证失败的日志信息
        log.error("授权失败：{}", e.getMessage());

        // 设置响应内容类型为JSON，并设置字符编码为UTF-8
        response.setContentType("application/json;charset=UTF-8");

        // 设置HTTP响应状态码为403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 创建ObjectMapper对象，用于将Java对象转换为JSON格式
        ObjectMapper mapper = new ObjectMapper();

        // 将认证失败的结果以JSON格式写入响应输出流
        mapper.writeValue(response.getOutputStream(),
                Result.fail(ResultCodeEnum.LOGIN_ERROR_SECURITY));
    }
}
