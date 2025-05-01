import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type TabProps = {
    title: ReactNode;
    disabled?: boolean;
    forceRender?: boolean;
    destroyOnClose?: boolean;
    children?: ReactNode;
} & NativeProps;
export declare const Tab: FC<TabProps>;
export declare type TabsProps = {
    activeKey?: string | null;
    defaultActiveKey?: string | null;
    activeLineMode?: 'auto' | 'full' | 'fixed';
    stretch?: boolean;
    onChange?: (key: string) => void;
    children?: ReactNode;
    direction?: 'ltr' | 'rtl';
    /**
     * @experimental Support disabled auto scroll when Tabs header content change.
     * This API name or function may change in the future.
     * Please lock the version if you want to use it.
     */
    autoScroll?: boolean;
} & NativeProps<'--fixed-active-line-width' | '--active-line-height' | '--active-line-border-radius' | '--title-font-size' | '--content-padding' | '--active-title-color' | '--active-line-color'>;
export declare const Tabs: FC<TabsProps>;
