package com.hao.topic.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    LOGIN_ERROR(401, "认证失败请重新登录"),
    LOGIN_ERROR_SECURITY(403, "未授权不能访问"),
    LOGOUT_SUCCESS(200, "退出登录成功"),
    CODE_ERROR(203, "验证码错误"),
    PASSWORD_ERROR(202, "账号或密码错误"),
    ACCOUNT_ERROR(205, "账号不存在"),
    ACCOUNT_LOCKED(206, "普通用户不能访问"),
    NO_ROLE_FAIL(99901, "用户无角色"),
    NO_MENU_FAIL(99901, "用户无菜单权限"),
    MENU_ID_NOT_EXIST(99902, "菜单不存在"),
    MENU_HAS_CHILDREN(99903, "菜单下还有子菜单"),
    DEL_ROLE_ERROR(99904, "删除角色失败"),
    ROLE_NOT_EXIST(99905, "修改角色失败"),

    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ADMIN_LOCKED(207, "登录失败请确认身份"),
    REPEAT_SUBMIT(1001, "重复提交"),
    LOGIN_AUTH_ERROR(1000, "登录认证失败请重新登录"),
    AI_HISTORY_ERROR(1001, "历史记录不存在"),
    AI_HISTORY_NOT_FOUND(1002, "历史记录不存在"),
    AI_COUNT_ERROR(1003, "使用次数不足请联系我们或晋升为我们的会员"),
    MENU_ERROR(1004, "没有权限访问"),

    LABEL_ERROR(1005, "查询题目标签失败"),
    LABEL_DEL_ERROR(1006, "题目标签删除失败"),
    LABEL_DEL_TOPIC_ERROR(1007, "该标签被题目关联"),
    LABEL_EXPORT_ERROR(1008, "标签导出失败"),
    LABEL_SAVE_ERROR(1009, "新增标签失败"),
    LABEL_UPDATE_ERROR(1010, "修改标签失败"),
    LABEL_AUDIT_ERROR(1011, "审核标签失败"),

    CATEGORY_ERROR(1012, "查询题目分类失败"),
    CATEGORY_DEL_ERROR(1013, "题目标签删除失败"),
    CATEGORY_DEL_SUBJECT_ERROR(1014, "该分类下有关联删除失败"),
    CATEGORY_EXPORT_ERROR(1015, "分类导出失败"),
    CATEGORY_UPDATE_ERROR(1016, "修改分类失败"),

    SUBJECT_ERROR(1017, "查询题目专题失败"),
    SUBJECT_DEL_ERROR(1013, "题目专题删除失败"),
    TOPIC_DEL_SUBJECT_ERROR(1014, "该专题下有关联删除失败"),
    SUBJECT_EXPORT_ERROR(1015, "分类导出失败"),
    SUBJECT_UPDATE_ERROR(1016, "修改专题失败"),

    AI_EXPORT_ERROR(1017, "导出失败"),


    USER_DATA_ERROR(1005, "暂时没有用户数据"),
    USER_ERROR(1006, "账号异常请联系管理员处理"),

    TOPIC_ERROR(1017, "查询题目失败"),
    TOPIC_EXPORT_ERROR(1018, "导出失败"),
    ADMIN_LOGIN_ERROR(1019, "用户名或密码错误"),
    TOPIC_UPDATE_ERROR(1020, "修改题目失败"),
    // SUBJECT_DEL_ERROR(1013, "题目专题删除失败"),
    // TOPIC_DEL_SUBJECT_ERROR(1014, "该专题下有关联删除失败"),
    // SUBJECT_EXPORT_ERROR(1015, "分类导出失败"),
    // SUBJECT_UPDATE_ERROR(1016, "修改专题失败"),

    REQUEST_TIMEOUT(1002, "请求超时"),
    REQUEST_LIMIT(1003, "请求次数超限"),
    REQUEST_NOT_FOUND(1004, "请求不存在"),
    REQUEST_METHOD_NOT_SUPPORTED(1005, "请求方法不支持"),

    REQUEST_PARAM_NOT_VALID(1006, "请求参数校验不通过");

    private Integer code;
    private String message;

}
