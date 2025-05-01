import { __rest } from "tslib";
import React from 'react';
import PickerView from '../picker-view';
import { useColumnsFn } from '../cascade-picker/cascade-picker-utils';
export const CascadePickerView = props => {
  const {
      options
    } = props,
    pickerProps = __rest(props, ["options"]);
  const columnsFn = useColumnsFn(options);
  return React.createElement(PickerView, Object.assign({}, pickerProps, {
    columns: columnsFn
  }));
};