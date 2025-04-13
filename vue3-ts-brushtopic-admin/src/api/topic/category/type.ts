import type { PageType } from "@/api/common";

export interface TopicCatgoryQueryType extends PageType {
  categoryName: string;
  params: any;
}