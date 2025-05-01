import React from 'react';
import { NativeProps } from '../../utils/native-props';
import { Page } from './convert';
export declare type CalendarRef = {
    jumpTo: (page: Page | ((page: Page) => Page)) => void;
    jumpToToday: () => void;
};
export declare type CalendarProps = {
    prevMonthButton?: React.ReactNode;
    prevYearButton?: React.ReactNode;
    nextMonthButton?: React.ReactNode;
    nextYearButton?: React.ReactNode;
    onPageChange?: (year: number, month: number) => void;
    weekStartsOn?: 'Monday' | 'Sunday';
    renderLabel?: (date: Date) => React.ReactNode;
    renderDate?: (date: Date) => React.ReactNode;
    allowClear?: boolean;
    max?: Date;
    min?: Date;
    shouldDisableDate?: (date: Date) => boolean;
    minPage?: Page;
    maxPage?: Page;
} & ({
    selectionMode?: undefined;
    value?: undefined;
    defaultValue?: undefined;
    onChange?: undefined;
} | {
    selectionMode: 'single';
    value?: Date | null;
    defaultValue?: Date | null;
    onChange?: (val: Date | null) => void;
} | {
    selectionMode: 'range';
    value?: [Date, Date] | null;
    defaultValue?: [Date, Date] | null;
    onChange?: (val: [Date, Date] | null) => void;
}) & NativeProps;
export declare const Calendar: React.ForwardRefExoticComponent<CalendarProps & React.RefAttributes<CalendarRef>>;
