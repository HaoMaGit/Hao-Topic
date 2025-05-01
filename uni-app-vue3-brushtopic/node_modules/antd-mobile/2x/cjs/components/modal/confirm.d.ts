import { ModalProps } from './index';
import type { ReactNode } from 'react';
export declare type ModalConfirmProps = Omit<ModalProps, 'visible' | 'closeOnAction' | 'actions'> & {
    confirmText?: ReactNode;
    cancelText?: ReactNode;
    onConfirm?: () => void | Promise<void>;
    onCancel?: () => void | Promise<void>;
};
export declare function confirm(p: ModalConfirmProps): Promise<boolean>;
