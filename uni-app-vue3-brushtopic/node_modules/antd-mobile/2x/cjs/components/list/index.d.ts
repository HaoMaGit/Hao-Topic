import './list.less';
export type { ListProps, ListRef } from './list';
export type { ListItemProps } from './list-item';
declare const _default: import("react").ForwardRefExoticComponent<{
    header?: import("react").ReactNode;
    mode?: "default" | "card" | undefined;
    children?: import("react").ReactNode;
} & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<"--active-background-color" | "--align-items" | "--border-bottom" | "--border-inner" | "--border-top" | "--extra-max-width" | "--font-size" | "--header-font-size" | "--padding-left" | "--padding-right" | "--prefix-padding-right" | "--prefix-width", string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & import("react").RefAttributes<import("./list").ListRef>> & {
    Item: import("react").FC<import("./list-item").ListItemProps>;
};
export default _default;
