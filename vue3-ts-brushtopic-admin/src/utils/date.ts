import dayjs from "dayjs";
// 添加日期范围
export function addDateRange(params: any, dateRange: any, propName: string) {
  const search = params;
  search['begin' + propName] = dayjs(dateRange[0]).format('YYYY-MM-DD');
  search['end' + propName] = dayjs(dateRange[1]).format('YYYY-MM-DD');
  return search;
}

// 清除日期范围
export function clearDateRange(params: any, propName: string) {
  const search = params;
  search['begin' + propName] = '';
  search['end' + propName] = '';
  return search;
}