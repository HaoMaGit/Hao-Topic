"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ResultPageCard = void 0;
var _react = _interopRequireDefault(require("react"));
var _nativeProps = require("../../utils/native-props");
var _classnames = _interopRequireDefault(require("classnames"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-result-page-card`;
const ResultPageCard = props => {
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement('div', {
    className: (0, _classnames.default)(`${classPrefix}`)
  }, props.children));
};
exports.ResultPageCard = ResultPageCard;