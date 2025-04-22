import request from "@/utils/request.ts";
import type { AiHistoryDto } from "./type";
// 获取路径
const prefix = "/ai/model/";

// 获取对话历史记录
export const apiGetManageList = (param: AiHistoryDto) => {
  return request({
    url: prefix + "history",
    method: "get",
    params: param
  });
}
