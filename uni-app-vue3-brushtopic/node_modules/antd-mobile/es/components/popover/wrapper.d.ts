import React from 'react';
import type { ReactNode } from 'react';
export declare class Wrapper extends React.Component<{
    children?: ReactNode;
}, {}> {
    element: Element | null;
    componentDidMount(): void;
    componentDidUpdate(): void;
    render(): React.ReactNode;
}
