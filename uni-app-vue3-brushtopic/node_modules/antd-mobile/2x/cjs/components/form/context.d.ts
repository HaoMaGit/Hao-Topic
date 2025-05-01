import React from 'react';
import { FormLayout } from '.';
import type { Meta, InternalNamePath } from 'rc-field-form/lib/interface';
export declare type FormContextType = {
    name?: string;
    hasFeedback: boolean;
    layout: FormLayout;
    requiredMarkStyle: 'asterisk' | 'text-required' | 'text-optional' | 'none';
    disabled: boolean;
};
export declare const defaultFormContext: FormContextType;
export declare const FormContext: React.Context<FormContextType>;
export declare type OnSubMetaChange = (meta: Meta & {
    destroy?: boolean;
}, namePath: InternalNamePath) => void;
export declare const NoStyleItemContext: React.Context<OnSubMetaChange | null>;
