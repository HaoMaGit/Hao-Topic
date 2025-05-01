import React from 'react';
import type { ReactElement } from 'react';
export declare type PropagationEvent = 'click' | 'touchstart';
export declare function withStopPropagation(events: PropagationEvent[], element: ReactElement): React.ReactElement<any, string | React.JSXElementConstructor<any>>;
