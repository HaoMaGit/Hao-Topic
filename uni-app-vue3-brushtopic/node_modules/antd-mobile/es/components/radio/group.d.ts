import type { FC, ReactNode } from 'react';
import { RadioValue } from '.';
export interface RadioGroupProps {
    value?: RadioValue | null;
    onChange?: (val: RadioValue) => void;
    defaultValue?: RadioValue | null;
    disabled?: boolean;
    children?: ReactNode;
}
export declare const Group: FC<RadioGroupProps>;
