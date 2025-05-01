import React from 'react';
import type { FC, ReactNode } from 'react';
import type { FormInstance } from 'rc-field-form';
import type { NamePath } from 'rc-field-form/es/interface';
declare type RenderChildren<Values = any> = (changedValues: Record<string, any>, form: FormInstance<Values>) => ReactNode;
declare type ChildrenType<Values = any> = RenderChildren<Values>;
export interface FormSubscribeProps {
    to: NamePath[];
    children: ChildrenType;
}
export declare const FormSubscribe: FC<FormSubscribeProps>;
export declare const Watcher: React.NamedExoticComponent<{
    form: FormInstance;
    namePath: NamePath;
    onChange: () => void;
}>;
export {};
