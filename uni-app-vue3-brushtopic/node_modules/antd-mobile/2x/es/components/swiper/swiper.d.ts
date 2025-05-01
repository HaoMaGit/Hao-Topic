import React from 'react';
import type { ReactNode, ReactElement } from 'react';
import { NativeProps } from '../../utils/native-props';
import { PageIndicatorProps } from '../page-indicator';
declare const eventToPropRecord: {
    readonly mousedown: "onMouseDown";
    readonly mousemove: "onMouseMove";
    readonly mouseup: "onMouseUp";
};
declare type PropagationEvent = keyof typeof eventToPropRecord;
export declare type SwiperRef = {
    swipeTo: (index: number) => void;
    swipeNext: () => void;
    swipePrev: () => void;
};
export declare type SwiperProps = {
    defaultIndex?: number;
    allowTouchMove?: boolean;
    autoplay?: boolean | 'reverse';
    autoplayInterval?: number;
    loop?: boolean;
    direction?: 'horizontal' | 'vertical';
    onIndexChange?: (index: number) => void;
    indicatorProps?: Pick<PageIndicatorProps, 'color' | 'style' | 'className'>;
    indicator?: false | ((total: number, current: number) => ReactNode);
    slideSize?: number;
    trackOffset?: number;
    stuckAtBoundary?: boolean;
    rubberband?: boolean;
    stopPropagation?: PropagationEvent[];
    /**
     * Virtual scroll usage. Should work with renderProps `children`
     */
    total?: number;
    /**
     * renderProps is only work when `total` used
     */
    children?: ReactElement | ReactElement[] | ((index: number) => ReactElement);
} & NativeProps<'--height' | '--width' | '--border-radius' | '--track-padding'>;
export declare const Swiper: React.ForwardRefExoticComponent<{
    defaultIndex?: number | undefined;
    allowTouchMove?: boolean | undefined;
    autoplay?: boolean | "reverse" | undefined;
    autoplayInterval?: number | undefined;
    loop?: boolean | undefined;
    direction?: "vertical" | "horizontal" | undefined;
    onIndexChange?: ((index: number) => void) | undefined;
    indicatorProps?: Pick<PageIndicatorProps, "style" | "className" | "color"> | undefined;
    indicator?: false | ((total: number, current: number) => ReactNode) | undefined;
    slideSize?: number | undefined;
    trackOffset?: number | undefined;
    stuckAtBoundary?: boolean | undefined;
    rubberband?: boolean | undefined;
    stopPropagation?: ("mousedown" | "mousemove" | "mouseup")[] | undefined;
    /**
     * Virtual scroll usage. Should work with renderProps `children`
     */
    total?: number | undefined;
    /**
     * renderProps is only work when `total` used
     */
    children?: React.ReactElement<any, string | React.JSXElementConstructor<any>> | React.ReactElement<any, string | React.JSXElementConstructor<any>>[] | ((index: number) => ReactElement) | undefined;
} & {
    className?: string | undefined;
    style?: (React.CSSProperties & Partial<Record<"--width" | "--height" | "--border-radius" | "--track-padding", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<SwiperRef>>;
export {};
