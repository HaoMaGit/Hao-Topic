import {
  defineStore
} from 'pinia';
import {
  ref
} from 'vue'
import { apiLogin, apiGetUserInfo, apiLogout } from '@/api/auth/index.ts'
import type { LoginType, UserResponse } from '@/api/auth/type';
import { message } from 'ant-design-vue';
import router from '@/router';
import { asyncRoute } from '@/router/routers';

export const useUserStore = defineStore('user', () => {
  // 用户信息
  const userInfo = ref<UserResponse>({
    account: '',
    avatar: '',
    identity: null,
    menuList: []
  });
  // token
  const token = ref<string | null>(null)
  // 登录
  const login = async (data: LoginType) => {
    const res = await apiLogin(data)
    console.log(res);
    // 登录失败
    if (res.code !== 200 && res.code !== '200') {
      message.error(res.message)
      return
    }
    // 将token信息存储
    token.value = res.token
    // 获取用户信息
    getUserInfo()
  }
  // 获取用户信息
  const getUserInfo = async () => {
    const res = await apiGetUserInfo(token.value)
    console.log(res);

    if (res.data) {
      userInfo.value = res.data
      // 确保 menuList 已初始化
      if (!userInfo.value.menuList) {
        userInfo.value.menuList = []
      }
      // 添加异步路由
      userInfo.value.menuList.unshift({ ...asyncRoute })
    } else {
      message.error("获取用户信息失败")
      return
    }
    console.log(userInfo.value.menuList);

    if (res.code === 200) {
      message.success("登录成功")
      router.push('/')
    }
  }
  const clearUserInfo = () => {
    apiLogout()
    userInfo.value = {
      account: '',
      avatar: '',
      identity: null,
      menuList: []
    }
    token.value = null
    window.localStorage.removeItem("user")
  }
  // 返回出去
  return {
    userInfo,
    getUserInfo,
    clearUserInfo,
    login,
    token,
  }
}, {
  persist: true
})