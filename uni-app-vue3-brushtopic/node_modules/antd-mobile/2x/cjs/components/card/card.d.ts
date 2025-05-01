import type { CSSProperties, FC, ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type CardProps = {
    title?: ReactNode;
    icon?: ReactNode;
    extra?: ReactNode;
    headerStyle?: CSSProperties;
    headerClassName?: string;
    bodyStyle?: CSSProperties;
    bodyClassName?: string;
    onClick?: (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
    onBodyClick?: (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
    onHeaderClick?: (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
    children?: ReactNode;
} & NativeProps;
export declare const Card: FC<CardProps>;
