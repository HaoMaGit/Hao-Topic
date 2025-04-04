// 登录类型
export interface LoginType {
  username: string,
  password: string,
  code: string,
  remember: boolean
}

// 登录返回结果类型
export interface LoginResultType {
  id: number | null,
  account: string,
  avatar: string,
  role: string,
  token: string
}