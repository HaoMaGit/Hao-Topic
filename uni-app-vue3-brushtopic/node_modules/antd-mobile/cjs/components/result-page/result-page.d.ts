import React from 'react';
import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
interface ResultPageDetail {
    label: ReactNode;
    value: ReactNode;
    bold?: boolean;
}
declare type ResultPageDetails = ResultPageDetail[];
declare type OnClick = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void | Promise<void>;
export declare type ResultPageProps = {
    status?: 'success' | 'error' | 'info' | 'waiting' | 'warning';
    title: ReactNode;
    description?: ReactNode;
    icon?: ReactNode;
    details?: ResultPageDetails | null;
    children?: ReactNode;
    primaryButtonText?: ReactNode;
    secondaryButtonText?: ReactNode;
    onPrimaryButtonClick?: OnClick;
    onSecondaryButtonClick?: OnClick;
} & NativeProps<'--background-color'>;
export declare const ResultPage: FC<ResultPageProps>;
export {};
