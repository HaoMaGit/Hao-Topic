import type { PageType } from "@/api/common";

export interface TopicLabelQueryType extends PageType {
  labelName: string;
  createBy: string;
  params: any;
}