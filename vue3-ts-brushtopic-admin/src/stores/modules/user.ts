import {
  defineStore
} from 'pinia';
import {
  ref
} from 'vue'
import { apiLogin, apiGetUserInfo } from '@/api/auth/index.ts'
import type { LoginType, UserResponse } from '@/api/auth/type';
import { message } from 'ant-design-vue';
import router from '@/router';

// import { asyncRoute } from '@/router/routers';
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
  // 菜单
  const menu = ref([])
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
    // TODO 菜单信息
    menu.value = res.data.menuList
    // menu.value.unshift({ ...asyncRoute })
    userInfo.value = res.data
    if (res.code === 200) {
      message.success("登录成功")
      router.push('/')
    }
  }
  // TODO 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = {
      account: '',
      avatar: '',
      identity: null,
      menuList: []
    }
    token.value = null
    menu.value = []
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