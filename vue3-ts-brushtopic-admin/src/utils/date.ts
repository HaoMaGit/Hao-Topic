import dayjs from "dayjs";
import * as echarts from 'echarts';
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

// 生成指定年份的数据
export function getVirtulDataByYear(year: number) {
  const { start, end } = getYearDateRange(year);
  const dayTime = 3600 * 24 * 1000;
  const data = [];
  for (let time = start; time <= end; time += dayTime) {
    data.push([
      echarts.format.formatTime('yyyy-MM-dd', time),
      Math.floor(Math.random() * 5)
    ]);
  }
  return data;
}

// 根据年份生成日期范围
export function getYearDateRange(year: number) {
  const startDate = new Date(year, 0, 1); // 1月1日
  const endDate = new Date(year, 11, 31); // 12月31日
  return {
    start: startDate.getTime(),
    end: endDate.getTime(),
    startStr: echarts.format.formatTime('yyyy-MM-dd', startDate),
    endStr: echarts.format.formatTime('yyyy-MM-dd', endDate)
  };
}

// 闰年判断工具函数
export function isLeapYear(year: number) {
  return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
}

// 动态生成日期范围（以今天为起点生成过去12个月）
export function getDynamicDateRange() {
  const today = new Date();

  // 起始日期 = 去年同一天
  const startDate = new Date(today);
  startDate.setFullYear(today.getFullYear() - 1);

  // 结束日期 = 今天
  const endDate = new Date(today);

  // 处理闰年2月29日特殊情况
  if (
    startDate.getMonth() === 1 &&
    startDate.getDate() === 29 &&
    !isLeapYear(startDate.getFullYear())
  ) {
    startDate.setDate(28);
  }

  return {
    start: startDate.getTime(),
    end: endDate.getTime(),
    startStr: echarts.format.formatTime('yyyy-MM-dd', startDate),
    endStr: echarts.format.formatTime('yyyy-MM-dd', endDate)
  };
}