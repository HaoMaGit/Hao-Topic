package com.hao.topic.system.service.impl;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hao.topic.api.utils.constant.TimeUtils;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.dto.system.SysNoticeDto;
import com.hao.topic.model.dto.system.SysNoticeReadDto;
import com.hao.topic.model.entity.system.SysNotice;
import com.hao.topic.model.vo.system.SysNoticeVo;
import com.hao.topic.system.constant.NoticeConstant;
import com.hao.topic.system.enums.NoticeEnums;
import com.hao.topic.system.mapper.SysNoticeMapper;
import com.hao.topic.system.service.SysNoticeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/3 16:29
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysNoticeServiceImpl implements SysNoticeService {
    private final SysNoticeMapper sysNoticeMapper;

    /**
     * 记录通知
     *
     * @param sysNoticeDto
     */
    public void recordNotice(SysNoticeDto sysNoticeDto) {
        if (sysNoticeDto.getStatus() == null) {
            throw new TopicException(ResultCodeEnum.FAIL);
        }
        // 获取当前登录用户id
        Long currentId = SecurityUtils.getCurrentId();
        // 查询message
        String message = NoticeEnums.getMessageByCode(sysNoticeDto.getStatus());
        // 开始记录
        SysNotice sysNoticeDb = new SysNotice();
        sysNoticeDb.setAccount(SecurityUtils.getCurrentName());
        sysNoticeDb.setUserId(currentId);
        sysNoticeDb.setStatus(sysNoticeDto.getStatus());
        // 是否是支付通知
        if (Objects.equals(sysNoticeDto.getStatus(), NoticeEnums.MEMBER_PAY.getCode())) {
            // 判断这个人是否记录过支付了
            LambdaQueryWrapper<SysNotice> sysNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getStatus, NoticeEnums.MEMBER_PAY.getCode());
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getUserId, currentId);
            SysNotice sysNotice = sysNoticeMapper.selectOne(sysNoticeLambdaQueryWrapper);
            if (sysNotice != null) {
                // 已存在用户支付通知
                return;
            }
            // 不存在记录支付内容
            sysNoticeDb.setContent(NoticeConstant.RECHARGE_MEMBER);
        } else {
            // 不是支付
            sysNoticeDb.setContent(message);
        }
        sysNoticeMapper.insert(sysNoticeDb);
    }

    /**
     * 查询通知列表
     *
     * @return
     */
    public List<SysNoticeVo> list() {
        // 获取当前登录用户
        Long currentId = SecurityUtils.getCurrentId();
        // 获取当前登录用户角色
        String role = SecurityUtils.getCurrentRole();
        List<SysNotice> sysNoticeList = null;
        if (role.equals("admin")) {
            // 是管理员那就查全部
            LambdaQueryWrapper<SysNotice> sysNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysNoticeLambdaQueryWrapper.orderByDesc(SysNotice::getCreateTime);
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getIsRead, 0);
            sysNoticeList = sysNoticeMapper.selectList(sysNoticeLambdaQueryWrapper);
        } else {
            // 不是管理员那就查接收人是不是自己哦
            LambdaQueryWrapper<SysNotice> sysNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getRecipientsId, currentId);
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getIsRead, 0);
            sysNoticeLambdaQueryWrapper.orderByDesc(SysNotice::getCreateTime);
            // 非管理员只能接受到2回复内容
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getStatus, NoticeEnums.REPLY.getCode());
            sysNoticeList = sysNoticeMapper.selectList(sysNoticeLambdaQueryWrapper);
        }
        return sysNoticeList.stream().map(item -> {
            SysNoticeVo sysNoticeVo = new SysNoticeVo();
            BeanUtils.copyProperties(item, sysNoticeVo);
            // 处理一下时间 判断是否是当天额外处理一下
            String today = DateUtils.format(item.getCreateTime(), "yyyy-MM-dd");
            if (today.equals(DateUtils.format(new Date(), "yyyy-MM-dd"))) {
                sysNoticeVo.setTimeDesc(TimeUtils.formatTimeAgo(item.getCreateTime()));
            } else {
                // 不是当天直接返回 格式化一下
                sysNoticeVo.setTimeDesc(DateUtils.format(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            return sysNoticeVo;
        }).toList();
    }

    /**
     * 查询是否有通知
     *
     * @return
     */
    public Boolean has() {
        // 获取当前登录用户
        Long currentId = SecurityUtils.getCurrentId();
        // 获取当前登录用户角色
        String role = SecurityUtils.getCurrentRole();
        if (role.equals("admin")) {
            // 是管理员那就查全部
            LambdaQueryWrapper<SysNotice> sysNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysNoticeLambdaQueryWrapper.orderByDesc(SysNotice::getCreateTime);
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getIsRead, 0);
            Long count = sysNoticeMapper.selectCount(sysNoticeLambdaQueryWrapper);
            return count > 0;
        } else {
            // 不是管理员那就查接收人是不是自己哦
            LambdaQueryWrapper<SysNotice> sysNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getRecipientsId, currentId);
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getIsRead, 0);
            sysNoticeLambdaQueryWrapper.orderByDesc(SysNotice::getCreateTime);
            // 非管理员只能接受到2回复内容
            sysNoticeLambdaQueryWrapper.eq(SysNotice::getStatus, NoticeEnums.REPLY.getCode());
            Long count = sysNoticeMapper.selectCount(sysNoticeLambdaQueryWrapper);
            return count > 0;
        }
    }

    /**
     * 已读通知
     *
     * @param sysNoticeReadDto
     */
    public void read(SysNoticeReadDto sysNoticeReadDto) {
        if (CollectionUtils.isEmpty(sysNoticeReadDto.getIdsList())) {
            throw new TopicException(ResultCodeEnum.FAIL);
        }
        sysNoticeReadDto.getIdsList().forEach(item -> {
            SysNotice sysNotice = new SysNotice();
            sysNotice.setId(item);
            sysNotice.setIsRead(1);
            sysNoticeMapper.updateById(sysNotice);
        });
    }

    /**
     * 清空通知
     */
    public void clearNotice() {
        // 获取当前登录用户
        Long currentId = SecurityUtils.getCurrentId();
        // 查询接收人的通知
        // 是管理员那就查全部
        LambdaQueryWrapper<SysNotice> sysNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysNoticeLambdaQueryWrapper.eq(SysNotice::getIsRead, 0);
        sysNoticeLambdaQueryWrapper.eq(SysNotice::getRecipientsId, currentId);
        List<SysNotice> sysNotices = sysNoticeMapper.selectList(sysNoticeLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(sysNotices)) {
            return;
        }
        sysNotices.forEach(item -> {
            item.setIsRead(1);
            sysNoticeMapper.updateById(item);
        });
    }
}
