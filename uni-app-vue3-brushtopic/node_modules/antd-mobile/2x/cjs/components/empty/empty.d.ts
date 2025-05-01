import type { FC, ReactNode, CSSProperties } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type EmptyProps = {
    image?: ReactNode;
    imageStyle?: CSSProperties;
    description?: ReactNode;
} & NativeProps;
/** @deprecated Empty has been deprecated and will be removed in the next major version. */
export declare const Empty: FC<EmptyProps>;
