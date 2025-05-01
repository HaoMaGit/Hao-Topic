"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.NavBar = void 0;
var _antdMobileIcons = require("antd-mobile-icons");
var _classnames = _interopRequireDefault(require("classnames"));
var _react = _interopRequireDefault(require("react"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _configProvider = require("../config-provider");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-nav-bar`;
const defaultBackIcon = _react.default.createElement(_antdMobileIcons.LeftOutline, null);
const NavBar = props => {
  const {
    navBar: componentConfig = {}
  } = (0, _configProvider.useConfig)();
  const mergedProps = (0, _withDefaultProps.mergeProps)(componentConfig, props);
  const {
    back,
    backIcon,
    backArrow
  } = mergedProps;
  const mergedDefaultBackIcon = componentConfig.backIcon || defaultBackIcon;
  const mergedBackIcon = (0, _withDefaultProps.mergeProp)(defaultBackIcon, componentConfig.backIcon, backArrow === true ? mergedDefaultBackIcon : backArrow, backIcon === true ? mergedDefaultBackIcon : backIcon);
  return (0, _nativeProps.withNativeProps)(mergedProps, _react.default.createElement("div", {
    className: (0, _classnames.default)(classPrefix)
  }, _react.default.createElement("div", {
    className: `${classPrefix}-left`,
    role: 'button'
  }, back !== null && _react.default.createElement("div", {
    className: `${classPrefix}-back`,
    onClick: mergedProps.onBack
  }, mergedBackIcon && _react.default.createElement("span", {
    className: `${classPrefix}-back-arrow`
  }, mergedBackIcon), _react.default.createElement("span", {
    "aria-hidden": 'true'
  }, back)), mergedProps.left), _react.default.createElement("div", {
    className: `${classPrefix}-title`
  }, mergedProps.children), _react.default.createElement("div", {
    className: `${classPrefix}-right`
  }, mergedProps.right)));
};
exports.NavBar = NavBar;