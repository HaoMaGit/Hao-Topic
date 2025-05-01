import React from 'react';
export default function useMeasure(containerRef: React.RefObject<HTMLDivElement>, content: string, rows: number, direction: 'start' | 'end' | 'middle', expanded: boolean, expandNode: React.ReactElement | null, collapseNode: React.ReactElement | null): readonly [React.JSX.Element, () => void];
