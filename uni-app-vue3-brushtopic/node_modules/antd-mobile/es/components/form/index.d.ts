import './index.less';
import { useWatch, useForm } from 'rc-field-form';
export declare type FormLayout = 'vertical' | 'horizontal';
export type { FormProps, FormInstance } from './form';
export type { FormItemProps } from './form-item';
export type { FormSubscribeProps } from './form-subscribe';
export type { ValidateMessages, FieldData, NamePath, Rule, RuleObject, RuleRender, } from 'rc-field-form/es/interface';
export type { FormArrayField, FormArrayOperation, FormArrayProps, } from './form-array';
declare const _default: import("react").ForwardRefExoticComponent<Pick<import("rc-field-form").FormProps<any>, "form" | "children" | "name" | "initialValues" | "preserve" | "validateMessages" | "validateTrigger" | "onFieldsChange" | "onFinish" | "onFinishFailed" | "onValuesChange"> & {
    className?: string | undefined;
    style?: (import("react").CSSProperties & Partial<Record<"--border-bottom" | "--border-inner" | "--border-top" | "--prefix-width", string>>) | undefined;
    tabIndex?: number | undefined;
} & import("react").AriaAttributes & Partial<import("./context").FormContextType> & {
    footer?: import("react").ReactNode;
    mode?: "default" | "card" | undefined;
} & import("react").RefAttributes<import("./form").FormInstance>> & {
    Item: import("react").FC<import("./form-item").FormItemProps>;
    Subscribe: import("react").FC<import("./form-subscribe").FormSubscribeProps>;
    Header: import("react").FC<{
        children?: import("react").ReactNode;
    }>;
    Array: import("react").FC<import("./form-array").FormArrayProps>;
    useForm: typeof useForm;
    useWatch: typeof useWatch;
};
export default _default;
