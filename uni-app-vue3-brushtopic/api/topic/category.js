import request from '@/utils/request.js'

// 前缀
const prefix = '/topic/category/'

/**
 * 获取分类名称和id
 * @param {*} isCustomQuestion 
 * @returns 
 */
export const apiQueryCategoryList = (isCustomQuestion) =>
  request(prefix + 'category/' + isCustomQuestion)