import { DatePickerProps } from './date-picker';
import type { PickerDate } from './util';
export declare function prompt(props: Omit<DatePickerProps, 'value' | 'visible' | 'children'>): Promise<PickerDate | null>;
