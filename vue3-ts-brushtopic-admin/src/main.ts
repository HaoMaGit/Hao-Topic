// 引入全局样式
import './styles/index.scss'
import { createApp } from 'vue'
// 引入pinia
import { createPinia } from 'pinia'
// 引入路由
import router from './router'
// 引入ant组件库
import Antd from 'ant-design-vue';
// 引入ant组件库样式
import 'ant-design-vue/dist/reset.css';
import App from './App.vue'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)
app.mount('#app')
