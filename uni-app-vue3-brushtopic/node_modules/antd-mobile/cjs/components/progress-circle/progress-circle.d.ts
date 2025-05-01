import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type ProgressCircleProps = {
    percent?: number;
    children?: ReactNode;
} & NativeProps<'--size' | '--track-width' | '--track-color' | '--fill-color'>;
export declare const ProgressCircle: FC<ProgressCircleProps>;
