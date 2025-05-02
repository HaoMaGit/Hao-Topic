
export const isLogin = () => {
  const user = uni.getStorageSync('h5UserInfo')

  if (!user) {
    //跳转进入登录页
    uni.reLaunch({
      url: "/pages/login/login",
      success: () => { }
    })
  } else {
    uni.reLaunch({
      url: "/pages/index/index",
      success: () => { }
    })
  }
}
