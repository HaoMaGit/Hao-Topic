import type { ReactElement } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
import type { NumberKeyboardProps } from '../number-keyboard';
export declare type PasscodeInputProps = {
    value?: string;
    defaultValue?: string;
    length?: number;
    plain?: boolean;
    error?: boolean;
    caret?: boolean;
    seperated?: boolean;
    keyboard?: ReactElement<NumberKeyboardProps>;
    inputMode?: 'numeric' | 'text';
    onBlur?: () => void;
    onFocus?: () => void;
    onChange?: (val: string) => void;
    onFill?: (val: string) => void;
} & NativeProps<'--cell-gap' | '--cell-size' | '--dot-size' | '--border-color' | '--border-radius'>;
export declare type PasscodeInputRef = {
    focus: () => void;
    blur: () => void;
};
export declare const PasscodeInput: React.ForwardRefExoticComponent<{
    value?: string | undefined;
    defaultValue?: string | undefined;
    length?: number | undefined;
    plain?: boolean | undefined;
    error?: boolean | undefined;
    caret?: boolean | undefined;
    seperated?: boolean | undefined;
    keyboard?: ReactElement<NumberKeyboardProps, string | React.JSXElementConstructor<any>> | undefined;
    inputMode?: "text" | "numeric" | undefined;
    onBlur?: (() => void) | undefined;
    onFocus?: (() => void) | undefined;
    onChange?: ((val: string) => void) | undefined;
    onFill?: ((val: string) => void) | undefined;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<"--border-radius" | "--border-color" | "--cell-size" | "--dot-size" | "--cell-gap", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<PasscodeInputRef>>;
