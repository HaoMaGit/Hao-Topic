import request from '@/utils/request.js'

// 前缀
const prefix = '/security/user/'

// 登录方法
export const apiLogin = (data) => {
  return request({
    url: prefix + 'loginType',
    method: 'post',
    data
  })
}

// 获取用户信息方法
export const apiGetUserInfo = (id) => {
  return request({
    url: prefix + 'info/' + id,
    method: 'get',
  })
}

// 修改昵称
export const apiUpdateNicknameAndEmail = (data) => {
  return request({
    url: prefix + 'updateNicknameAndEmail',
    method: 'put',
    data
  })
}

// 修改密码
export const apiUpdatePassword = (data) => {
  return request({
    url: prefix + 'updatePassword',
    method: 'put',
    data
  })
}

// 根据用户id保存用户头像
export function apiSaveUserAvatar (data) {
  return request({
    url: prefix + 'avatar',
    method: 'put',
    data
  })
}

// 退出登录
export const apiLogout = () => {
  return request({
    url: prefix + "logout",
    method: "post",
  })
}

// 发送邮件
export const apiSendEmail = (email) => {
  return request({
    url: prefix + "sendEmail",
    method: "get",
    params: { email },
  })
}

// 重置密码
export const apiResetPassword = (data) => {
  return request({
    url: prefix + "resetPassword",
    method: "put",
    data,
  })
}

// 注册账户
export const apiRegister = (data) => {
  return request({
    url: prefix + "register",
    method: "post",
    data,
  })
}