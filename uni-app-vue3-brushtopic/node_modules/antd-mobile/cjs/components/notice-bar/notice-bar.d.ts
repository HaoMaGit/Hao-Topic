import type { ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type NoticeBarProps = {
    /** The type of the NoticeBar */
    color?: 'default' | 'alert' | 'error' | 'success' | 'info';
    /** TDelay to start scrolling, unit ms */
    delay?: number;
    /** Scroll speed, unit px/s */
    speed?: number;
    /** The content of the NoticeBar */
    content: ReactNode;
    /** Whether it can be closed */
    closeable?: boolean;
    /** Custom close icon */
    closeIcon?: ReactNode;
    /** Callback when closed */
    onClose?: () => void;
    /** Event when click */
    onClick?: () => void;
    /** Additional operating area, displayed to the left of the close button */
    extra?: ReactNode;
    /** Radio icon on the left */
    icon?: ReactNode;
    /** Whether to display multiple lines */
    wrap?: boolean;
    /** Block shape */
    shape?: 'rectangular' | 'neutral' | 'rounded';
    /** Border visibility */
    bordered?: 'block' | boolean;
} & NativeProps<'--background-color' | '--border-color' | '--text-color' | '--font-size' | '--icon-font-size' | '--height'>;
export declare const NoticeBar: React.NamedExoticComponent<NoticeBarProps>;
