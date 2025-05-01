import './dropdown.less';
export type { DropdownProps, DropdownRef } from './dropdown';
export type { DropdownItemProps } from './item';
declare const _default: import("react").ForwardRefExoticComponent<{
    activeKey?: string | null | undefined;
    defaultActiveKey?: string | null | undefined;
    closeOnMaskClick?: boolean | undefined;
    closeOnClickAway?: boolean | undefined;
    onChange?: ((key: string | null) => void) | undefined;
    arrowIcon?: import("react").ReactNode;
    arrow?: import("react").ReactNode;
    getContainer?: import("../../utils/render-to-container").GetContainer | undefined;
} & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<never, string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & {
    children?: import("react").ReactNode;
} & import("react").RefAttributes<import("./dropdown").DropdownRef>> & {
    Item: import("react").FC<import("./item").DropdownItemProps>;
};
export default _default;
