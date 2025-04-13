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
    ]
  },
  // 系统管理=>用户管理 菜单管理 角色管理
  {
    path: '/system',
    redirect: '/system/user',
    component: () => import('../layout/index.vue'),
    children: [
      {
        path: '/system/user',
        component: () => import('@/views/system/user/index.vue'),
      },
      {
        path: '/system/role',
        component: () => import('@/views/system/role/index.vue'),
      },
      {
        path: '/system/menu',
        component: () => import('@/views/system/menu/index.vue'),
      }
    ]
  },
  {
    path: '/topic',
    redirect: '/topic/topic',
    component: () => import('../layout/index.vue'),
    children: [
      {
        path: '/topic/topic',
        component: () => import('@/views/topic/topic/index.vue'),
      },
      {
        path: '/topic/category',
        component: () => import('@/views/topic/category/index.vue'),
      },
      {
        path: '/topic/label',
        component: () => import('@/views/topic/label/index.vue'),
      },
      {
        path: '/topic/subject',
        component: () => import('@/views/topic/subject/index.vue'),
      }
    ]
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
