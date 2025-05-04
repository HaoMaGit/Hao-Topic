
import request from '@/utils/request.js'

// 前缀
const prefix = '/topic/topic/'

/**
 * 查询题目详情
 * @param {*} id 
 * @returns 
 */
export const apiQueryTopicDetail = (id) =>
  request(prefix + 'detail/' + id)



/**
 * 获取答案
 */

export const apiQueryTopicAnswer = (id) =>
  request(prefix + 'answer/' + id)