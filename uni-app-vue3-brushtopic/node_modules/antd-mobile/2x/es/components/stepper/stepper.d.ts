import React from 'react';
import { NativeProps } from '../../utils/native-props';
import { InputProps, InputRef } from '../input';
declare type ValueProps<ValueType> = {
    allowEmpty: true;
    value?: ValueType | null;
    defaultValue?: ValueType | null;
    onChange?: (value: ValueType | null) => void;
};
declare type ValuePropsWithNull<ValueType> = {
    allowEmpty?: false;
    value?: ValueType;
    defaultValue?: ValueType;
    onChange?: (value: ValueType) => void;
};
export declare type BaseStepperProps<ValueType> = Pick<InputProps, 'onFocus' | 'onBlur'> & (ValuePropsWithNull<ValueType> | ValueProps<ValueType>) & {
    min?: ValueType;
    max?: ValueType;
    step?: ValueType;
    digits?: number;
    disabled?: boolean;
    inputReadOnly?: boolean;
    parser?: (text: string) => ValueType;
    formatter?: (value?: ValueType) => string;
} & NativeProps<'--height' | '--input-width' | '--input-font-size' | '--input-background-color' | '--border-radius' | '--border' | '--border-inner' | '--active-border' | '--button-font-size' | '--button-background-color' | '--button-width' | '--input-font-color' | '--button-text-color'>;
export declare type NumberStepperProps = BaseStepperProps<number> & {
    stringMode?: false;
};
export declare type StringStepperProps = BaseStepperProps<string> & {
    stringMode: true;
};
export declare type StepperProps = NumberStepperProps | StringStepperProps;
export declare type StepperRef = Pick<InputRef, 'blur' | 'focus' | 'nativeElement'>;
export declare function InnerStepper<ValueType extends number | string>(p: StepperProps, ref: React.ForwardedRef<StepperRef>): React.ReactElement<any, string | React.JSXElementConstructor<any>>;
export declare const Stepper: React.ForwardRefExoticComponent<StepperProps & React.RefAttributes<StepperRef>>;
export {};
