import React, { forwardRef, useCallback, useMemo } from 'react';
import { useMemoizedFn } from 'ahooks';
import Picker from '../picker';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import { usePropsValue } from '../../utils/use-props-value';
import { convertDateToStringArray, convertStringArrayToDate, generateDatePickerColumns } from './date-picker-utils';
import { bound } from '../../utils/bound';
import useRenderLabel from '../date-picker-view/useRenderLabel';
import { TILL_NOW } from './util';
const thisYear = new Date().getFullYear();
const defaultProps = {
  min: new Date(new Date().setFullYear(thisYear - 10)),
  max: new Date(new Date().setFullYear(thisYear + 10)),
  precision: 'day',
  defaultValue: null
};
export const DatePicker = forwardRef((p, ref) => {
  const props = mergeProps(defaultProps, p);
  const {
    renderLabel
  } = props;
  const [value, setValue] = usePropsValue({
    value: props.value,
    defaultValue: props.defaultValue,
    onChange: v => {
      var _a;
      if (v === null) return;
      (_a = props.onConfirm) === null || _a === void 0 ? void 0 : _a.call(props, v);
    }
  });
  const now = useMemo(() => new Date(), []);
  const mergedRenderLabel = useRenderLabel(renderLabel);
  const pickerValue = useMemo(() => {
    let date = value !== null && value !== void 0 ? value : now;
    if (date.tillNow) {
      return [TILL_NOW];
    }
    date = new Date(bound(date.getTime(), props.min.getTime(), props.max.getTime()));
    return convertDateToStringArray(date, props.precision);
  }, [value, props.precision, props.min, props.max]);
  const onConfirm = useCallback(val => {
    const date = convertStringArrayToDate(val, props.precision);
    setValue(date, true);
  }, [setValue, props.precision]);
  const onSelect = useMemoizedFn(val => {
    var _a;
    const date = convertStringArrayToDate(val, props.precision);
    (_a = props.onSelect) === null || _a === void 0 ? void 0 : _a.call(props, date);
  });
  const columns = useCallback(selected => generateDatePickerColumns(selected, props.min, props.max, props.precision, mergedRenderLabel, props.filter, props.tillNow), [props.min, props.max, props.precision, mergedRenderLabel, props.tillNow]);
  return withNativeProps(props, React.createElement(Picker, {
    ref: ref,
    columns: columns,
    value: pickerValue,
    onCancel: props.onCancel,
    onClose: props.onClose,
    closeOnMaskClick: props.closeOnMaskClick,
    visible: props.visible,
    confirmText: props.confirmText,
    cancelText: props.cancelText,
    onConfirm: onConfirm,
    onSelect: onSelect,
    getContainer: props.getContainer,
    loading: props.loading,
    loadingContent: props.loadingContent,
    afterShow: props.afterShow,
    afterClose: props.afterClose,
    onClick: props.onClick,
    title: props.title,
    stopPropagation: props.stopPropagation,
    mouseWheel: props.mouseWheel,
    destroyOnClose: props.destroyOnClose,
    forceRender: props.forceRender
  }, (_, actions) => {
    var _a;
    return (_a = props.children) === null || _a === void 0 ? void 0 : _a.call(props, value, actions);
  }));
});