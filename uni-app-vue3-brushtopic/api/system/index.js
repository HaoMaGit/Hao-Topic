import request from "@/utils/request.js";
const prefix = "/system/"

/**
 * 获取前端配置
 */
export const apiGetConfig = (status) => {
  return request({
    url: prefix + "config/" + status,
    method: "get",
  });
}