import dayjs from 'dayjs';
export declare type DateRange = [Date, Date] | null;
export declare type Page = {
    month: number;
    year: number;
};
export declare function convertValueToRange(selectionMode: 'single' | 'range' | undefined, value: Date | [Date, Date] | null): DateRange;
export declare function convertPageToDayjs(page: Page): dayjs.Dayjs;
