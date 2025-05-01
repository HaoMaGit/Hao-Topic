import type { FC } from 'react';
import { NativeProps } from '../../utils/native-props';
import { TreeSelectOption } from '.';
import type { FieldNamesType } from '../../hooks';
export declare type MultipleProps = {
    options: TreeSelectOption[];
    defaultValue?: string[];
    value?: string[];
    onChange?: (value: string[], nodes: TreeSelectOption[]) => void;
    selectAllText?: string[];
    fieldNames?: FieldNamesType;
    expandKeys?: string[];
    defaultExpandKeys?: string[];
    onExpand?: (expandedKeys: string[], nodes: TreeSelectOption[]) => void;
} & NativeProps;
export declare const Multiple: FC<MultipleProps>;
