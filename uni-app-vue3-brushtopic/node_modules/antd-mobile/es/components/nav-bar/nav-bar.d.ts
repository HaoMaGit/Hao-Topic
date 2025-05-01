import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type NavBarProps = {
    back?: ReactNode;
    backIcon?: boolean | ReactNode;
    /**
     * @deprecated use `backIcon` instead
     */
    backArrow?: boolean | ReactNode;
    left?: ReactNode;
    right?: ReactNode;
    onBack?: () => void;
    children?: ReactNode;
} & NativeProps<'--height' | '--border-bottom'>;
export declare const NavBar: FC<NavBarProps>;
