import type { FC, ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type ListItemProps = {
    title?: ReactNode;
    children?: ReactNode;
    description?: ReactNode;
    prefix?: ReactNode;
    extra?: ReactNode;
    clickable?: boolean;
    arrowIcon?: boolean | ReactNode;
    disabled?: boolean;
    onClick?: (e: React.MouseEvent<HTMLElement>) => void;
    /**
     * @deprecated use `arrowIcon` instead
     */
    arrow?: boolean | ReactNode;
} & NativeProps<'--prefix-width' | '--align-items' | '--active-background-color'>;
export declare const ListItem: FC<ListItemProps>;
