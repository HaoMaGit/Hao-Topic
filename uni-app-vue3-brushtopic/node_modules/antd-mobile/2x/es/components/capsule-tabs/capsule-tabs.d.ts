import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type CapsuleTabProps = {
    title: ReactNode;
    disabled?: boolean;
    forceRender?: boolean;
    destroyOnClose?: boolean;
    children?: ReactNode;
} & NativeProps;
export declare const CapsuleTab: FC<CapsuleTabProps>;
export declare type CapsuleTabsProps = {
    activeKey?: string | null;
    defaultActiveKey?: string | null;
    onChange?: (key: string) => void;
    children?: ReactNode;
} & NativeProps;
export declare const CapsuleTabs: FC<CapsuleTabsProps>;
