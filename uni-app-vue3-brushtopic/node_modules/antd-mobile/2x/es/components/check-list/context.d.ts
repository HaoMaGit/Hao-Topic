import { CheckListValue } from '.';
import type { ReactNode } from 'react';
export declare const CheckListContext: import("react").Context<{
    value: CheckListValue[];
    check: (val: CheckListValue) => void;
    uncheck: (val: CheckListValue) => void;
    activeIcon?: ReactNode;
    extra?: ((active: boolean) => ReactNode) | undefined;
    disabled?: boolean | undefined;
    readOnly?: boolean | undefined;
} | null>;
