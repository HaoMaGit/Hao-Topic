import request from "@/utils/request.ts";

const prefix = "/system/notice/";


// 查询通知列表
export const apiGetNoticeList = () => {
  return request.get(prefix + "list");
}

// 查询是否有通知
export const apiGetNoticeHas = () => {
  return request.get(prefix + "has");
}

/**
 * 已读通知
 * @returns 
 */
export const apiReadNotice = (idsList: any[]) => {
  return request({
    url: prefix + "read",
    method: "put",
    data: {
      idsList: idsList
    }
  });
}