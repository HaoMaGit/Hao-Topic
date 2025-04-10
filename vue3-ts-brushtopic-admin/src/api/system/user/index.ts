import request from "@/utils/request.ts";
import type { UserQueryType } from "./type";

const prefix = "/system/user/";

// 查询用户列表
export const apiGetUserList = (query: UserQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};