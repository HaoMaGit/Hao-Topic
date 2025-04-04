import {
  defineStore
} from 'pinia';
import {
  ref
} from 'vue'
import { apiLogin, apiGetUserInfo } from '@/api/auth/index.ts'
import type { LoginType, LoginResultType } from '@/api/auth/type';
import { message } from 'ant-design-vue';
import router from '@/router';

// import { asyncRoute } from '@/router/routers';
export const useUserStore = defineStore('user', () => {
  // 用户信息
  const userInfo = ref<LoginResultType>({
    id: null,
    account: '',
    avatar: '',
    role: '',
    token: ''
  });
  // token
  const token = ref<string | null>(null)
  // 菜单
  const menu = ref(null)
  // 登录
  const login = async (data: LoginType) => {
    const res = await apiLogin(data)
    console.log(res);
    // 登录失败
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    if (res.code !== 200 && res.code !== '200') {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-expect-error
      message.error(res.message)
      return
    }
    // 将token信息存储
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    token.value = res.token
    // 获取用户信息
    getUserInfo()
  }
  // 获取用户信息
  const getUserInfo = async () => {
    const res = await apiGetUserInfo(token.value)
    menu.value = res.data.menuList
    // menu.value.unshift({ ...asyncRoute })
    userInfo.value = res.data
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    if (res.code === 200) {
      message.success("登录成功")
      router.push('/')
    }
  }
  // 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = {
      id: null,
      account: '',
      avatar: '',
      role: '',
      token: ''
    }
    token.value = null
    menu.value = null
    window.localStorage.removeItem("user")
  }
  // 返回出去
  return {
    userInfo,
    getUserInfo,
    clearUserInfo,
    login,
    token,
    menu
  }
}, {
  persist: true
})