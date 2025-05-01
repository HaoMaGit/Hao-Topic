"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.SearchBar = void 0;
var _antdMobileIcons = require("antd-mobile-icons");
var _classnames = _interopRequireDefault(require("classnames"));
var _react = _interopRequireWildcard(require("react"));
var _nativeProps = require("../../utils/native-props");
var _usePropsValue = require("../../utils/use-props-value");
var _withDefaultProps = require("../../utils/with-default-props");
var _button = _interopRequireDefault(require("../button"));
var _configProvider = require("../config-provider");
var _input = _interopRequireDefault(require("../input"));
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-search-bar`;
const defaultProps = {
  clearable: true,
  onlyShowClearWhenFocus: false,
  showCancelButton: false,
  defaultValue: '',
  clearOnCancel: true
};
const SearchBar = (0, _react.forwardRef)((props, ref) => {
  const {
    locale,
    searchBar: componentConfig = {}
  } = (0, _configProvider.useConfig)();
  const mergedProps = (0, _withDefaultProps.mergeProps)(defaultProps, componentConfig, {
    cancelText: locale.common.cancel
  }, props);
  const searchIcon = (0, _withDefaultProps.mergeProp)(_react.default.createElement(_antdMobileIcons.SearchOutline, null), componentConfig.searchIcon, props.icon, props.searchIcon);
  const [value, setValue] = (0, _usePropsValue.usePropsValue)(mergedProps);
  const [hasFocus, setHasFocus] = (0, _react.useState)(false);
  const inputRef = (0, _react.useRef)(null);
  const composingRef = (0, _react.useRef)(false);
  (0, _react.useImperativeHandle)(ref, () => ({
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
    return isShowCancel && _react.default.createElement("div", {
      className: `${classPrefix}-suffix`
    }, _react.default.createElement(_button.default, {
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
  return (0, _nativeProps.withNativeProps)(mergedProps, _react.default.createElement("div", {
    className: (0, _classnames.default)(classPrefix, {
      [`${classPrefix}-active`]: hasFocus
    })
  }, _react.default.createElement("div", {
    className: `${classPrefix}-input-box`
  }, searchIcon && _react.default.createElement("div", {
    className: `${classPrefix}-input-box-icon`
  }, searchIcon), _react.default.createElement(_input.default, {
    ref: inputRef,
    className: (0, _classnames.default)(`${classPrefix}-input`, {
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
exports.SearchBar = SearchBar;