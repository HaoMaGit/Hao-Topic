import type { ReactNode } from 'react';
import { ErrorBlock } from './error-block';
export { createErrorBlock } from './create-error-block';
export type { ErrorBlockProps } from './create-error-block';
export declare type ErrorBlockStatus = 'default' | 'disconnected' | 'empty' | 'busy';
export declare type ImageRecord = Partial<Record<ErrorBlockStatus, string | ReactNode>>;
export default ErrorBlock;
