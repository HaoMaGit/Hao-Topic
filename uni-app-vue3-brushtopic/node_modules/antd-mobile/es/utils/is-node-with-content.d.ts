import type { ReactNode } from 'react';
declare type Regulated<T> = T extends null | undefined | false ? never : T;
/**
 * Check if the `node` is visible Node (not null, undefined, or false)
 */
export declare function isNodeWithContent(node: ReactNode): node is Regulated<ReactNode>;
export {};
