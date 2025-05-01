import type { FC, ReactNode, ReactElement } from 'react';
import type { StoreValue } from 'rc-field-form/es/interface';
export interface FormArrayField {
    index: number;
    key: number;
}
export interface FormArrayOperation {
    add: (defaultValue?: StoreValue, insertIndex?: number) => void;
    remove: (index: number | number[]) => void;
    move: (from: number, to: number) => void;
}
export interface FormArrayProps {
    name: string | number | (string | number)[];
    initialValue?: any[];
    renderHeader?: (field: FormArrayField, operation: FormArrayOperation) => ReactNode;
    onAdd?: (operation: FormArrayOperation) => void;
    renderAdd?: () => ReactNode;
    children: (fields: FormArrayField[], operation: FormArrayOperation) => ReactElement[];
}
export declare const FormArray: FC<FormArrayProps>;
