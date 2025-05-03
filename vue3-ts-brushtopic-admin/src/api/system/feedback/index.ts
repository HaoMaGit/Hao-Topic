import request from "@/utils/request.ts";
import type { FeedbackQueryType } from "./type";

const prefix = "/system/feedback/";

// 查询反馈列表
export const apiGetFeedbackList = (query: FeedbackQueryType) => {
  return request({
    url: prefix + "list",
    method: "get",
    params: query
  });
};

// 回复内容
export const apiReplyFeedback = (data: any) => {
  return request({
    url: prefix + "reply",
    method: "post",
    data
  });
};