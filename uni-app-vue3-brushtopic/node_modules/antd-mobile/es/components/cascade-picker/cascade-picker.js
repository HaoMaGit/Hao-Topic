import { __rest } from "tslib";
import React, { forwardRef } from 'react';
import Picker from '../picker';
import { useColumnsFn } from './cascade-picker-utils';
export const CascadePicker = forwardRef((props, ref) => {
  const {
      options
    } = props,
    pickerProps = __rest(props, ["options"]);
  const columnsFn = useColumnsFn(options);
  return React.createElement(Picker, Object.assign({}, pickerProps, {
    ref: ref,
    columns: columnsFn
  }));
});