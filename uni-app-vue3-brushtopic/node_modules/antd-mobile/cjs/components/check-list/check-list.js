"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.CheckList = void 0;
var _antdMobileIcons = require("antd-mobile-icons");
var _react = _interopRequireDefault(require("react"));
var _nativeProps = require("../../utils/native-props");
var _usePropsValue = require("../../utils/use-props-value");
var _withDefaultProps = require("../../utils/with-default-props");
var _configProvider = require("../config-provider");
var _list = _interopRequireDefault(require("../list"));
var _context = require("./context");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = 'adm-check-list';
const defaultProps = {
  multiple: false,
  defaultValue: [],
  activeIcon: _react.default.createElement(_antdMobileIcons.CheckOutline, null)
};
const CheckList = props => {
  const {
    checkList: componentConfig = {}
  } = (0, _configProvider.useConfig)();
  const mergedProps = (0, _withDefaultProps.mergeProps)(defaultProps, componentConfig, props);
  const [value, setValue] = (0, _usePropsValue.usePropsValue)(mergedProps);
  function check(val) {
    if (mergedProps.multiple) {
      setValue([...value, val]);
    } else {
      setValue([val]);
    }
  }
  function uncheck(val) {
    setValue(value.filter(item => item !== val));
  }
  const {
    activeIcon,
    extra,
    disabled,
    readOnly
  } = mergedProps;
  return _react.default.createElement(_context.CheckListContext.Provider, {
    value: {
      value,
      check,
      uncheck,
      activeIcon,
      extra,
      disabled,
      readOnly
    }
  }, (0, _nativeProps.withNativeProps)(mergedProps, _react.default.createElement(_list.default, {
    mode: mergedProps.mode,
    className: classPrefix
  }, mergedProps.children)));
};
exports.CheckList = CheckList;