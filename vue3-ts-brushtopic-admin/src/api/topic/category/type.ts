import type { PageType } from "@/api/common";

export interface TopicCatgoryQueryType extends PageType {
  categoryName: string | null;
  createBy: string | null;
  params: any | null;
  status: number | null;
}