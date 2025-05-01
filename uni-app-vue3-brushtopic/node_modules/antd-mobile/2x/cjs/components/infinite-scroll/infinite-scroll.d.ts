import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
export declare type InfiniteScrollProps = {
    loadMore: (isRetry: boolean) => Promise<void>;
    hasMore: boolean;
    threshold?: number;
    children?: ReactNode | ((hasMore: boolean, failed: boolean, retry: () => void) => ReactNode);
} & NativeProps;
export declare const InfiniteScroll: FC<InfiniteScrollProps>;
