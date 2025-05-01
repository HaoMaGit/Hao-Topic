import type { ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
import { PopupProps } from '../popup';
export declare type DropdownProps = {
    activeKey?: string | null;
    defaultActiveKey?: string | null;
    closeOnMaskClick?: boolean;
    closeOnClickAway?: boolean;
    onChange?: (key: string | null) => void;
    arrowIcon?: ReactNode;
    /**
     * @deprecated use `arrowIcon` instead
     */
    arrow?: ReactNode;
    getContainer?: PopupProps['getContainer'];
} & NativeProps;
export declare type DropdownRef = {
    close: () => void;
};
declare const Dropdown: React.ForwardRefExoticComponent<{
    activeKey?: string | null | undefined;
    defaultActiveKey?: string | null | undefined;
    closeOnMaskClick?: boolean | undefined;
    closeOnClickAway?: boolean | undefined;
    onChange?: ((key: string | null) => void) | undefined;
    arrowIcon?: ReactNode;
    /**
     * @deprecated use `arrowIcon` instead
     */
    arrow?: ReactNode;
    getContainer?: PopupProps['getContainer'];
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<never, string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & {
    children?: ReactNode;
} & React.RefAttributes<DropdownRef>>;
export default Dropdown;
