import { prompt } from './prompt';
import './cascader.less';
export type { CascaderProps, CascaderRef, CascaderActions } from './cascader';
export type { CascaderValue, CascaderValueExtend, CascaderOption, } from '../cascader-view';
declare const _default: import("react").ForwardRefExoticComponent<{
    options: import("../cascader-view").CascaderOption[];
    value?: import("../check-list").CheckListValue[] | undefined;
    defaultValue?: import("../check-list").CheckListValue[] | undefined;
    placeholder?: string | undefined;
    onSelect?: ((value: import("../check-list").CheckListValue[], extend: import("../cascader-view").CascaderValueExtend) => void) | undefined;
    onConfirm?: ((value: import("../check-list").CheckListValue[], extend: import("../cascader-view").CascaderValueExtend) => void) | undefined;
    onCancel?: (() => void) | undefined;
    onClose?: (() => void) | undefined;
    visible?: boolean | undefined;
    title?: import("react").ReactNode;
    confirmText?: import("react").ReactNode;
    cancelText?: import("react").ReactNode;
    loading?: boolean | undefined;
    children?: ((items: (import("../cascader-view").CascaderOption | null)[], actions: import("./cascader").CascaderActions) => import("react").ReactNode) | undefined;
    onTabsChange?: ((index: number) => void) | undefined;
    activeIcon?: import("react").ReactNode;
    fieldNames?: import("../../hooks").FieldNamesType | undefined;
} & Pick<import("../popup").PopupProps, "onClick" | "destroyOnClose" | "forceRender" | "getContainer" | "afterShow" | "afterClose" | "stopPropagation"> & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<never, string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & import("react").RefAttributes<import("./cascader").CascaderActions>> & {
    prompt: typeof prompt;
    optionSkeleton: import("../cascader-view").CascaderOption[];
};
export default _default;
