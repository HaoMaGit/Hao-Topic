import React from 'react';
import { type GetContainer } from '../../utils/render-to-container';
import { CalendarPickerViewProps, CalendarPickerViewRef } from '../calendar-picker-view';
export declare type CalendarPickerRef = CalendarPickerViewRef;
export declare type CalendarPickerProps = CalendarPickerViewProps & {
    visible?: boolean;
    confirmText?: string;
    popupClassName?: string;
    popupStyle?: React.CSSProperties;
    popupBodyStyle?: React.CSSProperties;
    forceRender?: true;
    closeOnMaskClick?: boolean;
    onClose?: () => void;
    onMaskClick?: () => void;
    getContainer?: GetContainer;
} & ({
    selectionMode?: undefined;
    onConfirm?: undefined;
} | {
    selectionMode: 'single';
    onConfirm?: (val: Date | null) => void;
} | {
    selectionMode: 'range';
    onConfirm?: (val: [Date, Date] | null) => void;
});
export declare const CalendarPicker: React.ForwardRefExoticComponent<(({
    title?: React.ReactNode;
    confirmText?: string | undefined;
    weekStartsOn?: "Monday" | "Sunday" | undefined;
    renderTop?: false | import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    renderDate?: import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    renderBottom?: false | import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    allowClear?: boolean | undefined;
    max?: Date | undefined;
    min?: Date | undefined;
    shouldDisableDate?: ((date: Date) => boolean) | undefined;
} & {
    selectionMode?: undefined;
    value?: undefined;
    defaultValue?: undefined;
    onChange?: undefined;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<never, string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & {
    visible?: boolean | undefined;
    confirmText?: string | undefined;
    popupClassName?: string | undefined;
    popupStyle?: React.CSSProperties | undefined;
    popupBodyStyle?: React.CSSProperties | undefined;
    forceRender?: true | undefined;
    closeOnMaskClick?: boolean | undefined;
    onClose?: (() => void) | undefined;
    onMaskClick?: (() => void) | undefined;
    getContainer?: GetContainer | undefined;
} & {
    selectionMode?: undefined;
    onConfirm?: undefined;
}) | ({
    title?: React.ReactNode;
    confirmText?: string | undefined;
    weekStartsOn?: "Monday" | "Sunday" | undefined;
    renderTop?: false | import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    renderDate?: import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    renderBottom?: false | import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    allowClear?: boolean | undefined;
    max?: Date | undefined;
    min?: Date | undefined;
    shouldDisableDate?: ((date: Date) => boolean) | undefined;
} & {
    selectionMode: "single";
    value?: Date | null | undefined;
    defaultValue?: Date | null | undefined;
    onChange?: ((val: Date | null) => void) | undefined;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<never, string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & {
    visible?: boolean | undefined;
    confirmText?: string | undefined;
    popupClassName?: string | undefined;
    popupStyle?: React.CSSProperties | undefined;
    popupBodyStyle?: React.CSSProperties | undefined;
    forceRender?: true | undefined;
    closeOnMaskClick?: boolean | undefined;
    onClose?: (() => void) | undefined;
    onMaskClick?: (() => void) | undefined;
    getContainer?: GetContainer | undefined;
} & {
    selectionMode: 'single';
    onConfirm?: ((val: Date | null) => void) | undefined;
}) | ({
    title?: React.ReactNode;
    confirmText?: string | undefined;
    weekStartsOn?: "Monday" | "Sunday" | undefined;
    renderTop?: false | import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    renderDate?: import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    renderBottom?: false | import("../calendar-picker-view/calendar-picker-view").CalendarPickerViewColumRender | undefined;
    allowClear?: boolean | undefined;
    max?: Date | undefined;
    min?: Date | undefined;
    shouldDisableDate?: ((date: Date) => boolean) | undefined;
} & {
    selectionMode: "range";
    value?: [Date, Date] | null | undefined;
    defaultValue?: [Date, Date] | null | undefined;
    onChange?: ((val: [Date, Date] | null) => void) | undefined;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<never, string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & {
    visible?: boolean | undefined;
    confirmText?: string | undefined;
    popupClassName?: string | undefined;
    popupStyle?: React.CSSProperties | undefined;
    popupBodyStyle?: React.CSSProperties | undefined;
    forceRender?: true | undefined;
    closeOnMaskClick?: boolean | undefined;
    onClose?: (() => void) | undefined;
    onMaskClick?: (() => void) | undefined;
    getContainer?: GetContainer | undefined;
} & {
    selectionMode: 'range';
    onConfirm?: ((val: [Date, Date] | null) => void) | undefined;
}) | Omit<never, "ref"> | Omit<never, "ref"> | Omit<never, "ref"> | Omit<never, "ref"> | Omit<never, "ref"> | Omit<never, "ref">) & React.RefAttributes<CalendarPickerViewRef>>;
