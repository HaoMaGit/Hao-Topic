import type { FC, ReactNode } from 'react';
import { CheckboxValue } from '.';
export interface CheckboxGroupProps {
    value?: CheckboxValue[];
    onChange?: (val: CheckboxValue[]) => void;
    defaultValue?: CheckboxValue[];
    disabled?: boolean;
    children?: ReactNode;
}
export declare const Group: FC<CheckboxGroupProps>;
