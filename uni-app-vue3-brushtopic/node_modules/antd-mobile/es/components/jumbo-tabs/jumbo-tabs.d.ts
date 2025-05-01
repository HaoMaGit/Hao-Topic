import type { ReactNode, FC } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type JumboTabProps = {
    title: ReactNode;
    description: ReactNode;
    disabled?: boolean;
    forceRender?: boolean;
    destroyOnClose?: boolean;
    children?: ReactNode;
} & NativeProps;
export declare const JumboTab: FC<JumboTabProps>;
export declare type JumboTabsProps = {
    activeKey?: string | null;
    defaultActiveKey?: string | null;
    onChange?: (key: string) => void;
    children?: ReactNode;
} & NativeProps;
export declare const JumboTabs: FC<JumboTabsProps>;
