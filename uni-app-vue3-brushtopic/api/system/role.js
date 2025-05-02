import request from '@/utils/request.js'

// 前缀
const prefix = '/system/role/'

/**
 * 获取角色详情
 * @param {} role 
 * @returns 
 */
export const apiGetRoleDetail = (role) => {
  return request({
    url: prefix + 'identify/' + role,
    method: 'get',
  })
}