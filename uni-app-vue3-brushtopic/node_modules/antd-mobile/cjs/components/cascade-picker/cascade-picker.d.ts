import React from 'react';
import type { PickerProps, PickerRef } from '../picker';
export declare type CascadePickerRef = PickerRef;
export declare type CascadePickerOption = {
    label: string;
    value: string;
    children?: CascadePickerOption[];
};
export declare type CascadePickerProps = Omit<PickerProps, 'columns'> & {
    options: CascadePickerOption[];
};
export declare const CascadePicker: React.ForwardRefExoticComponent<Omit<PickerProps, "columns"> & {
    options: CascadePickerOption[];
} & React.RefAttributes<import("../picker").PickerActions>>;
