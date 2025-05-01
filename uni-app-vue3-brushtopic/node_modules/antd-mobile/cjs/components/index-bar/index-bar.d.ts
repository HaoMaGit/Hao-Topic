import React from 'react';
import type { ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type IndexBarProps = {
    sticky?: boolean;
    onIndexChange?: (index: string) => void;
    children?: ReactNode;
} & NativeProps<'--sticky-offset-top'>;
export declare type IndexBarRef = {
    scrollTo: (index: string) => void;
};
export declare const IndexBar: React.ForwardRefExoticComponent<{
    sticky?: boolean | undefined;
    onIndexChange?: ((index: string) => void) | undefined;
    children?: ReactNode;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<"--sticky-offset-top", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<IndexBarRef>>;
