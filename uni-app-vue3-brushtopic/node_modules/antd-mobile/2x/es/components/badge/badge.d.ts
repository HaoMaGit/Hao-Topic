import React from 'react';
import type { FC, ReactNode, CSSProperties } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare const dot: React.JSX.Element;
export declare type BadgeProps = {
    content?: ReactNode | typeof dot;
    color?: string;
    bordered?: boolean;
    children?: ReactNode;
    wrapperClassName?: string;
    wrapperStyle?: CSSProperties;
} & NativeProps<'--right' | '--top' | '--color'>;
export declare const Badge: FC<BadgeProps>;
