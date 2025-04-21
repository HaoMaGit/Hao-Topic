// import request from "@/utils/request.ts";
import { useUserStore } from "@/stores/modules/user";
const userStore = useUserStore();
import { message } from "ant-design-vue";
// 获取路径
const { VITE_SERVE, VITE_APP_BASE_API } = import.meta.env
const prefix = "/ai/model/";

// 发送
export const apiSendMessage = async (mes: any) => {
  const url = new URL(`${VITE_SERVE}${VITE_APP_BASE_API}${prefix}chat`)
  try {
    const response = await fetch(url, {
      method: "POST",
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-expect-error
      headers: {
        "Content-Type": "application/json",
        "Authorization": userStore.token,
      },
      body: JSON.stringify(mes),
    });
    if (!response.ok) {
      message.error("发送失败")
    }
    if (response.body) {
      return response.body.getReader()
    }
  } catch {
    message.error("发送失败")
  }
};
