import React from 'react';
import type { ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type ListProps = {
    header?: ReactNode;
    mode?: 'default' | 'card';
    children?: ReactNode;
} & NativeProps<'--active-background-color' | '--align-items' | '--border-bottom' | '--border-inner' | '--border-top' | '--extra-max-width' | '--font-size' | '--header-font-size' | '--padding-left' | '--padding-right' | '--prefix-padding-right' | '--prefix-width'>;
export declare type ListRef = {
    nativeElement: HTMLDivElement | null;
};
export declare const List: React.ForwardRefExoticComponent<{
    header?: ReactNode;
    mode?: "default" | "card" | undefined;
    children?: ReactNode;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<"--active-background-color" | "--align-items" | "--border-bottom" | "--border-inner" | "--border-top" | "--extra-max-width" | "--font-size" | "--header-font-size" | "--padding-left" | "--padding-right" | "--prefix-padding-right" | "--prefix-width", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<ListRef>>;
