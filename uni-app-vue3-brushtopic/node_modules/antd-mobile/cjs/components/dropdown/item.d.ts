import type { FC, ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type DropdownItemProps = {
    key: string;
    title: ReactNode;
    active?: boolean;
    highlight?: boolean;
    forceRender?: boolean;
    destroyOnClose?: boolean;
    onClick?: (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
    arrowIcon?: ReactNode;
    /**
     * @deprecated use `arrowIcon` instead
     */
    arrow?: ReactNode;
    children?: ReactNode;
} & NativeProps;
declare const Item: FC<DropdownItemProps>;
export default Item;
declare type DropdownItemChildrenWrapProps = {
    onClick?: () => void;
} & Pick<DropdownItemProps, 'active' | 'forceRender' | 'destroyOnClose' | 'children'>;
export declare const ItemChildrenWrap: FC<DropdownItemChildrenWrapProps>;
