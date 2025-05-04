import request from '@/utils/request.js'

// 前缀
const prefix = '/system/notice/'

/**
 * 记录通知
 * @param {} data 
 * @returns 
 */
export const apiRecordNotice = (data) =>
  request({
    url: prefix + 'record',
    method: 'post',
    data
  })

// 查询是否有通知
export const apiGetNoticeHas = () => {
  return request.get(prefix + "has");
}

export const apiClearNotice = () => {
  return request.put(prefix + "clear");
}