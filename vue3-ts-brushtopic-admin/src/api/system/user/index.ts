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

// 添加用户
export const apiAddUser = (data: any) => {
  return request({
    url: prefix + "add",
    method: "post",
    data
  });
};

// 修改用户
export const apiUpdateUser = (data: any) => {
  return request({
    url: prefix + "update",
    method: "put",
    data
  });
};

// 删除用户与批量删除
export const apiDeleteUser = (ids: number[]) => {
  return request({
    url: prefix + "delete/" + ids,
    method: "delete",
  });
};