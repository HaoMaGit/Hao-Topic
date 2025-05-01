import './index-bar.less';
export type { IndexBarProps, IndexBarRef } from './index-bar';
export type { IndexBarPanelProps } from './panel';
declare const _default: import("react").ForwardRefExoticComponent<{
    sticky?: boolean | undefined;
    onIndexChange?: ((index: string) => void) | undefined;
    children?: import("react").ReactNode;
} & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<"--sticky-offset-top", string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & import("react").RefAttributes<import("./index-bar").IndexBarRef>> & {
    Panel: import("react").FC<import("./panel").IndexBarPanelProps>;
};
export default _default;
