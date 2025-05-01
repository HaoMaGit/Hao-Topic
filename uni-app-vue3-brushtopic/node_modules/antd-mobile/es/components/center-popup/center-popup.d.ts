import type { FC, PropsWithChildren } from 'react';
import { NativeProps } from '../../utils/native-props';
import { PopupBaseProps } from '../popup/popup-base-props';
export declare type CenterPopupProps = PopupBaseProps & PropsWithChildren<{
    role?: string;
}> & NativeProps<'--background-color' | '--border-radius' | '--max-width' | '--min-width' | '--z-index'>;
export declare const CenterPopup: FC<CenterPopupProps>;
