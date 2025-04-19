package com.hao.topic.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.ai.mapper.AiUserMapper;
import com.hao.topic.ai.service.ManageService;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.ai.AiUserDto;
import com.hao.topic.model.entity.ai.AiUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/18 22:33
 */
@Service
@AllArgsConstructor
public class ManageServiceImpl implements ManageService {
    private AiUserMapper aiUserMapper;

    /**
     * 查询用户ai列表
     *
     * @param aiUserDto
     * @return
     */
    public Map<String, Object> list(AiUserDto aiUserDto) {
        // 设置分页参数
        Page<AiUser> aiUserPage = new Page<>(aiUserDto.getPageNum(), aiUserDto.getPageSize());
        // 设置分页条件
        LambdaQueryWrapper<AiUser> aiUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(aiUserDto.getAccount())) {
            aiUserLambdaQueryWrapper.like(AiUser::getAccount, aiUserDto.getAccount());
        }
        aiUserLambdaQueryWrapper.orderByDesc(AiUser::getRecentlyUsedTime);
        // 开始查询
        Page<AiUser> aiUserPageDb = aiUserMapper.selectPage(aiUserPage, aiUserLambdaQueryWrapper);

        return Map.of(
                "total", aiUserPageDb.getTotal(),
                "rows", aiUserPageDb.getRecords()
        );
    }
}
