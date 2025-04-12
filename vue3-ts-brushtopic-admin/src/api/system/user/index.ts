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

// 查询角色列表
export const apiGetRoleList = () => {
  return request({
    url: prefix + "roleList",
    method: "get",
  });
};

// 添加角色
export const apiAddRole = (data: any) => {
  return request({
    url: prefix + "add",
    method: "post",
    data
  });
};