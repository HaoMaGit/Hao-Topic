// 通用的返回结果类型
export interface CommonResultType<T> {
  code: number | string;
  message: string;
  data: T;
}

// 分页类型
export interface PageType {
  pageNum: number;
  pageSize: number;
}

// 分页返回数据
export interface PageResultType<T> {
  total: number;
  rows: T[];
}