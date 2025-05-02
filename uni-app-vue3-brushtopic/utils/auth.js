
export const isLogin = () => {
  const user = uni.getStorageSync('h5UserInfo')
  console.log("================>");

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


// 清空缓存
export const clearStorage = () => {
  // 用户信息
  const userInfo = JSON.parse(uni.getStorageSync('h5UserInfo'))
  uni.removeStorageSync('h5UserInfo')
  uni.removeStorageSync(userInfo.account + "token")
  uni.removeStorageSync("role")
}
