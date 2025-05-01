import React from 'react';
import type { ReactNode } from 'react';
import type { PickerProps, PickerRef, PickerActions } from '../picker';
import { NativeProps } from '../../utils/native-props';
import type { Precision, DatePickerFilter } from './date-picker-utils';
import type { RenderLabel } from '../date-picker-view/date-picker-view';
import type { PickerDate } from './util';
export declare type DatePickerRef = PickerRef;
export declare type DatePickerProps = Pick<PickerProps, 'onCancel' | 'onClose' | 'closeOnMaskClick' | 'visible' | 'confirmText' | 'cancelText' | 'getContainer' | 'loading' | 'loadingContent' | 'afterShow' | 'afterClose' | 'onClick' | 'title' | 'stopPropagation' | 'style' | 'mouseWheel' | 'forceRender' | 'destroyOnClose'> & {
    value?: PickerDate | null;
    defaultValue?: PickerDate | null;
    onSelect?: (value: PickerDate) => void;
    onConfirm?: (value: PickerDate) => void;
    min?: PickerDate;
    max?: PickerDate;
    precision?: Precision;
    children?: (value: PickerDate | null, actions: PickerActions) => ReactNode;
    renderLabel?: RenderLabel;
    filter?: DatePickerFilter;
    tillNow?: boolean;
} & NativeProps;
export declare const DatePicker: React.ForwardRefExoticComponent<Pick<PickerProps, "style" | "title" | "onClick" | "visible" | "destroyOnClose" | "forceRender" | "getContainer" | "afterShow" | "afterClose" | "stopPropagation" | "closeOnMaskClick" | "onClose" | "cancelText" | "loading" | "confirmText" | "mouseWheel" | "loadingContent" | "onCancel"> & {
    value?: PickerDate | null | undefined;
    defaultValue?: PickerDate | null | undefined;
    onSelect?: ((value: PickerDate) => void) | undefined;
    onConfirm?: ((value: PickerDate) => void) | undefined;
    min?: PickerDate | undefined;
    max?: PickerDate | undefined;
    precision?: Precision | undefined;
    children?: ((value: PickerDate | null, actions: PickerActions) => ReactNode) | undefined;
    renderLabel?: RenderLabel | undefined;
    filter?: Partial<Record<Precision, (val: number, extend: {
        date: Date;
    }) => boolean>> | undefined;
    tillNow?: boolean | undefined;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<never, string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<PickerActions>>;
