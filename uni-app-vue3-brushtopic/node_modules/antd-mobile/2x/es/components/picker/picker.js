import React, { useState, useEffect, forwardRef, useImperativeHandle, memo } from 'react';
import classNames from 'classnames';
import Popup from '../popup';
import { mergeProps } from '../../utils/with-default-props';
import { withNativeProps } from '../../utils/native-props';
import { usePropsValue } from '../../utils/use-props-value';
import PickerView from '../picker-view';
import { generateColumnsExtend, useColumnsExtend } from '../picker-view/columns-extend';
import { useConfig } from '../config-provider';
import { useMemoizedFn } from 'ahooks';
import SafeArea from '../safe-area';
import { defaultRenderLabel } from './picker-utils';
const classPrefix = `adm-picker`;
const defaultProps = {
  defaultValue: [],
  closeOnMaskClick: true,
  renderLabel: defaultRenderLabel,
  destroyOnClose: false,
  forceRender: false
};
export const Picker = memo(forwardRef((p, ref) => {
  var _a;
  const {
    locale
  } = useConfig();
  const props = mergeProps(defaultProps, {
    confirmText: locale.common.confirm,
    cancelText: locale.common.cancel
  }, p);
  const [visible, setVisible] = usePropsValue({
    value: props.visible,
    defaultValue: false,
    onChange: v => {
      var _a;
      if (v === false) {
        (_a = props.onClose) === null || _a === void 0 ? void 0 : _a.call(props);
      }
    }
  });
  const actions = {
    toggle: () => {
      setVisible(v => !v);
    },
    open: () => {
      setVisible(true);
    },
    close: () => {
      setVisible(false);
    }
  };
  useImperativeHandle(ref, () => actions);
  const [value, setValue] = usePropsValue(Object.assign(Object.assign({}, props), {
    onChange: val => {
      var _a;
      const extend = generateColumnsExtend(props.columns, val);
      (_a = props.onConfirm) === null || _a === void 0 ? void 0 : _a.call(props, val, extend);
    }
  }));
  const extend = useColumnsExtend(props.columns, value);
  const [innerValue, setInnerValue] = useState(value);
  useEffect(() => {
    if (innerValue !== value) {
      setInnerValue(value);
    }
  }, [visible]);
  useEffect(() => {
    if (!visible) {
      setInnerValue(value);
    }
  }, [value]);
  const onChange = useMemoizedFn((val, ext) => {
    var _a;
    setInnerValue(val);
    if (visible) {
      (_a = props.onSelect) === null || _a === void 0 ? void 0 : _a.call(props, val, ext);
    }
  });
  const pickerElement = withNativeProps(props, React.createElement("div", {
    className: classPrefix
  }, React.createElement("div", {
    className: `${classPrefix}-header`
  }, React.createElement("a", {
    role: 'button',
    className: `${classPrefix}-header-button`,
    onClick: () => {
      var _a;
      (_a = props.onCancel) === null || _a === void 0 ? void 0 : _a.call(props);
      setVisible(false);
    }
  }, props.cancelText), React.createElement("div", {
    className: `${classPrefix}-header-title`
  }, props.title), React.createElement("a", {
    role: 'button',
    className: classNames(`${classPrefix}-header-button`, props.loading && `${classPrefix}-header-button-disabled`),
    onClick: () => {
      if (props.loading) return;
      setValue(innerValue, true);
      setVisible(false);
    },
    "aria-disabled": props.loading
  }, props.confirmText)), React.createElement("div", {
    className: `${classPrefix}-body`
  }, React.createElement(PickerView, {
    loading: props.loading,
    loadingContent: props.loadingContent,
    columns: props.columns,
    renderLabel: props.renderLabel,
    value: innerValue,
    mouseWheel: props.mouseWheel,
    onChange: onChange
  }))));
  const popupElement = React.createElement(Popup, {
    style: props.popupStyle,
    className: classNames(`${classPrefix}-popup`, props.popupClassName),
    visible: visible,
    position: 'bottom',
    onMaskClick: () => {
      var _a;
      if (!props.closeOnMaskClick) return;
      (_a = props.onCancel) === null || _a === void 0 ? void 0 : _a.call(props);
      setVisible(false);
    },
    getContainer: props.getContainer,
    destroyOnClose: props.destroyOnClose,
    afterShow: props.afterShow,
    afterClose: props.afterClose,
    onClick: props.onClick,
    forceRender: props.forceRender,
    stopPropagation: props.stopPropagation
  }, pickerElement, React.createElement(SafeArea, {
    position: 'bottom'
  }));
  return React.createElement(React.Fragment, null, popupElement, (_a = props.children) === null || _a === void 0 ? void 0 : _a.call(props, extend.items, actions));
}));
Picker.displayName = 'Picker';