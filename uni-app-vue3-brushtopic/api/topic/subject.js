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


/**
 * 根据专题id查询专题详细信息和题目列表
 */

export const apiQuerySubjectDetail = (id) =>
  request(prefix + 'subjectDetail/' + id)

