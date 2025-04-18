package com.hao.topic.ai.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hao.topic.ai.service.ManageService;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.model.dto.ai.AiUserDto;
import com.hao.topic.model.dto.system.SysUserListDto;
import com.hao.topic.model.vo.ai.AiUserVo;
import com.hao.topic.model.vo.system.SysUserListVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final SecurityFeignClient securityFeignClient;

    /**
     * 查询用户ai列表
     *
     * @param aiUserDto
     * @return
     */
    public Map<String, Object> list(AiUserDto aiUserDto) {
        // 调用远程服务获取用户列表
        Map<String, Object> list = securityFeignClient.manageList(aiUserDto);
        // TODO 解析数据查询最近使用时间也就是查询ai日志表
        return list;
    }
}
