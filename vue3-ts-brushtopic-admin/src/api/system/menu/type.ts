
// 菜单列表查询参数
export interface MenuQueryType {
  menuName: string;
}

// 添加参数
export interface MenuType {
  menuName: string;
  icon: string;
  route: string;
  sorted: number | null;
  parentId: number;
  id: number | null;
}