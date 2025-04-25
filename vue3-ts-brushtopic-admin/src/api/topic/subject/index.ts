import request from "@/utils/request.ts";
import type { SubjectCatgoryQueryType } from "./type";

const prefix = "/topic/subject/";

// 查询题目专题列表
export const apiGetSubjectList = (query: SubjectCatgoryQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};

// 添加题目专题
export const apiAddSubject = (data: any) => {
  return request({
    url: prefix + "add",
    method: "post",
    data
  });
};

// 修改题目专题
export const apiUpdateSubject = (data: any) => {
  return request({
    url: prefix + "update",
    method: "put",
    data
  });
};

// 删除题目专题与批量删除
export const apiDeleteSubject = (ids: number[]) => {
  return request({
    url: prefix + "delete/" + ids,
    method: "delete",
  });
};

// 导出题目专题
export const apiExportSubject = (query: SubjectCatgoryQueryType | null, ids: number[] | null) => {
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

// 查询专题名称以及id
export function apiGetSubjectName() {
  return request({
    url: prefix + 'getSubject',
    method: 'get'
  })
}