import request from "@/utils/request.ts";
import type { TopicLabelQueryType } from "./type";

const prefix = "/topic/label/";

// 查询题目标签列表
export const apiGetLabelList = (query: TopicLabelQueryType | null) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};

// 添加题目标签
export const apiAddLabel = (data: any) => {
  return request({
    url: prefix + "add",
    method: "post",
    data
  });
};

// 修改题目标签
export const apiUpdateLabel = (data: any) => {
  return request({
    url: prefix + "update",
    method: "put",
    data
  });
};

// 删除题目与批量删除
export const apiDeleteLabel = (ids: number[]) => {
  return request({
    url: prefix + "delete/" + ids,
    method: "delete",
  });
};

// 导出题目标签
export const apiExportLabel = (query: TopicLabelQueryType | null, ids: number[] | null) => {
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