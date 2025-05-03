package com.hao.topic.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.dto.system.SysNoticeDto;
import com.hao.topic.model.entity.system.SysNotice;
import com.hao.topic.system.constant.NoticeConstant;
import com.hao.topic.system.enums.NoticeEnums;
import com.hao.topic.system.mapper.SysNoticeMapper;
import com.hao.topic.system.service.SysNoticeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
