// 进行axios二次封装:使用请求与响应拦截器
import axios from 'axios'
import { Toast } from 'antd-mobile'
//第一步:利用axios对象的create方法,去创建axios实例(其他的配置:基础路径、超时的时间)
const request = axios.create({
  //基础路径
  baseURL: "http://localhost:9993", //基础路径上会携带/api
  timeout: 100000, //超时的时间的设置
})

//第二步:request实例添加请求与响应拦截器
request.interceptors.request.use((config) => {
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

    if (status === 401) {
      //token过期
      //跳转到登录页面
      Toast.show({
        icon: 'fail',
        content: '登录过期 请重新登录',
      })
      // 清除用户信息
      uni.removeStorageSync('userInfo')
      // 跳转
      setTimeout(() => {
        uni.reLaunch({
          url: '/pages/login/login',
        })
      }, 1500)
      return
    }
    //提示错误信息
    Toast.show({
      icon: 'fail',
      content: '登录失败请重新登录',
    })
    return Promise.reject(error)
  },
)
//对外暴露
export default request