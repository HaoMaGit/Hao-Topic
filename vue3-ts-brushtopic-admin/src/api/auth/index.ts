import request from "@/utils/request.ts";

const prefix = "/security/user"

/**
 * 登录接口
 * @param {} data 
 * @returns 
 */
export const apiLogin = (data: unknown) => {
  return request({
    url: prefix + "login",
    method: "post",
    data,
  })
}

// 退出登录
export const apiLogout = () => {
  return request({
    url: prefix + "logout",
    method: "post",
  })
}


