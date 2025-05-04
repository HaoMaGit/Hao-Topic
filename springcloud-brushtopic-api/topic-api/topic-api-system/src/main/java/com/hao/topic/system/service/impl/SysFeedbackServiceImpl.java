package com.hao.topic.system.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.dto.system.SysFeedbackReplyDto;
import com.hao.topic.model.entity.system.SysFeedback;
import com.hao.topic.model.entity.system.SysNotice;
import com.hao.topic.model.vo.system.SysFeedbackUserVo;
import com.hao.topic.model.vo.system.SysFeedbackVo;
import com.hao.topic.system.enums.NoticeEnums;
import com.hao.topic.system.mapper.SysFeedbackMapper;
import com.hao.topic.system.mapper.SysNoticeMapper;
import com.hao.topic.system.service.SysFeedbackService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final SysNoticeMapper sysNoticeMapper;

    /**
     * 用户发起反馈
     *
     * @param sysFeedbackDto
     */
    @Transactional
    public void saveFeedback(SysFeedback sysFeedbackDto) {
        // 校验反馈内容不能为空
        if (StringUtils.isEmpty(sysFeedbackDto.getFeedbackContent())) {
            throw new TopicException(ResultCodeEnum.PARAM_ACCOUNT_ERROR);
        }
        // 获取用户id
        Long currentId = SecurityUtils.getCurrentId();
        String currentName = SecurityUtils.getCurrentName();
        SysFeedback sysFeedback = new SysFeedback();
        sysFeedback.setUserId(currentId);
        sysFeedback.setAccount(currentName);
        sysFeedback.setFeedbackContent(sysFeedbackDto.getFeedbackContent());
        sysFeedback.setStatus(0);
        sysFeedbackMapper.insert(sysFeedback);
        // 添加到通知表中
        SysNotice sysNotice = new SysNotice();
        sysNotice.setAccount(currentName);
        sysNotice.setUserId(currentId);
        sysNotice.setContent(sysFeedbackDto.getFeedbackContent());
        sysNotice.setStatus(sysFeedbackDto.getStatus());
        sysNoticeMapper.insert(sysNotice);
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
        sysFeedbackLambdaQueryWrapper.orderByDesc(SysFeedback::getCreateTime);
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

    /**
     * 回复内容
     *
     * @param sysFeedbackReplyDto
     */
    @Transactional
    public void reply(SysFeedbackReplyDto sysFeedbackReplyDto) {
        // 校验一下参数
        if (sysFeedbackReplyDto.getId() == null || sysFeedbackReplyDto.getReplyContent() == null || sysFeedbackReplyDto.getReplyContent().isEmpty()) {
            throw new TopicException(ResultCodeEnum.FEEDBACK_CONTENT_IS_NULL);
        }
        // 查询一下这条反馈记录
        SysFeedback sysFeedback = sysFeedbackMapper.selectById(sysFeedbackReplyDto.getId());
        if (sysFeedback == null) {
            throw new TopicException(ResultCodeEnum.FEEDBACK_NOT_EXIST);
        }
        // 反馈记录存在
        sysFeedback.setReplyContent(sysFeedbackReplyDto.getReplyContent());
        sysFeedback.setReplyAccount(SecurityUtils.getCurrentName());
        sysFeedback.setReplyTime(LocalDateTime.now());
        sysFeedback.setReplyId(SecurityUtils.getCurrentId());
        // 修改状态为已回复
        sysFeedback.setStatus(1);
        // 修改
        sysFeedbackMapper.updateById(sysFeedback);

        // 记录到通知表中通知到这个反馈用户已经回复了
        SysNotice sysNotice = new SysNotice();
        // 创建人
        sysNotice.setAccount(sysFeedback.getReplyAccount());
        sysNotice.setUserId(sysFeedback.getReplyId());
        // 通知内容
        sysNotice.setContent(sysFeedback.getReplyContent());
        sysNotice.setStatus(NoticeEnums.REPLY.getCode());
        // 接收人
        sysNotice.setRecipientsId(sysFeedback.getUserId());
        // 插入到通知表
        sysNoticeMapper.insert(sysNotice);
    }
}
