import type { PageType } from "@/api/common";

// 菜单列表查询参数
export interface MenuQueryType extends PageType {
  menuName: string;
}