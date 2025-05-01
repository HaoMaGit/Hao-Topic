import type { FC, ReactNode } from 'react';
import { GetContainer } from '../../utils/render-to-container';
import { PropagationEvent } from '../../utils/with-stop-propagation';
import type { MaskProps } from '../mask';
export interface ToastProps {
    afterClose?: () => void;
    maskStyle?: MaskProps['style'];
    maskClassName?: string;
    maskClickable?: boolean;
    content?: ReactNode;
    icon?: 'success' | 'fail' | 'loading' | ReactNode;
    duration?: number;
    position?: 'top' | 'bottom' | 'center';
    visible?: boolean;
    getContainer?: GetContainer;
    stopPropagation?: PropagationEvent[];
}
export declare const InternalToast: FC<ToastProps>;
