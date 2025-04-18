import type { PageType } from "@/api/common";

export interface TopicQueryType extends PageType {
  topicName: string;
  createBy: string;
  params: any;
}