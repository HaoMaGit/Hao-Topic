import request from '@/utils/request.js'

// 前缀
const prefix = '/system/feedback/'

// 发送反馈
export const apiSendFeedback = (data) => {
  return request({
    url: prefix + 'send',
    method: 'post',
    data
  })
}