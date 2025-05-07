import request from '@/utils/request.js'

// 前缀
const prefix = '/ai/model/'

// 获取对话历史记录
export const apiGetManageList = (param) => {
  return request({
    url: prefix + "history",
    method: "get",
    params: param
  });
}

// 根据历史记录id获取对话记录
export const apiGetHistoryDetail = (id) => {
  return request({
    url: prefix + "history/" + id,
    method: "get"
  });
}