import { PickerColumn, PickerValue } from '../picker-view';
import { CascadePickerOption } from './cascade-picker';
export declare function useColumnsFn(options: CascadePickerOption[]): (selected: PickerValue[]) => PickerColumn[];
