import React, { AriaAttributes } from 'react';
import type { CSSProperties, ReactElement } from 'react';
export declare type NativeProps<S extends string = never> = {
    className?: string;
    style?: CSSProperties & Partial<Record<S, string>>;
    tabIndex?: number;
} & AriaAttributes;
export declare function withNativeProps<P extends NativeProps>(props: P, element: ReactElement): React.ReactElement<any, string | React.JSXElementConstructor<any>>;
