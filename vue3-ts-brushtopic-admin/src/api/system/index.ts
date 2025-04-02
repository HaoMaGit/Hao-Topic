import request from "@/utils/request.ts";
const prefix = "/system/"

// 获取验证码
export const apiGetCode = () => {
  return request({
    url: prefix + "captchaImage",
    method: "get",
    responseType: 'blob'  // 关键配置
  });
}

