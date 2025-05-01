import React from 'react';
import type { ReactNode } from 'react';
import { PopoverProps, PopoverRef } from './popover';
export declare type Action = {
    text: ReactNode;
    icon?: ReactNode;
    disabled?: boolean;
    key?: string | number;
    onClick?: () => void;
};
export declare type PopoverMenuProps = Omit<PopoverProps, 'content'> & {
    actions: Action[];
    maxCount?: number;
    onAction?: (item: Action) => void;
};
export declare const PopoverMenu: React.ForwardRefExoticComponent<Omit<PopoverProps, "content"> & {
    actions: Action[];
    maxCount?: number | undefined;
    onAction?: ((item: Action) => void) | undefined;
} & React.RefAttributes<PopoverRef>>;
