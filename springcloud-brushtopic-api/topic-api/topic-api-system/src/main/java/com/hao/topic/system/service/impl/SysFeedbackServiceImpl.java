package com.hao.topic.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.entity.system.SysFeedback;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.system.mapper.SysFeedbackMapper;
import com.hao.topic.system.mapper.SysMenuMapper;
import com.hao.topic.system.service.SysFeedbackService;
import com.hao.topic.system.service.SysMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/2 22:54
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysFeedbackServiceImpl extends ServiceImpl<SysFeedbackMapper, SysFeedback> implements SysFeedbackService {
    private final SysFeedbackMapper sysFeedbackMapper;

    /**
     * 用户发起反馈
     *
     * @param sysFeedback
     */
    public void saveFeedback(SysFeedback sysFeedback) {
        // 校验反馈内容不能为空
        if (sysFeedback.getFeedbackContent() == null || sysFeedback.getFeedbackContent().isEmpty()) {
            throw new TopicException(ResultCodeEnum.PARAM_ACCOUNT_ERROR);
        }
        // 获取用户id
        Long currentId = SecurityUtils.getCurrentId();
        String currentName = SecurityUtils.getCurrentName();
        sysFeedback.setUserId(currentId);
        sysFeedback.setAccount(currentName);
        sysFeedbackMapper.insert(sysFeedback);
    }
}
