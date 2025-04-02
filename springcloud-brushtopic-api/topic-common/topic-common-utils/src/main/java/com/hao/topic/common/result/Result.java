package com.haoma.topic.service.result;

import com.haoma.topic.service.enums.ResultCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description: 统一返回结果类
 */
@Data
public class Result<T> implements Serializable {
    private Integer code; // 编码
    private String message; // 错误信息
    private T data; // 数据


    /**
     * 构建返回结果
     *
     * @param data
     * @param resultCodeEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        // 创建返回对象
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }


    /**
     * 成功不带参数的方法
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 成功带数据的方法
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T object) {
        return build(object, ResultCodeEnum.SUCCESS);
    }

    /**
     * 失败不带参数的方法
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail() {
        return build(null, ResultCodeEnum.FAIL);
    }

    /**
     * 失败带参数的方法
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        return build(null, resultCodeEnum);
    }

    /**
     * 失败带消息的异常的方法
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(String message) {
        // 创建返回对象
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setData(null);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败异常的方法
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(String message, Integer code) {
        // 创建返回对象
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(null);
        result.setMessage(message);
        return result;
    }
}
