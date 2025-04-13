import request from "@/utils/request.ts";
import type { TopicCatgoryQueryType } from "./type";

const prefix = "/topic/category/";

// 查询题目分类列表
export const apiGetCategoryList = (query: TopicCatgoryQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};

// 添加题目分类
export const apiAddCategory = (data: any) => {
  return request({
    url: prefix + "add",
    method: "post",
    data
  });
};

// 修改题目分类
export const apiUpdateCategory = (data: any) => {
  return request({
    url: prefix + "update",
    method: "put",
    data
  });
};