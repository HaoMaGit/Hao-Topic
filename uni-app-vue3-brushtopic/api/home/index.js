import request from "@/utils/request.js";
const prefix = "/topic/data/"


/**
 * 统计h5首页刷题数据以及用户数量和排名
 */
export const apiQueryWebHomeCount = () =>
  request(prefix + 'webHomeCount')