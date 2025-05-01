import type { ReactElement } from 'react';
declare type ImperativeProps = {
    visible?: boolean;
    onClose?: () => void;
    afterClose?: () => void;
};
declare type TargetElement = ReactElement<ImperativeProps>;
export declare type ImperativeHandler = {
    close: () => void;
    replace: (element: TargetElement) => void;
    isRendered?: () => boolean;
};
export declare function renderImperatively(element: TargetElement): ImperativeHandler;
export {};
