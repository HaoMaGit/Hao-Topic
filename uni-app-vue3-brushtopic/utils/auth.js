export const isLogin = () => {
  const user = uni.getStorageSync('h5UserInfo')
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]?.route || ''
  
  // 如果没有登录且不在登录页，则跳转到登录页
  if (!user && currentPage !== 'pages/login/login') {
    uni.reLaunch({
      url: "/pages/login/login"
    })
    return
  }

  // 如果已登录且在登录页，则跳转到首页
  if (user && currentPage === 'pages/login/login') {
    uni.reLaunch({
      url: "/pages/index/index"
    })
    return
  }
}


// 清空缓存
export const clearStorage = () => {
  // 用户信息
  const userInfo = JSON.parse(uni.getStorageSync('h5UserInfo'))
  uni.removeStorageSync('h5UserInfo')
  uni.removeStorageSync(userInfo.account + "token")
  uni.removeStorageSync("role")
}
