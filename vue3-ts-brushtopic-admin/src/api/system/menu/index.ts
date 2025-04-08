import request from "@/utils/request.ts";
import type { MenuQueryType, MenuType } from "./type";

const prefix = "/system/menu/";

// 查询菜单列表
export const apiGetMenuList = (query: MenuQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};

// 添加
export const apiAddMenu = (data: MenuType) => {
  return request({
    url: prefix + "add",
    method: "post",
    data
  });
};

// 修改
export const apiUpdateMenu = (data: MenuType) => {
  return request({
    url: prefix + "update",
    method: "put",
    data
  });
};

// 删除
export const apiDeleteMenu = (id: number) => {
  return request({
    url: prefix + "delete/" + id,
    method: "delete"
  });
};