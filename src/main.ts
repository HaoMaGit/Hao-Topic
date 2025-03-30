// 引入全局样式
import './styles/index.scss'
import { createApp } from 'vue'
// 引入pinia
import { createPinia } from 'pinia'
// 引入路由
import router from './router'
import App from './App.vue'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.mount('#app')
