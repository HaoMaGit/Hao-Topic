import type { FC, ReactNode } from 'react';
import { BadgeProps } from '../badge';
import { NativeProps } from '../../utils/native-props';
export declare type SideBarItemProps = {
    title?: ReactNode;
    disabled?: boolean;
    badge?: BadgeProps['content'];
} & NativeProps;
export declare const SideBarItem: FC<SideBarItemProps>;
export declare type SideBarProps = {
    activeKey?: string | null;
    defaultActiveKey?: string | null;
    onChange?: (key: string) => void;
    children?: ReactNode;
} & NativeProps<'--width' | '--height' | '--item-border-radius' | '--background-color'>;
export declare const SideBar: FC<SideBarProps>;
