import type { ReactElement, ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
import { GetContainer } from '../../utils/render-to-container';
import { PropagationEvent } from '../../utils/with-stop-propagation';
import { DeprecatedPlacement, Placement } from './index';
export declare type PopoverProps = {
    defaultVisible?: boolean;
    visible?: boolean;
    onVisibleChange?: (visible: boolean) => void;
    getContainer?: GetContainer;
    destroyOnHide?: boolean;
    children: ReactElement;
    mode?: 'light' | 'dark';
    trigger?: 'click';
    placement?: Placement | DeprecatedPlacement;
    stopPropagation?: PropagationEvent[];
    content: ReactNode;
} & NativeProps<'--z-index' | '--arrow-size'>;
export declare type PopoverRef = {
    show: () => void;
    hide: () => void;
    visible: boolean;
};
export declare const Popover: React.ForwardRefExoticComponent<{
    defaultVisible?: boolean | undefined;
    visible?: boolean | undefined;
    onVisibleChange?: ((visible: boolean) => void) | undefined;
    getContainer?: GetContainer | undefined;
    destroyOnHide?: boolean | undefined;
    children: ReactElement;
    mode?: "dark" | "light" | undefined;
    trigger?: "click" | undefined;
    placement?: DeprecatedPlacement | Placement | undefined;
    stopPropagation?: PropagationEvent[] | undefined;
    content: ReactNode;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<"--z-index" | "--arrow-size", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<PopoverRef>>;
