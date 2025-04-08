package com.hao.topic.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充时间
 */
@Component // 确保Spring能够扫描到该类
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 自动填充时间
     *
     * @param metaObject
     */
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        // 如果有其他字段需要在插入时自动填充，也可以在这里设置  
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        // 如果有其他字段需要在更新时自动填充，也可以在这里设置  
    }
}