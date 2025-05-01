import { RefObject } from 'react';
export declare function observe(element: HTMLElement | null, options: MutationObserverInit, callback: VoidFunction): () => void;
export declare function useMutationEffect(effect: () => void, targetRef: RefObject<HTMLElement>, options: MutationObserverInit): void;
