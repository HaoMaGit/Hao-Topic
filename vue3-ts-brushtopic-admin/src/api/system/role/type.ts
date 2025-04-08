import type { PageType } from "@/api/common";

// 角色查询类型
export interface RoleQueryType extends PageType {
  name: string;
}

// 角色实体
export interface RoleType {
  name: string;
  identify: number;
  id: number | null;
  remark: string;
  roleKey: string;
}