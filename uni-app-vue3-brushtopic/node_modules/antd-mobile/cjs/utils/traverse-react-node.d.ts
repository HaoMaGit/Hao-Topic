import type { ReactNode } from 'react';
export declare function traverseReactNode(children: ReactNode, fn: (child: ReactNode, index: number) => void): void;
