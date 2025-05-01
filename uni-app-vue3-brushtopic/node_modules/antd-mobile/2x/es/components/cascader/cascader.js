import React, { useState, useEffect, forwardRef, useImperativeHandle } from 'react';
import Popup from '../popup';
import { mergeProps } from '../../utils/with-default-props';
import { withNativeProps } from '../../utils/native-props';
import { usePropsValue } from '../../utils/use-props-value';
import CascaderView from '../cascader-view';
import { useConfig } from '../config-provider';
import { useCascaderValueExtend } from '../cascader-view/use-cascader-value-extend';
import { useFieldNames } from '../../hooks';
const classPrefix = `adm-cascader`;
const defaultProps = {
  defaultValue: [],
  destroyOnClose: true,
  forceRender: false
};
export const Cascader = forwardRef((p, ref) => {
  var _a;
  const {
    locale
  } = useConfig();
  const props = mergeProps(defaultProps, {
    confirmText: locale.common.confirm,
    cancelText: locale.common.cancel,
    placeholder: locale.Cascader.placeholder
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
      (_a = props.onConfirm) === null || _a === void 0 ? void 0 : _a.call(props, val, generateValueExtend(val));
    }
  }));
  const [, valueName, childrenName] = useFieldNames(props.fieldNames);
  const generateValueExtend = useCascaderValueExtend(props.options, {
    valueName,
    childrenName
  });
  const [innerValue, setInnerValue] = useState(value);
  useEffect(() => {
    if (!visible) {
      setInnerValue(value);
    }
  }, [visible, value]);
  const cascaderElement = withNativeProps(props, React.createElement("div", {
    className: classPrefix
  }, React.createElement("div", {
    className: `${classPrefix}-header`
  }, React.createElement("a", {
    className: `${classPrefix}-header-button`,
    onClick: () => {
      var _a;
      (_a = props.onCancel) === null || _a === void 0 ? void 0 : _a.call(props);
      setVisible(false);
    }
  }, props.cancelText), React.createElement("div", {
    className: `${classPrefix}-header-title`
  }, props.title), React.createElement("a", {
    className: `${classPrefix}-header-button`,
    onClick: () => {
      setValue(innerValue, true);
      setVisible(false);
    }
  }, props.confirmText)), React.createElement("div", {
    className: `${classPrefix}-body`
  }, React.createElement(CascaderView, Object.assign({}, props, {
    value: innerValue,
    onChange: (val, ext) => {
      var _a;
      setInnerValue(val);
      if (visible) {
        (_a = props.onSelect) === null || _a === void 0 ? void 0 : _a.call(props, val, ext);
      }
    }
  })))));
  const popupElement = React.createElement(Popup, {
    visible: visible,
    position: 'bottom',
    onMaskClick: () => {
      var _a;
      (_a = props.onCancel) === null || _a === void 0 ? void 0 : _a.call(props);
      setVisible(false);
    },
    getContainer: props.getContainer,
    destroyOnClose: props.destroyOnClose,
    forceRender: props.forceRender,
    afterShow: props.afterShow,
    afterClose: props.afterClose,
    onClick: props.onClick,
    stopPropagation: props.stopPropagation
  }, cascaderElement);
  return React.createElement(React.Fragment, null, popupElement, (_a = props.children) === null || _a === void 0 ? void 0 : _a.call(props, generateValueExtend(value).items, actions));
});