import React from 'react';
import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
declare type Props = {
    onClick?: (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
    children?: ReactNode;
} & NativeProps;
export declare const SwiperItem: FC<Props>;
export {};
