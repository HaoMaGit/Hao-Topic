import type { FC, PropsWithChildren } from 'react';
import { NativeProps } from '../../utils/native-props';
import { PopupBaseProps } from './popup-base-props';
export declare type PopupProps = PopupBaseProps & PropsWithChildren<{
    position?: 'bottom' | 'top' | 'left' | 'right';
    closeOnSwipe?: boolean;
}> & NativeProps<'--z-index'>;
export declare const Popup: FC<PopupProps>;
