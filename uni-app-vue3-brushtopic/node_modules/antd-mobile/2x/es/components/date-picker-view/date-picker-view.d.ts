import type { FC, ReactNode } from 'react';
import type { PickerViewProps } from '../picker-view';
import { NativeProps } from '../../utils/native-props';
import type { Precision, DatePickerFilter } from '../date-picker/date-picker-utils';
import type { PickerDate } from '../date-picker/util';
export declare type RenderLabel = (type: Precision | 'now', data: number) => ReactNode;
export declare type DatePickerViewProps = Pick<PickerViewProps, 'style' | 'mouseWheel' | 'loading' | 'loadingContent'> & {
    value?: PickerDate;
    defaultValue?: PickerDate;
    onChange?: (value: PickerDate) => void;
    min?: PickerDate;
    max?: PickerDate;
    precision?: Precision;
    renderLabel?: RenderLabel;
    filter?: DatePickerFilter;
    tillNow?: boolean;
} & NativeProps;
export declare const DatePickerView: FC<DatePickerViewProps>;
