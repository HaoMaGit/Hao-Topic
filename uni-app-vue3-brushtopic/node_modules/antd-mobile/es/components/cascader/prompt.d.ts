import type { CascaderProps } from './index';
export declare function prompt(props: Omit<CascaderProps, 'value' | 'visible' | 'children'>): Promise<import("../check-list").CheckListValue[] | null>;
