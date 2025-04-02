package com.hao.topic.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.topic.model.entity.system.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 用户角色数据访问层
 * Author: Hao
 * Date: 2025/4/1 11:51
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
