import type { ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
import { InputProps, InputRef } from '../input';
export declare type SearchBarRef = InputRef;
export declare type SearchBarProps = Pick<InputProps, 'onFocus' | 'onBlur' | 'onClear' | 'onCompositionStart' | 'onCompositionEnd' | 'autoFocus'> & {
    value?: string;
    defaultValue?: string;
    maxLength?: number;
    placeholder?: string;
    clearable?: boolean;
    onlyShowClearWhenFocus?: boolean;
    showCancelButton?: boolean | ((focus: boolean, value: string) => boolean);
    cancelText?: string;
    searchIcon?: ReactNode;
    /**
     * @deprecated use `searchIcon` instead
     */
    icon?: ReactNode;
    clearOnCancel?: boolean;
    onSearch?: (val: string) => void;
    onChange?: (val: string) => void;
    onCancel?: () => void;
} & NativeProps<'--background' | '--border-radius' | '--placeholder-color' | '--height' | '--padding-left'>;
export declare const SearchBar: React.ForwardRefExoticComponent<Pick<InputProps, "onCompositionEnd" | "onCompositionStart" | "onFocus" | "onBlur" | "autoFocus" | "onClear"> & {
    value?: string | undefined;
    defaultValue?: string | undefined;
    maxLength?: number | undefined;
    placeholder?: string | undefined;
    clearable?: boolean | undefined;
    onlyShowClearWhenFocus?: boolean | undefined;
    showCancelButton?: boolean | ((focus: boolean, value: string) => boolean) | undefined;
    cancelText?: string | undefined;
    searchIcon?: ReactNode;
    /**
     * @deprecated use `searchIcon` instead
     */
    icon?: ReactNode;
    clearOnCancel?: boolean | undefined;
    onSearch?: ((val: string) => void) | undefined;
    onChange?: ((val: string) => void) | undefined;
    onCancel?: (() => void) | undefined;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<"--height" | "--border-radius" | "--padding-left" | "--background" | "--placeholder-color", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<InputRef>>;
