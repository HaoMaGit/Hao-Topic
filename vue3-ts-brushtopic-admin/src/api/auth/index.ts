import request from "@/utils/request.ts";
import type { LoginType, LoginResultType, UserResponse } from "./type";
import type { CommonResultType } from "../common";

const prefix = "/security/user/"


/**
 * 登录接口
 * @param {} data 
 * @returns 
 */
export const apiLogin = (data: LoginType): Promise<LoginResultType> => {
  return request({
    url: prefix + "login",
    method: "post",
    data: JSON.stringify(data),  // 确保数据被正确序列化
    headers: {
      'Content-Type': 'application/json',  // 明确指定 Content-Type
      'remember': data.remember
    },
  })
}

// 退出登录
export const apiLogout = () => {
  return request({
    url: prefix + "logout",
    method: "post",
  })
}

// 获取用户信息
export const apiGetUserInfo = (token: string | null): Promise<CommonResultType<UserResponse>> => {
  return request({
    url: prefix + "userInfo",
    method: "get",
    params: {
      token
    }
  })
}

// 获取用户信息
export function apiGetUserInfoDetail(id: number | null) {
  return request({
    url: prefix + 'info/' + id,
    method: 'get'
  })
}

// 根据用户id保存用户头像
export function apiSaveUserAvatar(data: any) {
  return request({
    url: prefix + 'avatar',
    method: 'put',
    data
  })
}