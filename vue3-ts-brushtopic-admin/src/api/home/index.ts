import request from "@/utils/request.ts";


const prefix = "/topic/data/"
/**
 * 管理员首页左侧顶部系统数据
 */
export const apiAdminHomeCount = () => {
    return request({
        url: prefix + "adminHomeCount",
        method: "get",
    })
}
/**
 * 管理员首页右侧分类数据
 */
export const apiAdminHomeCategory = () => {
    return request({
        url: prefix + "adminHomeCategory",
        method: "get",
    })
}


/**
 * 统计题目15日的趋势图
 */
export const apiTopicTrend = () => {
    return request({
        url: prefix + "topicTrend",
        method: "get",
    })
}



// 统计用户15日的趋势图
export const apiUserTrend = () =>
    request.get(prefix + "userTrend");