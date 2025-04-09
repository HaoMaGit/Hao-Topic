import type { PageType } from "@/api/common";

// 角色查询类型
export interface RoleQueryType extends PageType {
  name: string;
}

// 角色实体
export interface RoleType {
  name: string;
  identify: number | null;
  id: number | null;
  remark: string;
  roleKey: string;
}

// 角色实体（包含菜单）
export interface RoleDtoType extends RoleType {
  menuIds: number[] | null;
}