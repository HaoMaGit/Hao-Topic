// 通用的路由
export const asyncRoute = {
  label: '首页',
  key: '/home',
  icon: 'HomeOutlined'
}

// 对外暴露的配置路由
export const constantRoute = [
  // 登录
  {
    path: '/login',
    component: () => import('@/views/login.vue'),
    name: 'login',
    meta: {
      title: '登录', // 菜单标题
      hidden: true, // 代表路由标题在菜单中是否隐藏  true:隐藏 false:不隐藏
    },
  },
  // 登录成功后显示的路由
  {
    path: '/',
    component: () => import('../layout/index.vue'),
    name: 'layout',
    meta: {
      title: '',
      icon: '',
    },
    redirect: '/home',
    children: [{
      path: '/home',
      component: () => import('@/views/index.vue'),
    },
    // 测试路由
    {
      path: '/test',
      component: () => import('@/views/test/index.vue'),
    },]
  },
  // 404页面
  {
    path: '/404',
    component: () => import('@/views/404.vue'),
    name: '404',
    meta: {
      title: '404',
      hidden: true,
      icon: '',
    },
  },

  {
    // 当未匹配到路由则跳转404
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    name: 'Any',
    meta: {
      title: '任意路由',
    },
  }
]
