import React from 'react';
import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
declare type Offset = {
    x: number;
    y: number;
};
export declare type FloatingBubbleProps = {
    onClick?: (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
    axis?: 'x' | 'y' | 'xy' | 'lock';
    magnetic?: 'x' | 'y';
    children?: ReactNode;
    offset?: Offset;
    defaultOffset?: Offset;
    onOffsetChange?: (offset: Offset) => void;
} & NativeProps<'--initial-position-left' | '--initial-position-right' | '--initial-position-top' | '--initial-position-bottom' | '--z-index' | '--edge-distance' | '--size' | '--border-radius' | '--background'>;
export declare const FloatingBubble: FC<FloatingBubbleProps>;
export {};
