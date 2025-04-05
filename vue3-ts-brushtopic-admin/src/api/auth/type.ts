// 登录类型
export interface LoginType {
  username: string,
  password: string,
  code: string,
  remember: boolean
}

// 用户返回结果类型
export interface UserResponse {
  account: string,
  avatar: string,
  identity: number | null,
  menuList: MenuType[]
}

// 登录返回结果类型
export interface LoginResultType {
  token: string;
  code: number | string;
  message: string;
}

// 菜单类型
export interface MenuType {
  children: MenuType[];
  key: string;
  icon: unknown;
  label: string;
}

