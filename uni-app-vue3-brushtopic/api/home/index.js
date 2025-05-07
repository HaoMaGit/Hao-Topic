import request from "@/utils/request.js";
const prefix = "/topic/data/"


/**
 * 统计h5首页刷题数据以及用户数量和排名
 */
export const apiQueryWebHomeCount = () =>
  request(prefix + 'webHomeCount')

/**
 * 查询排行榜
 */
export const apiQueryRank = (type) =>
  request(prefix + 'rank/' + type)

/**
 * 获取当前用户排名信息
 */
export const apiQueryUserRank = (type) =>
  request(prefix + 'userRank/' + type)

/**
 * 获取今日题目
 */
export const apiQueryTopicTodayVo = () =>
  request(prefix + 'topicTodayVo')