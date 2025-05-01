import type { FC, ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type CollapsePanelProps = {
    key: string;
    title: ReactNode;
    disabled?: boolean;
    forceRender?: boolean;
    destroyOnClose?: boolean;
    onClick?: (event: React.MouseEvent<Element, MouseEvent>) => void;
    arrowIcon?: ReactNode | ((active: boolean) => ReactNode);
    children?: ReactNode;
    /**
     * @deprecated use `arrowIcon` instead
     */
    arrow?: ReactNode | ((active: boolean) => ReactNode);
} & NativeProps;
export declare const CollapsePanel: FC<CollapsePanelProps>;
declare type ValueProps<T> = {
    activeKey?: T;
    defaultActiveKey?: T;
    onChange?: (activeKey: T) => void;
    arrowIcon?: ReactNode | ((active: boolean) => ReactNode);
    /**
     * @deprecated use `arrowIcon` instead
     */
    arrow?: ReactNode | ((active: boolean) => ReactNode);
};
export declare type CollapseProps = (({
    accordion?: false;
} & ValueProps<string[]>) | ({
    accordion: true;
} & ValueProps<string | null>)) & {
    children?: ReactNode;
} & NativeProps;
export declare const Collapse: FC<CollapseProps>;
export {};
