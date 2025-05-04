import request from '@/utils/request.js'

// 前缀
const prefix = '/topic/subject/'

/**
 * 根据分类id查询专题列表
 * @param {*} categoryId 
 * @returns 
 */
export const apiQuerySubjectList = (categoryId) =>
  request(prefix + 'subject/' + categoryId)