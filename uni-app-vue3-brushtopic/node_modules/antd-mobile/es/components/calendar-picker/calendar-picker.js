import { __rest } from "tslib";
import classNames from 'classnames';
import React, { forwardRef, useRef } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import Button from '../button';
import CalendarPickerView from '../calendar-picker-view';
import { Context } from '../calendar-picker-view/calendar-picker-view';
import { useConfig } from '../config-provider';
import Divider from '../divider';
import Popup from '../popup';
const classPrefix = 'adm-calendar-picker';
const defaultProps = {
  weekStartsOn: 'Sunday',
  defaultValue: null,
  allowClear: true,
  usePopup: true,
  selectionMode: 'single'
};
export const CalendarPicker = forwardRef((p, ref) => {
  const props = mergeProps(defaultProps, p);
  const {
    locale
  } = useConfig();
  const calendarRef = ref !== null && ref !== void 0 ? ref : useRef(null);
  const {
      visible,
      confirmText,
      popupClassName,
      popupStyle,
      popupBodyStyle,
      forceRender,
      closeOnMaskClick,
      onClose,
      onConfirm,
      onMaskClick,
      getContainer
    } = props,
    calendarViewProps = __rest(props, ["visible", "confirmText", "popupClassName", "popupStyle", "popupBodyStyle", "forceRender", "closeOnMaskClick", "onClose", "onConfirm", "onMaskClick", "getContainer"]);
  const viewContext = React.useMemo(() => ({
    visible: !!visible
  }), [visible]);
  const footer = React.createElement("div", {
    className: `${classPrefix}-footer`
  }, React.createElement(Divider, null), React.createElement("div", {
    className: `${classPrefix}-footer-bottom`
  }, React.createElement(Button, {
    color: 'primary',
    onClick: () => {
      var _a, _b, _c, _d;
      const dateRange = (_b = (_a = calendarRef.current) === null || _a === void 0 ? void 0 : _a.getDateRange()) !== null && _b !== void 0 ? _b : null;
      if (props.selectionMode === 'single') {
        (_c = props.onConfirm) === null || _c === void 0 ? void 0 : _c.call(props, dateRange ? dateRange[0] : null);
      } else if (props.selectionMode === 'range') {
        (_d = props.onConfirm) === null || _d === void 0 ? void 0 : _d.call(props, dateRange);
      }
      onClose === null || onClose === void 0 ? void 0 : onClose();
    }
  }, confirmText !== null && confirmText !== void 0 ? confirmText : locale.Calendar.confirm)));
  return withNativeProps(props, React.createElement("div", {
    className: classPrefix
  }, React.createElement(Popup, {
    visible: visible,
    className: classNames(`${classPrefix}-popup`, popupClassName),
    showCloseButton: true,
    forceRender: ref ? true : forceRender,
    style: popupStyle,
    bodyStyle: Object.assign({
      borderTopLeftRadius: '8px',
      borderTopRightRadius: '8px',
      minHeight: '80vh',
      overflow: 'auto'
    }, popupBodyStyle),
    onClose: onClose,
    onMaskClick: () => {
      onMaskClick === null || onMaskClick === void 0 ? void 0 : onMaskClick();
      if (closeOnMaskClick) {
        onClose === null || onClose === void 0 ? void 0 : onClose();
      }
    },
    getContainer: getContainer
  }, React.createElement(Context.Provider, {
    value: viewContext
  }, React.createElement(CalendarPickerView, Object.assign({
    ref: calendarRef
  }, calendarViewProps))), footer)));
});