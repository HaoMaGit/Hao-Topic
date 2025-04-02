import {
  defineStore
} from 'pinia';
import {
  ref
} from 'vue'
import { apiLogin } from '@/api/auth/index.ts'
// import { message } from 'ant-design-vue';
// import router from '@/router';
// import { asyncRoute } from '@/router/routers';
export const useUserStore = defineStore('user', () => {
  // 用户信息
  const userInfo = ref(null)
  // token
  const token = ref(null)
  // 菜单
  const menu = ref(null)
  // 测试
  // const ceShi = ref(null)
  // 登录
  const login = async (data: unknown) => {
    const res = await apiLogin(data)
    // 将token信息存储
    token.value = res.data
    // 获取用户信息
    getUserInfo()
  }
  // 获取用户信息
  const getUserInfo = async () => {
    // const res = await apiGetUserInfo(token.value)
    // menu.value = res.data.menuList
    // menu.value.unshift({ ...asyncRoute })
    // userInfo.value = res.data.userInfo
    // if (id == null) {
    //   message.success("登录成功")
    //   router.push('/')
    // }
  }
  // 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = null
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