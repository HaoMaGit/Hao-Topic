import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
import { ImageProps } from '../image';
export declare type AvatarProps = {
    src: string;
    fallback?: ReactNode;
    fit?: 'contain' | 'cover' | 'fill' | 'none' | 'scale-down';
} & Pick<ImageProps, 'alt' | 'lazy' | 'onClick' | 'onError' | 'onLoad'> & NativeProps<'--size' | '--border-radius'>;
export declare const Avatar: FC<AvatarProps>;
