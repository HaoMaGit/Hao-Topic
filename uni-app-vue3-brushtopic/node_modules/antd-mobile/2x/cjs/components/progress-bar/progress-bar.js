"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ProgressBar = void 0;
var _react = _interopRequireDefault(require("react"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _classnames = _interopRequireDefault(require("classnames"));
var _isNodeWithContent = require("../../utils/is-node-with-content");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-progress-bar`;
const defaultProps = {
  percent: 0,
  rounded: true,
  text: false
};
const ProgressBar = p => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const fillStyle = {
    width: `${props.percent}%`
  };
  const textElement = function () {
    if (props.text === true) {
      return `${props.percent}%`;
    }
    if (typeof props.text === 'function') {
      return props.text(props.percent);
    }
    return props.text;
  }();
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: (0, _classnames.default)(classPrefix, props.rounded && `${classPrefix}-rounded`)
  }, _react.default.createElement("div", {
    className: `${classPrefix}-trail`
  }, _react.default.createElement("div", {
    className: `${classPrefix}-fill`,
    style: fillStyle
  })), (0, _isNodeWithContent.isNodeWithContent)(textElement) && _react.default.createElement("div", {
    className: `${classPrefix}-text`
  }, textElement)));
};
exports.ProgressBar = ProgressBar;