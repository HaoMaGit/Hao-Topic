import request from "@/utils/request.ts";
import type { TopicQueryType } from "./type";

const prefix = "/topic/topic/";

// 查询题目列表
export const apiGetTopicList = (query: TopicQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};

// 添加题目
export const apiAddTopic = (data: any) => {
  return request({
    url: prefix + "add",
    method: "post",
    data
  });
};

// 修改题目
export const apiUpdateTopic = (data: any) => {
  return request({
    url: prefix + "update",
    method: "put",
    data
  });
};

// 删除题目与批量删除
export const apiDeleteTopic = (ids: number[]) => {
  return request({
    url: prefix + "delete/" + ids,
    method: "delete",
  });
};

// 导出题目专题
export const apiExportTopic = (query: TopicQueryType | null, ids: number[] | null) => {
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

// 生成ai答案
export function apiGenerateAnswer(id: number) {
  return request({
    url: prefix + 'generateAnswer/' + id,
    method: 'get',
  })
}