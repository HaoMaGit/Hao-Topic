import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type IndexBarPanelProps = {
    index: string;
    title?: ReactNode;
    brief?: ReactNode;
    children?: ReactNode;
} & NativeProps;
export declare const Panel: FC<IndexBarPanelProps>;
