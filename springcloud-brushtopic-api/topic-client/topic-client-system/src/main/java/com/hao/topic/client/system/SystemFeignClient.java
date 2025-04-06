package com.hao.topic.client.system;

import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.vo.system.SysMenuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Description: 表示：已被【认证服务】调用
 * Author: Hao
 * Date: 2025/4/5 15:47
 */
@FeignClient(name = "service-system", configuration = TokenInterceptor.class)
public interface SystemFeignClient {
    /**
     * 根据角色id查询菜单信息
     *
     * @param roleId
     * @return
     */
    @GetMapping("/system/menu/userMenu/{roleId}")
    public List<SysMenuVo> userMenu(@PathVariable Long roleId);
}
