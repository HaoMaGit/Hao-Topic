package com.hao.topic.model.dto.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hao.topic.model.entity.BaseEntity;
import com.hao.topic.model.entity.system.SysUser;
import lombok.Data;

import java.util.List;

/**
 * Description: 用户列表查询参数
 * Author: Hao
 * Date: 2025/4/10 21:49
 */
@Data
public class SysUserListDto {
    private String account;
    private String roleName;
    private String beginCreateTime;
    private String endCreateTime;
    private String beginMemberTime;
    private String endMemberTime;
    private Integer pageNum;
    private Integer pageSize;
}
