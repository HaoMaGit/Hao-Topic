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
    PARAM_ACCOUNT_ERROR(99906, "账户不能为空"),
    PARAM_ROLE_ERROR(99907, "角色不能为空"),
    PARAM_PASSWORD_ERROR(99908, "密码不能为空"),
    ROLE_NO_EXIST(99908, "角色不存在"),
    UPLOAD_FILE_ERROR(99909, "上传文件失败"),
    ROLE_USER_ERROR(99910, "该角色下还有用户"),
    EXPORT_ERROR(99911, "导出数据失败"),
    DOWNLOAD_ERROR(99912, "下载导入模板失败"),
    IMPORT_ERROR(99913, "导入失败"),
    ADD_USER_ERROR(99914, "添加用户失败"),
    IMPORT_EXCEL_ERROR(99915, "导入数据不能为空"),

    CATEGORY_NAME_IS_NULL(99916, "题目分类名称不能为空"),
    CATEGORY_UPDATE_IS_NULL(99915, "修改题目分类失败"),
    CATEGORY_NAME_EXIST(99917, "题目分类名称已存在"),
    CATEGORY_DELETE_IS_NULL(99918, "删除题目分类失败"),
    CATEGORY_DELETE_TOPIC_ERROR(99919, "该题目分类下有关联专题"),

    SUBJECT_NAME_EXIST(99920, "题目专题已存在"),
    SUBJECT_UPDATE_IS_NULL(99921, "修改题目专题失败"),
    SUBJECT_DELETE_IS_NULL(99922, "删除题目专题失败"),
    SUBJECT_DELETE_TOPIC_ERROR(99922, "该题目专题下有关联题目"),
    SUBJECT_SELECT_ERROR(99923, "题目专题分类选择失败"),

    LABEL_NAME_IS_NULL(99924, "题目标签名称不能为空"),
    LABEL_NAME_EXIST(99925, "题目标签名称已存在"),
    LABEL_DELETE_IS_NULL(99926, "删除题目标签失败"),
    LABEL_DELETE_TOPIC_ERROR(99927, "该题目标签下有关联题目"),

    AI_HISTORY_ERROR(99928, "对话记录不存在"),
    AI_HISTORY_DELETE_ERROR(99929, "删除对话记录失败"),
    AI_HISTORY_UPDATE_ERROR(99930, "修改对话记录失败"),
    AI_COUNT_ERROR(99931, "使用次数不足请联系我们或晋升为我们的会员"),
    AI_ERROR(99932, "使用异常请联系我们"),


    TOPIC_NAME_EXIST(99928, "题目名称已存在"),
    SUBJECT_NOT_EXIST(99929, "题目专题不存在"),
    LABEL_NOT_EXIST(99930, "题目标签不存在"),
    TOPIC_UPDATE_IS_NULL(99931, "修改题目失败"),
    TOPIC_DELETE_IS_NULL(99932, "删除题目失败"),
    TOPIC_EVERYDAY_ERROR(99933, "每日推荐题目只能存在9个"),
    TOPIC_SUBJECT_IS_NULL(99934, "题目专题不能为空"),
    TOPIC_LABEL_IS_NULL(99935, "题目标签不能为空"),

    // 生成ai答案失败
    TOPIC_GENERATE_ANSWER_ERROR(99936, "生成ai答案失败"),
    TOPIC_GENERATE_ANSWER_PROCESSING(99937, "HaoAi正在努力生成中"),

    USER_NOT_EXIST(99938, "用户不存在"),
    USER_PASSWORD_ERROR(99939, "用户密码错误"),
    USER_NICKNAME_EXIST(99940, "用户昵称已存在"),
    USER_EMAIL_EXIST(99941, "用户邮箱已存在"),
    USER_EMAIL_NOT_EXIST(99942, "用户邮箱不存在"),
    USER_EMAIL_SEND_ERROR(99943, "发送邮件失败"),
    USER_EMAIL_CODE_ERROR(99944, "验证码过期或未发送"),
    USER_EMAIL_CODE_INPUT_ERROR(99945, "验证码输入错误"),
    USER_EMAIL_CODE_EXIST(99946, "验证码已存在打开QQ查看哦"),
    SERVICE_ERROR(2012, "服务异常");


    private Integer code;
    private String message;

}
