import { ModalProps } from './modal';
export declare type ModalShowProps = Omit<ModalProps, 'visible' | 'destroyOnClose' | 'forceRender'>;
export declare type ModalShowHandler = {
    close: () => void;
};
export declare const closeFnSet: Set<() => void>;
export declare function show(props: ModalShowProps): ModalShowHandler;
