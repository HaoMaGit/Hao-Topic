// 引入全局样式
import './styles/index.scss'
import { createApp } from 'vue'
// 引入pinia
import pinia from './stores/index'
// 引入路由
import router from './router'
// 引入ant组件库
import Antd from 'ant-design-vue';
// 引入ant组件库样式
import 'ant-design-vue/dist/reset.css';
// 引入所有ant图标
import *  as Icon from '@ant-design/icons-vue'
// 引入路由鉴权
import './permisstion.ts'
import App from './App.vue'


const app = createApp(App)

// 全局注册所有图标
Object.keys(Icon).forEach((key) => {
  app.component(key, (Icon as any)[key])
})

app.use(pinia)
app.use(router)
app.use(Antd)
app.mount('#app')
