import type { ReactElement } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
import type { InputProps } from '../input';
import { NumberKeyboardProps } from '../number-keyboard';
export declare type VirtualInputProps = {
    onFocus?: () => void;
    onBlur?: () => void;
    onClick?: (e: React.MouseEvent<HTMLDivElement>) => void;
    keyboard?: ReactElement<NumberKeyboardProps>;
    clearable?: boolean;
    onClear?: () => void;
} & Pick<InputProps, 'value' | 'onChange' | 'placeholder' | 'disabled' | 'clearIcon'> & NativeProps<'--font-size' | '--color' | '--placeholder-color' | '--disabled-color' | '--text-align' | '--caret-width' | '--caret-color'>;
export declare type VirtualInputRef = {
    focus: () => void;
    blur: () => void;
};
export declare const VirtualInput: React.ForwardRefExoticComponent<{
    onFocus?: (() => void) | undefined;
    onBlur?: (() => void) | undefined;
    onClick?: ((e: React.MouseEvent<HTMLDivElement>) => void) | undefined;
    keyboard?: ReactElement<NumberKeyboardProps, string | React.JSXElementConstructor<any>> | undefined;
    clearable?: boolean | undefined;
    onClear?: (() => void) | undefined;
} & Pick<InputProps, "value" | "onChange" | "disabled" | "placeholder" | "clearIcon"> & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<"--color" | "--font-size" | "--placeholder-color" | "--text-align" | "--disabled-color" | "--caret-width" | "--caret-color", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<VirtualInputRef>>;
