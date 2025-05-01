import './checkbox.less';
export type { CheckboxValue, CheckboxProps, CheckboxRef } from './checkbox';
export type { CheckboxGroupProps } from './group';
declare const _default: import("react").ForwardRefExoticComponent<{
    checked?: boolean | undefined;
    defaultChecked?: boolean | undefined;
    disabled?: boolean | undefined;
    onChange?: ((checked: boolean) => void) | undefined;
    value?: import("./checkbox").CheckboxValue | undefined;
    indeterminate?: boolean | undefined;
    block?: boolean | undefined;
    id?: string | undefined;
    icon?: ((checked: boolean, indeterminate: boolean) => import("react").ReactNode) | undefined;
    children?: import("react").ReactNode;
    onClick?: ((event: import("react").MouseEvent<HTMLLabelElement, MouseEvent>) => void) | undefined;
} & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<"--font-size" | "--icon-size" | "--gap", string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & import("react").RefAttributes<import("./checkbox").CheckboxRef>> & {
    Group: import("react").FC<import("./group").CheckboxGroupProps>;
};
export default _default;
