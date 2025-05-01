import type { SegmentedLabeledOption as RcSegmentedLabeledOption, SegmentedProps as RCSegmentedProps, SegmentedRawOption } from 'rc-segmented';
import * as React from 'react';
import { NativeProps } from '../../utils/native-props';
export type { SegmentedValue } from 'rc-segmented';
interface SegmentedLabeledOptionWithoutIcon extends RcSegmentedLabeledOption {
    label: RcSegmentedLabeledOption['label'];
}
interface SegmentedLabeledOptionWithIcon extends Omit<RcSegmentedLabeledOption, 'label'> {
    label?: RcSegmentedLabeledOption['label'];
    /** Set icon for Segmented item */
    icon: React.ReactNode;
}
export declare type SegmentedLabeledOption = SegmentedLabeledOptionWithIcon | SegmentedLabeledOptionWithoutIcon;
interface InternalSegmentedProps extends Omit<RCSegmentedProps, 'size' | 'options'> {
    options: (SegmentedRawOption | SegmentedLabeledOption)[];
    /** Option to fit width to its parent's width */
    block?: boolean;
}
export declare type SegmentedProps = InternalSegmentedProps & NativeProps<'--segmented-background' | '--segmented-item-color' | '--segmented-item-selected-background' | '--segmented-item-selected-color' | '--segmented-item-disabled-color'>;
declare const Segmented: React.ForwardRefExoticComponent<Omit<SegmentedProps, "ref"> & React.RefAttributes<HTMLDivElement>>;
export { Segmented };
