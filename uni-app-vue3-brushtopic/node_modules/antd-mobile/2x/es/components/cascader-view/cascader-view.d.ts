import type { FC, ReactNode } from 'react';
import { CheckListValue } from '../check-list';
import { NativeProps } from '../../utils/native-props';
import type { FieldNamesType, BaseOptionType } from '../../hooks';
export declare type CascaderValue = CheckListValue;
export declare type CascaderOption = {
    label?: string;
    value?: string;
    disabled?: boolean;
    children?: CascaderOption[];
} & BaseOptionType;
export declare type CascaderValueExtend = {
    items: (CascaderOption | null)[];
    isLeaf: boolean;
};
export declare type CascaderViewProps = {
    options: CascaderOption[];
    value?: CascaderValue[];
    defaultValue?: CascaderValue[];
    onChange?: (value: CascaderValue[], extend: CascaderValueExtend) => void;
    placeholder?: string | ((index: number) => string);
    onTabsChange?: (index: number) => void;
    activeIcon?: ReactNode;
    loading?: boolean;
    fieldNames?: FieldNamesType;
} & NativeProps<'--height'>;
export declare const CascaderView: FC<CascaderViewProps>;
