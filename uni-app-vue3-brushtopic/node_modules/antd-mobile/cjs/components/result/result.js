"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Result = void 0;
var _react = _interopRequireDefault(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _useResultIcon = require("./use-result-icon");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-result`;
const defaultProps = {
  status: 'info'
};
const Result = p => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const {
    status,
    title,
    description,
    icon
  } = props;
  const fallbackIcon = (0, _useResultIcon.useResultIcon)(status);
  if (!status) return null;
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: (0, _classnames.default)(classPrefix, `${classPrefix}-${status}`)
  }, _react.default.createElement("div", {
    className: `${classPrefix}-icon`
  }, icon || fallbackIcon), _react.default.createElement("div", {
    className: `${classPrefix}-title`
  }, title), !!description && _react.default.createElement("div", {
    className: `${classPrefix}-description`
  }, description)));
};
exports.Result = Result;