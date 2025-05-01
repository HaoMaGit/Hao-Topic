import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type StepProps = {
    title?: ReactNode;
    description?: ReactNode;
    icon?: ReactNode;
    status?: 'wait' | 'process' | 'finish' | 'error';
} & NativeProps;
export declare const Step: FC<StepProps>;
