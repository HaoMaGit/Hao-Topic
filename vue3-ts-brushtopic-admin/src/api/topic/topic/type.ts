import type { PageType } from "@/api/common";

export interface TopicQueryType extends PageType {
  topicName: string;
  createBy: string;
  params: any;
  // answer: string;
  // aiAnswer: string;
  // sorted: number;
  // isEveryDay: number | null;
  // isMember: number | null;
  // subjectId: number;
  // labelIds: any
}