package com.hao.topic.system.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.entity.system.SysFeedback;
import com.hao.topic.model.vo.system.SysFeedbackUserVo;
import com.hao.topic.model.vo.system.SysFeedbackVo;
import com.hao.topic.system.mapper.SysFeedbackMapper;
import com.hao.topic.system.service.SysFeedbackService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询反馈列表
     *
     * @param sysFeedback
     * @return
     */
    public Map<String, Object> list(SysFeedback sysFeedback) {
        // 设置分页参数
        Page<SysFeedback> page = new Page<>(sysFeedback.getPageNum(), sysFeedback.getPageSize());
        // 设置分页条件
        LambdaQueryWrapper<SysFeedback> sysFeedbackLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (sysFeedback.getStatus() != null) {
            sysFeedbackLambdaQueryWrapper.eq(SysFeedback::getStatus, sysFeedback.getStatus());
        }
        if (!StringUtils.isEmpty(sysFeedback.getReplyAccount())) {
            sysFeedbackLambdaQueryWrapper.like(SysFeedback::getReplyAccount, sysFeedback.getReplyAccount());
        }
        if (!StringUtils.isEmpty(sysFeedback.getAccount())) {
            sysFeedbackLambdaQueryWrapper.like(SysFeedback::getAccount, sysFeedback.getAccount());
        }
        // 开始分页查询
        Page<SysFeedback> selectedPage = sysFeedbackMapper.selectPage(page, sysFeedbackLambdaQueryWrapper);
        // 封装返回数据
        return Map.of(
                "total", selectedPage.getTotal(),
                "rows", selectedPage.getRecords().stream().map(
                        sysFeedback1 -> {
                            SysFeedbackVo sysFeedbackVo = new SysFeedbackVo();
                            BeanUtils.copyProperties(sysFeedback1, sysFeedbackVo);
                            return sysFeedbackVo;
                        }
                )
        );
    }

    /**
     * h5查询反馈列表
     *
     * @return
     */
    public List<SysFeedbackUserVo> feedback() {
        LambdaQueryWrapper<SysFeedback> sysFeedbackLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysFeedbackLambdaQueryWrapper.eq(SysFeedback::getUserId, SecurityUtils.getCurrentId());
        sysFeedbackLambdaQueryWrapper.orderByDesc(SysFeedback::getCreateTime);
        List<SysFeedback> sysFeedbacks = sysFeedbackMapper.selectList(sysFeedbackLambdaQueryWrapper);
        return sysFeedbacks.stream().map(sysFeedback -> {
            SysFeedbackUserVo sysFeedbackUserVo = new SysFeedbackUserVo();
            BeanUtils.copyProperties(sysFeedback, sysFeedbackUserVo);
            return sysFeedbackUserVo;
        }).toList();
    }
}
