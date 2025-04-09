import request from "@/utils/request.ts";
import type { RoleQueryType, RoleDtoType } from "./type";

const prefix = "/system/role/";

// 查询角色列表
export const apiGetRoleList = (query: RoleQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};

// 添加
export const apiAddRole = (data: RoleDtoType) => {
  return request({
    url: prefix + "add",
    method: "post",
    data
  });
};

// 修改
export const apiUpdateRole = (data: RoleDtoType) => {
  return request({
    url: prefix + "update",
    method: "put",
    data
  });
};

// 删除
export const apiDeleteRole = (id: number) => {
  return request({
    url: prefix + "delete/" + id,
    method: "delete"
  });
};

// 根据角色id查询菜单
export const apiGetRoleMenu = (id: number | null) => {
  return request({
    url: prefix + "menu/" + id,
    method: "get"
  });
};