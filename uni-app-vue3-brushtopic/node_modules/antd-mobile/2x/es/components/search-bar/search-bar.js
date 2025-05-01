import { SearchOutline } from 'antd-mobile-icons';
import classNames from 'classnames';
import React, { forwardRef, useImperativeHandle, useRef, useState } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { usePropsValue } from '../../utils/use-props-value';
import { mergeProp, mergeProps } from '../../utils/with-default-props';
import Button from '../button';
import { useConfig } from '../config-provider';
import Input from '../input';
const classPrefix = `adm-search-bar`;
const defaultProps = {
  clearable: true,
  onlyShowClearWhenFocus: false,
  showCancelButton: false,
  defaultValue: '',
  clearOnCancel: true
};
export const SearchBar = forwardRef((props, ref) => {
  const {
    locale,
    searchBar: componentConfig = {}
  } = useConfig();
  const mergedProps = mergeProps(defaultProps, componentConfig, {
    cancelText: locale.common.cancel
  }, props);
  const searchIcon = mergeProp(React.createElement(SearchOutline, null), componentConfig.searchIcon, props.icon, props.searchIcon);
  const [value, setValue] = usePropsValue(mergedProps);
  const [hasFocus, setHasFocus] = useState(false);
  const inputRef = useRef(null);
  const composingRef = useRef(false);
  useImperativeHandle(ref, () => ({
    clear: () => {
      var _a;
      return (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.clear();
    },
    focus: () => {
      var _a;
      return (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.focus();
    },
    blur: () => {
      var _a;
      return (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.blur();
    },
    get nativeElement() {
      var _a, _b;
      return (_b = (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.nativeElement) !== null && _b !== void 0 ? _b : null;
    }
  }));
  const renderCancelButton = () => {
    let isShowCancel;
    if (typeof mergedProps.showCancelButton === 'function') {
      isShowCancel = mergedProps.showCancelButton(hasFocus, value);
    } else {
      isShowCancel = mergedProps.showCancelButton && hasFocus;
    }
    return isShowCancel && React.createElement("div", {
      className: `${classPrefix}-suffix`
    }, React.createElement(Button, {
      fill: 'none',
      className: `${classPrefix}-cancel-button`,
      onClick: () => {
        var _a, _b, _c;
        if (mergedProps.clearOnCancel) {
          (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.clear();
        }
        (_b = inputRef.current) === null || _b === void 0 ? void 0 : _b.blur();
        (_c = mergedProps.onCancel) === null || _c === void 0 ? void 0 : _c.call(mergedProps);
      },
      onMouseDown: e => {
        e.preventDefault();
      }
    }, mergedProps.cancelText));
  };
  return withNativeProps(mergedProps, React.createElement("div", {
    className: classNames(classPrefix, {
      [`${classPrefix}-active`]: hasFocus
    })
  }, React.createElement("div", {
    className: `${classPrefix}-input-box`
  }, searchIcon && React.createElement("div", {
    className: `${classPrefix}-input-box-icon`
  }, searchIcon), React.createElement(Input, {
    ref: inputRef,
    className: classNames(`${classPrefix}-input`, {
      [`${classPrefix}-input-without-icon`]: !searchIcon
    }),
    value: value,
    onChange: setValue,
    maxLength: mergedProps.maxLength,
    autoFocus: mergedProps.autoFocus,
    placeholder: mergedProps.placeholder,
    clearable: mergedProps.clearable,
    onlyShowClearWhenFocus: mergedProps.onlyShowClearWhenFocus,
    onFocus: e => {
      var _a;
      setHasFocus(true);
      (_a = mergedProps.onFocus) === null || _a === void 0 ? void 0 : _a.call(mergedProps, e);
    },
    onBlur: e => {
      var _a;
      setHasFocus(false);
      (_a = mergedProps.onBlur) === null || _a === void 0 ? void 0 : _a.call(mergedProps, e);
    },
    onClear: mergedProps.onClear,
    type: 'search',
    enterKeyHint: 'search',
    onEnterPress: () => {
      var _a, _b;
      if (!composingRef.current) {
        (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.blur();
        (_b = mergedProps.onSearch) === null || _b === void 0 ? void 0 : _b.call(mergedProps, value);
      }
    },
    "aria-label": locale.SearchBar.name,
    onCompositionStart: e => {
      var _a;
      composingRef.current = true;
      (_a = mergedProps.onCompositionStart) === null || _a === void 0 ? void 0 : _a.call(mergedProps, e);
    },
    onCompositionEnd: e => {
      var _a;
      composingRef.current = false;
      (_a = mergedProps.onCompositionEnd) === null || _a === void 0 ? void 0 : _a.call(mergedProps, e);
    }
  })), renderCancelButton()));
});