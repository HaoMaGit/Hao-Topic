import { RenderLabel } from '../date-picker-view/date-picker-view';
import type { DatePrecision } from './date-picker-date-utils';
import type { QuarterPrecision } from './date-picker-quarter-utils';
import type { WeekPrecision } from './date-picker-week-utils';
export declare type Precision = DatePrecision | WeekPrecision | QuarterPrecision;
export declare type DatePickerFilter = Partial<Record<Precision, (val: number, extend: {
    date: Date;
}) => boolean>>;
export declare const convertDateToStringArray: (date: Date | undefined | null, precision: Precision) => string[];
export declare const convertStringArrayToDate: <T extends string | number | null | undefined>(value: T[], precision: Precision) => Date;
export declare const generateDatePickerColumns: (selected: string[], min: Date, max: Date, precision: Precision, renderLabel: RenderLabel, filter: DatePickerFilter | undefined, tillNow?: boolean | undefined) => import("../picker-view").PickerColumn[];
