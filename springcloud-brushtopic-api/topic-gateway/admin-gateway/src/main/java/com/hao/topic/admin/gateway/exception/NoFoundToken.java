package com.hao.topic.admin.gateway.exception;

/**
 * 自定义异常类，用于表示在请求中未找到token的情况
 */
public class NoFoundToken extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 构造函数，接受一个异常消息
     *
     * @param msg 异常消息
     */
    public NoFoundToken(String msg) {
        super(msg);
    }

    /**
     * 获取异常消息
     *
     * @return 异常消息
     */
    public String getMsg() {
        return getMessage();
    }
}
