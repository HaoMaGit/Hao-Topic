// 通用的返回结果类型
export interface CommonResultType<T> {
  code: number | string;
  message: string;
  data: T;
}