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





