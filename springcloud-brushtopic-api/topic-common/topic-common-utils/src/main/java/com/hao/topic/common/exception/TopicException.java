package com.hao.topic.common.exception;


import com.hao.topic.common.enums.ResultCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Description: 自定义异常
 */
@Getter
@Setter
public class TopicException extends RuntimeException {
    // 异常状态码
    private Integer code;

    // 自定义异常
    public TopicException(ResultCodeEnum resultCodeEnum) {
        // 传递异常信息
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    public TopicException(String message) {
        super(message);
        this.code = 400;
    }
}
