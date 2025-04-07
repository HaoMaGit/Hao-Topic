import request from "@/utils/request.ts";
import type { MenuQueryType } from "./type";

const prefix = "/system/menu/";

// 查询菜单列表
export const apiGetMenuList = (query: MenuQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};