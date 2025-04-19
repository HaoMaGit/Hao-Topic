import request from "@/utils/request.ts";
import type { ManageQueryType } from "./type";

const prefix = "/ai/manage/";


// 查询AI用户列表
export const apiGetManageList = (query: ManageQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};

// 修改Ai用户
export const apiUpdateAiUser = (data: any) => {
  return request({
    url: prefix + "update",
    method: "put",
    data
  });
};