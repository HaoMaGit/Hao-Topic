import React, { ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
import { DateRange, Page } from './convert';
export declare const Context: React.Context<{
    visible: boolean;
}>;
export declare type CalendarPickerViewRef = {
    jumpTo: (page: Page | ((page: Page) => Page)) => void;
    jumpToToday: () => void;
    getDateRange: () => DateRange;
};
export declare type CalendarPickerViewColumRender = (date: Date) => ReactNode;
export declare type CalendarPickerViewProps = {
    title?: React.ReactNode | false;
    confirmText?: string;
    weekStartsOn?: 'Monday' | 'Sunday';
    renderTop?: CalendarPickerViewColumRender | false;
    renderDate?: CalendarPickerViewColumRender;
    renderBottom?: CalendarPickerViewColumRender | false;
    allowClear?: boolean;
    max?: Date;
    min?: Date;
    shouldDisableDate?: (date: Date) => boolean;
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
export declare const CalendarPickerView: React.ForwardRefExoticComponent<CalendarPickerViewProps & React.RefAttributes<CalendarPickerViewRef>>;
