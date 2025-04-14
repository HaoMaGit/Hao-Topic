import type { PageType } from "@/api/common";

export interface SubjectCatgoryQueryType extends PageType {
  subjectName: string;
  createBy: string;
  params: any;
}