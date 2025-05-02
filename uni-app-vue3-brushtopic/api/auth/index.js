import request  from '@/utils/request.js'

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