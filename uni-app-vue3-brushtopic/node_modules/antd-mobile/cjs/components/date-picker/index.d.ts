import './date-picker.less';
import { prompt } from './prompt';
export type { DatePickerProps, DatePickerRef } from './date-picker';
export type { DatePickerFilter } from './date-picker-utils';
declare const _default: import("react").ForwardRefExoticComponent<Pick<import("../picker").PickerProps, "style" | "title" | "onClick" | "visible" | "destroyOnClose" | "forceRender" | "getContainer" | "afterShow" | "afterClose" | "stopPropagation" | "closeOnMaskClick" | "onClose" | "cancelText" | "loading" | "confirmText" | "mouseWheel" | "loadingContent" | "onCancel"> & {
    value?: import("./util").PickerDate | null | undefined;
    defaultValue?: import("./util").PickerDate | null | undefined;
    onSelect?: ((value: import("./util").PickerDate) => void) | undefined;
    onConfirm?: ((value: import("./util").PickerDate) => void) | undefined;
    min?: import("./util").PickerDate | undefined;
    max?: import("./util").PickerDate | undefined;
    precision?: import("./date-picker-utils").Precision | undefined;
    children?: ((value: import("./util").PickerDate | null, actions: import("../picker").PickerActions) => import("react").ReactNode) | undefined;
    renderLabel?: import("../date-picker-view/date-picker-view").RenderLabel | undefined;
    filter?: Partial<Record<import("./date-picker-utils").Precision, (val: number, extend: {
        date: Date;
    }) => boolean>> | undefined;
    tillNow?: boolean | undefined;
} & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<never, string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & import("react").RefAttributes<import("../picker").PickerActions>> & {
    prompt: typeof prompt;
    DATE_NOW: string;
};
export default _default;
