import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type SwitchProps = {
    loading?: boolean;
    disabled?: boolean;
    checked?: boolean;
    defaultChecked?: boolean;
    /** @deprecated use `onChange` instead */
    beforeChange?: (val: boolean) => Promise<void>;
    onChange?: (checked: boolean) => void | Promise<void>;
    checkedText?: ReactNode;
    uncheckedText?: ReactNode;
} & NativeProps<'--checked-color' | '--width' | '--height' | '--border-width'>;
export declare const Switch: FC<SwitchProps>;
