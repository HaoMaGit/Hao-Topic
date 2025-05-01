"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ThumbIcon = void 0;
var _react = _interopRequireDefault(require("react"));
var _nativeProps = require("../../utils/native-props");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const ThumbIcon = props => {
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("svg", {
    viewBox: '0 0 24 24',
    xmlns: 'http://www.w3.org/2000/svg'
  }, _react.default.createElement("g", {
    fill: 'currentColor',
    fillRule: 'evenodd'
  }, _react.default.createElement("rect", {
    x: 10,
    width: 4,
    height: 24,
    rx: 2
  }), _react.default.createElement("rect", {
    y: 4,
    width: 4,
    height: 16,
    rx: 2
  }), _react.default.createElement("rect", {
    x: 20,
    y: 4,
    width: 4,
    height: 16,
    rx: 2
  }))));
};
exports.ThumbIcon = ThumbIcon;