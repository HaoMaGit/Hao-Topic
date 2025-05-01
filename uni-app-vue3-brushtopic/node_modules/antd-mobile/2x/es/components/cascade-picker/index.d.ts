import { prompt } from './prompt';
export type { CascadePickerProps, CascadePickerRef, CascadePickerOption, } from './cascade-picker';
declare const _default: import("react").ForwardRefExoticComponent<Omit<import("../picker").PickerProps, "columns"> & {
    options: import("./cascade-picker").CascadePickerOption[];
} & import("react").RefAttributes<import("../picker").PickerActions>> & {
    prompt: typeof prompt;
};
export default _default;
