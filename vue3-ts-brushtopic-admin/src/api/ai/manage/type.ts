import type { PageType } from "@/api/common";

// 用户列表查重
export interface ManageQueryType extends PageType {
  account: string;
}