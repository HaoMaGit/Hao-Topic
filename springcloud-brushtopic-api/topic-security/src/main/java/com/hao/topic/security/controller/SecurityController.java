package com.hao.topic.security.controller;

import com.hao.topic.common.constant.ExceptionConstant;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.system.SysUserDto;
import com.hao.topic.model.dto.system.SysUserListDto;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.excel.sytem.SysUserExcel;
import com.hao.topic.model.excel.sytem.SysUserExcelExport;
import com.hao.topic.model.vo.system.UserInfoVo;
import com.hao.topic.security.dto.LoginRequestDto;
import com.hao.topic.security.dto.LoginTypeDto;
import com.hao.topic.security.dto.UserDto;
import com.hao.topic.security.handle.AuthenticationSuccessHandler;
import com.hao.topic.security.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/security/user")
@Slf4j
public class SecurityController {

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysUserService sysUserService;


    /**
     * 登录接口
     *
     * @param exchange
     * @param loginRequest
     * @return
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> login(ServerWebExchange exchange, @Validated @RequestBody LoginRequestDto loginRequest) {
        // 查询redis校验图片验证码
        String s = stringRedisTemplate.opsForValue().get(loginRequest.getCode());
        if (s == null || !s.equals(loginRequest.getCode())) {
            log.error("验证码错误");
            return Mono.error(new RuntimeException(ExceptionConstant.CODE_ERROR));
        }

        exchange.getAttributes().put("remember", loginRequest.getRemember());

        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ))
                .flatMap(authentication -> {
                    List<WebFilter> filters = new ArrayList<>();
                    DefaultWebFilterChain filterChain = new DefaultWebFilterChain(exchange1 -> Mono.empty(), filters);
                    WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, filterChain);

                    // 直接返回认证成功处理器的结果
                    return authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication);
                })
                .onErrorResume(e -> {
                    log.error("认证失败", e);
                    return Mono.error(e);
                });
    }


    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("/userInfo")
    public Result<UserInfoVo> getUserInfo(String token) {
        UserInfoVo userInfoVo = sysUserService.getUserInfo(token);
        return Result.success(userInfoVo);
    }


    /**
     * 查询用户列表
     *
     * @param sysUserListDto
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list(SysUserListDto sysUserListDto) {
        return sysUserService.userList(sysUserListDto);
    }

    /**
     * 添加用户
     *
     * @param sysUserDto
     */
    @PostMapping("/add")
    void add(@RequestBody SysUserDto sysUserDto) {
        sysUserService.add(sysUserDto);
    }

    /**
     * 修改用户
     *
     * @param sysUserDto
     */
    @PostMapping("/update")
    void update(@RequestBody SysUserDto sysUserDto) {
        sysUserService.update(sysUserDto);
    }

    /**
     * 删除用户
     *
     * @param ids
     */
    @DeleteMapping("/delete/{ids}")
    void delete(@PathVariable Long[] ids) {
        sysUserService.delete(ids);
    }

    /**
     * 查询用户角色关系表
     *
     * @param roleId
     */
    @GetMapping("/getByRoleId/{roleId}")
    Boolean getByRoleId(@PathVariable Long roleId) {
        return sysUserService.getByRoleId(roleId);
    }

    /**
     * 获取excelVo数据
     *
     * @param sysUserListDto
     * @param ids
     * @return
     */
    @RequestMapping("/export/{ids}")
    List<SysUserExcelExport> getExcelVo(SysUserListDto sysUserListDto, @PathVariable Long[] ids) {
        return sysUserService.getExcelVo(sysUserListDto, ids);
    }

    /**
     * 将excel数据插入到数据库中
     *
     * @param excelVoList
     * @param updateSupport
     */
    @PostMapping("/import")
    String importExcel(@RequestBody List<SysUserExcel> excelVoList, @RequestParam("updateSupport") Boolean updateSupport) {
        return sysUserService.importExcel(excelVoList, updateSupport);
    }

    /**
     * 获取用户详细信息
     */
    @GetMapping("/info/{id}")
    public SysUser getUserInfo(@PathVariable Long id) {
        return sysUserService.getById(id);
    }


    /**
     * 保存头像
     *
     * @param userDto
     * @return
     */
    @PutMapping("/avatar")
    public Result saveAvatar(@RequestBody UserDto userDto) {
        // 保存头像
        sysUserService.saveAvatar(userDto);
        return Result.success();
    }

    /**
     * 修改用户昵称和邮箱
     */
    @PutMapping("/updateNicknameAndEmail")
    public Result updateNicknameAndEmail(@RequestBody UserDto userDto) {
        // 修改账户和邮箱
        sysUserService.updateNicknameAndEmail(userDto);
        return Result.success();
    }

    /**
     * 修改用户密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody UserDto userDto) {
        // 修改密码
        sysUserService.updatePassword(userDto);
        return Result.success();
    }


    /**
     * h5端登录
     */
    @PostMapping("loginType")
    public Mono<Result<Map<String, Object>>> loginType(@RequestBody @Validated LoginTypeDto loginTypeDto) {
        return sysUserService.loginType(loginTypeDto)
                .map(Result::success)
                .defaultIfEmpty(Result.fail("登录失败"));
    }

    /**
     * 发送qq邮箱验证码
     */
    @GetMapping("/sendEmail")
    public Result sendEmail(String email) {
        // 发送qq邮箱验证码
        sysUserService.sendVerificationEmail(email);
        return Result.success();
    }

}