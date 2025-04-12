import type { PageType } from "@/api/common";

// 用户列表查重
export interface UserQueryType extends PageType {
  account: string;
  roleName: string;
  params: any;
}