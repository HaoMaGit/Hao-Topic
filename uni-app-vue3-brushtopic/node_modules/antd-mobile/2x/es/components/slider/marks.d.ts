import type { FC, ReactNode } from 'react';
export declare type SliderMarks = {
    [key: number]: ReactNode;
};
declare type MarksProps = {
    marks: SliderMarks;
    max: number;
    min: number;
    upperBound: number;
    lowerBound: number;
};
declare const Marks: FC<MarksProps>;
export default Marks;
