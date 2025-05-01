import type { FC, ReactNode } from 'react';
import { NativeProps } from '../../utils/native-props';
import { ListProps } from '../list';
export declare type CheckListValue = string | number;
export declare type CheckListProps = Pick<ListProps, 'mode' | 'style'> & {
    defaultValue?: CheckListValue[];
    value?: CheckListValue[];
    onChange?: (val: CheckListValue[]) => void;
    multiple?: boolean;
    activeIcon?: ReactNode;
    extra?: (active: boolean) => ReactNode;
    disabled?: boolean;
    readOnly?: boolean;
    children?: ReactNode;
} & NativeProps;
export declare const CheckList: FC<CheckListProps>;
