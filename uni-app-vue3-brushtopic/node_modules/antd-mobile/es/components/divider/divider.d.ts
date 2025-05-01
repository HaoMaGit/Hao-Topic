import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type DividerProps = {
    contentPosition?: 'left' | 'right' | 'center';
    direction?: 'horizontal' | 'vertical';
    children?: ReactNode;
} & NativeProps;
export declare const Divider: FC<DividerProps>;
