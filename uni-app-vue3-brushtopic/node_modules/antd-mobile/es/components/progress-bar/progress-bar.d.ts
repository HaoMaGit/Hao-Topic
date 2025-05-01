import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type ProgressBarProps = {
    percent?: number;
    rounded?: boolean;
    text?: boolean | ReactNode | ((percent: number) => ReactNode);
} & NativeProps<'--track-width' | '--track-color' | '--fill-color' | '--text-width'>;
export declare const ProgressBar: FC<ProgressBarProps>;
