import type { ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type FloatingPanelRef = {
    setHeight: (height: number, options?: {
        immediate?: boolean;
    }) => void;
};
export declare type FloatingPanelProps = {
    anchors: number[];
    children: ReactNode;
    onHeightChange?: (height: number, animating: boolean) => void;
    handleDraggingOfContent?: boolean;
    placement?: 'bottom' | 'top';
} & NativeProps<'--border-radius' | '--z-index' | '--header-height'>;
export declare const FloatingPanel: React.ForwardRefExoticComponent<{
    anchors: number[];
    children: ReactNode;
    onHeightChange?: ((height: number, animating: boolean) => void) | undefined;
    handleDraggingOfContent?: boolean | undefined;
    placement?: "bottom" | "top" | undefined;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<"--z-index" | "--border-radius" | "--header-height", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<FloatingPanelRef>>;
