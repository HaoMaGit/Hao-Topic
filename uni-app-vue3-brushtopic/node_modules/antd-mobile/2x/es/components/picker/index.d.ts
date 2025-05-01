import './picker.less';
import { prompt } from './prompt';
export type { PickerProps, PickerRef, PickerActions } from './picker';
export type { PickerValue, PickerColumnItem, PickerColumn, PickerValueExtend, } from '../picker-view';
declare const _default: import("react").NamedExoticComponent<{
    columns: import("../picker-view").PickerColumn[] | ((value: import("../picker-view").PickerValue[]) => import("../picker-view").PickerColumn[]);
    value?: import("../picker-view").PickerValue[] | undefined;
    defaultValue?: import("../picker-view").PickerValue[] | undefined;
    loading?: boolean | undefined;
    loadingContent?: import("react").ReactNode;
    onSelect?: ((value: import("../picker-view").PickerValue[], extend: import("../picker-view").PickerValueExtend) => void) | undefined;
    onConfirm?: ((value: import("../picker-view").PickerValue[], extend: import("../picker-view").PickerValueExtend) => void) | undefined;
    onCancel?: (() => void) | undefined;
    onClose?: (() => void) | undefined;
    closeOnMaskClick?: boolean | undefined;
    visible?: boolean | undefined;
    title?: import("react").ReactNode;
    confirmText?: import("react").ReactNode;
    cancelText?: import("react").ReactNode;
    children?: ((items: (import("../picker-view").PickerColumnItem | null)[], actions: import("./picker").PickerActions) => import("react").ReactNode) | undefined;
    renderLabel?: ((item: import("../picker-view").PickerColumnItem) => import("react").ReactNode) | undefined;
    mouseWheel?: boolean | undefined;
    popupClassName?: string | undefined;
    popupStyle?: import("react").CSSProperties | undefined;
} & Pick<import("../popup").PopupProps, "onClick" | "destroyOnClose" | "forceRender" | "getContainer" | "afterShow" | "afterClose" | "stopPropagation"> & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<"--header-button-font-size" | "--title-font-size" | "--item-font-size" | "--item-height", string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & import("react").RefAttributes<import("./picker").PickerActions>> & {
    readonly type: import("react").ForwardRefExoticComponent<{
        columns: import("../picker-view").PickerColumn[] | ((value: import("../picker-view").PickerValue[]) => import("../picker-view").PickerColumn[]);
        value?: import("../picker-view").PickerValue[] | undefined;
        defaultValue?: import("../picker-view").PickerValue[] | undefined;
        loading?: boolean | undefined;
        loadingContent?: import("react").ReactNode;
        onSelect?: ((value: import("../picker-view").PickerValue[], extend: import("../picker-view").PickerValueExtend) => void) | undefined;
        onConfirm?: ((value: import("../picker-view").PickerValue[], extend: import("../picker-view").PickerValueExtend) => void) | undefined;
        onCancel?: (() => void) | undefined;
        onClose?: (() => void) | undefined;
        closeOnMaskClick?: boolean | undefined;
        visible?: boolean | undefined;
        title?: import("react").ReactNode;
        confirmText?: import("react").ReactNode;
        cancelText?: import("react").ReactNode;
        children?: ((items: (import("../picker-view").PickerColumnItem | null)[], actions: import("./picker").PickerActions) => import("react").ReactNode) | undefined;
        renderLabel?: ((item: import("../picker-view").PickerColumnItem) => import("react").ReactNode) | undefined;
        mouseWheel?: boolean | undefined;
        popupClassName?: string | undefined;
        popupStyle?: import("react").CSSProperties | undefined;
    } & Pick<import("../popup").PopupProps, "onClick" | "destroyOnClose" | "forceRender" | "getContainer" | "afterShow" | "afterClose" | "stopPropagation"> & {
        className?: string | undefined;
        style?: (import("react").CSSProperties & Partial<Record<"--header-button-font-size" | "--title-font-size" | "--item-font-size" | "--item-height", string>>) | undefined;
        tabIndex?: number | undefined;
    } & import("react").AriaAttributes & import("react").RefAttributes<import("./picker").PickerActions>>;
} & {
    prompt: typeof prompt;
};
export default _default;
