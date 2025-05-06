
export interface AdminLeftDataType {
  countTodayFrequency: number,
  topicGrowthRate: number,
  userCount: number,
  userGrowthRate: number,
  totalTopicCount: number,
  totalSubjectCount: number
  topicLabelCount: number,
  aiCount: number,
  aiGrowthRate: number;
}

export interface TopicTrendType {
  dateList: any[],
  countUserList: any[],
  countTopicList: any[]
}