import type { FC } from 'react';
import { NativeProps } from '../../utils/native-props';
import type { FieldNamesType } from '../../hooks';
export declare type TreeSelectOption = {
    label?: string;
    value?: string;
    children?: TreeSelectOption[];
} & {
    [key: string]: any;
};
export declare type TreeSelectProps = {
    options: TreeSelectOption[];
    defaultValue?: string[];
    value?: string[];
    onChange?: (value: string[], extend: {
        options: TreeSelectOption[];
    }) => void;
    fieldNames?: FieldNamesType;
} & NativeProps;
export declare const TreeSelect: FC<TreeSelectProps>;
