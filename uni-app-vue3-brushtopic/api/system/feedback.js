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

// 查询反馈列表
export const apiQueryFeedbackList = () => {
  return request({
    url: prefix + 'feedback',
    method: 'get',
  })
}