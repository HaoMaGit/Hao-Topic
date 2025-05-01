import type { FC } from 'react';
import { ListItemProps } from '../list';
import { NativeProps } from '../../utils/native-props';
import { CheckListValue } from '.';
export declare type CheckListItemProps = Pick<ListItemProps, 'title' | 'children' | 'description' | 'prefix' | 'disabled' | 'onClick' | 'style'> & {
    value: CheckListValue;
    readOnly?: boolean;
} & NativeProps;
export declare const CheckListItem: FC<CheckListItemProps>;
