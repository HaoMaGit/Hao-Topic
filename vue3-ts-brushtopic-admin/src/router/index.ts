// 统一路由配置
import { createRouter, createWebHistory } from 'vue-router'
// 引入静态路由
import {
  constantRoute,
}
  from './routers'
// 创建路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoute
})


export default router
