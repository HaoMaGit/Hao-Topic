// 路由鉴权:鉴权,项目当中路由能不能被的权限的设置(某一个路由什么条件下可以访问、什么条件下不可以访问)
import router from '@/router'
// 引入进度条
import nprogress from 'nprogress'
// 引入进度条样式
import 'nprogress/nprogress.css'
//获取用户相关的小仓库内部token数据,去判断用户是否登录成功
import {
  useUserStore
} from './stores/modules/user.js'
import { message } from 'ant-design-vue'
//全局守卫:项目当中任意路由切换都会触发的钩子
//全局前置守卫
const whiteList = ['/login', '/404', '/captchaImage']
router.beforeEach(async (to, from, next) => {
  //to:你将要访问那个路由
  //from:你从来个路由而来
  //next:路由的放行函数
  nprogress.start()
  // 实例化用户相关的小仓库
  const userStore = useUserStore()
  //获取token,去判断用户登录、还是未登录
  const token = userStore.token
  //获取用户名字
  const account = userStore.userInfo?.account

  //用户登录判断
  if (token) {
    // 登录成功,访问login,不能访问,指向首页
    if (to.path == '/login') {
      next('/')
    } else {
      // 有用户信息
      if (account) {
        next()
        return
      } else {
        try {
          // 获取用户信息
          await userStore.getUserInfo()
          // 放行
          next({
            ...to
          })
          // eslint-disable-next-line @typescript-eslint/no-unused-vars
        } catch (error) {
          message.success('请重新登录')
          //退出登录->用户相关的数据清空
          await userStore.clearUserInfo()
          next('/login')
        }
      }
    }
  } else {
    if (whiteList.includes(to.path)) {
      console.log('不需要登录就可以访问');
      next()
    } else {
      console.log('没有token');
      next('/login')
    }
  }
})
//全局后置守卫
// eslint-disable-next-line @typescript-eslint/no-unused-vars
router.afterEach((to, from) => {
  nprogress.done()
})