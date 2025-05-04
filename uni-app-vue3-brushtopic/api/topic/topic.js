
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


/**
 * 根据题目id收藏题目
 */

export const apiCollectionTopic = (id) =>
  request(prefix + 'collection/' + id)


/**
    * 查询收藏的题目
    */
export const apiQueryCollectionTopicList = () =>
  request(prefix + 'collection/list')

/**
  * 计算用户刷题次数
  */
export const apiCountTopic = (data) =>
  request({
    url: prefix + 'count',
    method: 'post',
    data
  })