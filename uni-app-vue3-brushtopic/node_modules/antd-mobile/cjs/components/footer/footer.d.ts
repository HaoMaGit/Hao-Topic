import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type LinkItem = {
    text: string;
    href: string;
};
export declare type ChipItem = {
    text: ReactNode;
    type?: 'plain' | 'link';
};
export declare type FooterProps = {
    label?: ReactNode;
    links?: LinkItem[];
    content?: ReactNode;
    chips?: ChipItem[];
    onChipClick?: (item: ChipItem, index: number) => void;
    onLinkClick?: (item: LinkItem, index: number) => void;
} & NativeProps<'--background-color'>;
export declare const Footer: FC<FooterProps>;
