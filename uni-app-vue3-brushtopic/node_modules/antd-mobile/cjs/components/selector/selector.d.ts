import React from 'react';
import type { ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
import { GridProps } from '../grid';
import type { FieldNamesType, BaseOptionType } from '../../hooks';
declare type SelectorValue = string | number;
export declare type SelectorOption<V> = {
    label?: ReactNode;
    description?: ReactNode;
    value?: V;
    disabled?: boolean;
} & BaseOptionType;
export declare type SelectorProps<V> = {
    options: SelectorOption<V>[];
    columns?: GridProps['columns'];
    multiple?: boolean;
    disabled?: boolean;
    defaultValue?: V[];
    value?: V[];
    onChange?: (v: V[], extend: {
        items: SelectorOption<V>[];
    }) => void;
    showCheckMark?: boolean;
    fieldNames?: FieldNamesType;
} & NativeProps<'--color' | '--checked-color' | '--text-color' | '--checked-text-color' | '--border' | '--checked-border' | '--border-radius' | '--padding' | '--gap' | '--gap-vertical' | '--gap-horizontal'>;
export declare const Selector: <V extends SelectorValue>(p: SelectorProps<V>) => React.ReactElement<any, string | React.JSXElementConstructor<any>>;
export {};
