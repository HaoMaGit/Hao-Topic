import './swiper.less';
export type { SwiperProps, SwiperRef } from './swiper';
declare const _default: import("react").ForwardRefExoticComponent<{
    defaultIndex?: number | undefined;
    allowTouchMove?: boolean | undefined;
    autoplay?: boolean | "reverse" | undefined;
    autoplayInterval?: number | undefined;
    loop?: boolean | undefined;
    direction?: "vertical" | "horizontal" | undefined;
    onIndexChange?: ((index: number) => void) | undefined;
    indicatorProps?: Pick<import("../page-indicator").PageIndicatorProps, "style" | "className" | "color"> | undefined;
    indicator?: false | ((total: number, current: number) => import("react").ReactNode) | undefined;
    slideSize?: number | undefined;
    trackOffset?: number | undefined;
    stuckAtBoundary?: boolean | undefined;
    rubberband?: boolean | undefined;
    stopPropagation?: ("mousedown" | "mousemove" | "mouseup")[] | undefined;
    total?: number | undefined;
    children?: import("react").ReactElement<any, string | import("react").JSXElementConstructor<any>> | import("react").ReactElement<any, string | import("react").JSXElementConstructor<any>>[] | ((index: number) => import("react").ReactElement<any, string | import("react").JSXElementConstructor<any>>) | undefined;
} & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<"--width" | "--height" | "--border-radius" | "--track-padding", string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & import("react").RefAttributes<import("./swiper").SwiperRef>> & {
    Item: import("react").FC<{
        onClick?: ((e: import("react").MouseEvent<HTMLDivElement, MouseEvent>) => void) | undefined;
        children?: import("react").ReactNode;
    } & {
        className?: string | undefined;
        style?: (import("react").CSSProperties & Partial<Record<never, string>>) | undefined;
        tabIndex?: number | undefined;
    } & import("react").AriaAttributes>;
};
export default _default;
