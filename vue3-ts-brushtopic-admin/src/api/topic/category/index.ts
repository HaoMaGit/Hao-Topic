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

// 删除题目与批量删除
export const apiDeleteCategory = (ids: number[]) => {
  return request({
    url: prefix + "delete/" + ids,
    method: "delete",
  });
};

// 导出题目分类
export const apiExportCategory = (query: TopicCatgoryQueryType | null, ids: number[] | null) => {
  return request({
    url: prefix + "export/" + ids,
    method: "get",
    params: query,
    responseType: "blob"
  });
};



// 下载导入模板
export function apiGetExportTemplate() {
  return request({
    url: prefix + 'template',
    responseType: 'blob' // 二进制文件流
  })
}