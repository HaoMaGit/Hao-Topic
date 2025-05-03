import type { PageType } from "@/api/common";


export interface FeedbackQueryType extends PageType {
  account: string | null;
  status: number | null;
  replyAccount: string | null;
}