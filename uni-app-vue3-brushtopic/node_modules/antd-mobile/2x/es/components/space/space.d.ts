import React from 'react';
import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type SpaceProps = {
    direction?: 'horizontal' | 'vertical';
    align?: 'start' | 'end' | 'center' | 'baseline';
    justify?: 'start' | 'end' | 'center' | 'between' | 'around' | 'evenly' | 'stretch';
    wrap?: boolean;
    block?: boolean;
    onClick?: (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
    children?: ReactNode;
} & NativeProps<'--gap' | '--gap-vertical' | '--gap-horizontal'>;
export declare const Space: FC<SpaceProps>;
