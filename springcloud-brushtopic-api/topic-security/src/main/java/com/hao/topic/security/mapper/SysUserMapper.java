package com.hao.topic.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.topic.model.dto.system.SysUserListDto;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.vo.system.SysUserListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description: 用户数据层
 * Author: Hao
 * Date: 2025/4/1 10:56
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysUserListVo> selectUserListVo(SysUserListDto sysUserListDto);

    int countUserList(SysUserListDto sysUserListDto);
}
