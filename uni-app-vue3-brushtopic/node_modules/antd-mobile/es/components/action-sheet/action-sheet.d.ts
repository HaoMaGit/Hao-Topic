import type { FC, ReactNode, CSSProperties } from 'react';
import { NativeProps } from '../../utils/native-props';
import { PopupProps } from '../popup';
export declare type Action = {
    key: string | number;
    text: ReactNode;
    disabled?: boolean;
    description?: ReactNode;
    danger?: boolean;
    bold?: boolean;
    onClick?: () => void;
};
export declare type ActionSheetProps = {
    visible?: boolean;
    actions: Action[];
    extra?: ReactNode;
    cancelText?: ReactNode;
    onAction?: (action: Action, index: number) => void;
    onClose?: () => void;
    onMaskClick?: () => void;
    closeOnAction?: boolean;
    closeOnMaskClick?: boolean;
    safeArea?: boolean;
    popupClassName?: string;
    /** @deprecated use `styles` instead */
    popupStyle?: CSSProperties;
    styles?: Partial<Record<'body' | 'mask', CSSProperties>>;
} & Pick<PopupProps, 'afterClose' | 'getContainer' | 'destroyOnClose' | 'forceRender'> & NativeProps;
export declare const ActionSheet: FC<ActionSheetProps>;
export declare type ActionSheetShowHandler = {
    close: () => void;
};
export declare function showActionSheet(props: Omit<ActionSheetProps, 'visible' | 'destroyOnClose' | 'forceRender'>): ActionSheetShowHandler;
