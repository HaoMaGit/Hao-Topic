import type { FC, ReactNode } from 'react';
import { Action } from './dialog-action-button';
import { NativeProps } from '../../utils/native-props';
import { CenterPopupProps } from '../center-popup';
export declare type DialogProps = Pick<CenterPopupProps, 'afterClose' | 'afterShow' | 'bodyClassName' | 'bodyStyle' | 'destroyOnClose' | 'disableBodyScroll' | 'forceRender' | 'getContainer' | 'maskClassName' | 'maskStyle' | 'stopPropagation' | 'visible'> & {
    image?: string;
    header?: ReactNode;
    title?: ReactNode;
    content?: ReactNode;
    actions?: (Action | Action[])[];
    onAction?: (action: Action, index: number) => void | Promise<void>;
    onClose?: () => void;
    closeOnAction?: boolean;
    closeOnMaskClick?: boolean;
} & NativeProps;
export declare const Dialog: FC<DialogProps>;
