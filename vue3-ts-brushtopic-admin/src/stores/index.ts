// 仓库大仓库
import {
  createPinia
} from 'pinia'
// 持久化插件
// pinia持久化插件
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
// 创建大仓库
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
// 对外暴露：入口文件需要安装仓库
export default pinia