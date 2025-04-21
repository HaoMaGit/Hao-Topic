// 进行axios二次封装:使用请求与响应拦截器
import axios from 'axios'
import { message } from 'ant-design-vue';
// 引入用户相关的仓库
import {
  useUserStore
} from '@/stores/modules/user.js'
import router from '@/router'
//第一步:利用axios对象的create方法,去创建axios实例(其他的配置:基础路径、超时的时间)
const request = axios.create({
  //基础路径
  baseURL: import.meta.env.VITE_APP_BASE_API, //基础路径上会携带/api
  timeout: 100000, //超时的时间的设置
})

//第二步:request实例添加请求与响应拦截器
request.interceptors.request.use((config) => {
  //获取用户相关的小仓库: 获取仓库内部token,登录成功以后携带给服务器
  const userStore = useUserStore()
  // 登录不需要带token
  if (userStore.token) {
    config.headers.Authorization = userStore.token
  }
  //config配置对象,headers属性请求头,经常给服务器端携带公共参数
  //返回配置对象
  return config
})

//第三步:响应拦截器
request.interceptors.response.use(
  (response) => {
    // 成功回调
    return response.data
  },
  (error) => {
    console.log(error);

    const status = error.response.status
    const userStore = useUserStore()

    if (status === 401) {
      //token过期
      //跳转到登录页面
      message.error('登录过期 请重新登录')
      router.push('/login')
      userStore.clearUserInfo()
      return
    }
    //提示错误信息
    message.error(error.response.data.message)
    return Promise.reject(error)
  },
)
//对外暴露
export default request