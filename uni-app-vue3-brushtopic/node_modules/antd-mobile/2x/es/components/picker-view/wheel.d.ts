import React from 'react';
import type { ReactNode } from 'react';
import { PickerColumnItem, PickerValue } from './index';
declare type Props = {
    index: number;
    column: PickerColumnItem[];
    value: PickerValue;
    onSelect: (value: PickerValue, index: number) => void;
    renderLabel: (item: PickerColumnItem) => ReactNode;
    mouseWheel: boolean;
};
export declare const Wheel: React.NamedExoticComponent<Props>;
export {};
