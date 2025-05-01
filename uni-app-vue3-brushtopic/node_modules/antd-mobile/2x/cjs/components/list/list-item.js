"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ListItem = void 0;
var _antdMobileIcons = require("antd-mobile-icons");
var _classnames = _interopRequireDefault(require("classnames"));
var _react = _interopRequireDefault(require("react"));
var _isNodeWithContent = require("../../utils/is-node-with-content");
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _configProvider = require("../config-provider");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-list-item`;
const ListItem = props => {
  var _a, _b;
  const {
    arrow,
    arrowIcon
  } = props;
  const {
    list: componentConfig = {}
  } = (0, _configProvider.useConfig)();
  const clickable = (_a = props.clickable) !== null && _a !== void 0 ? _a : !!props.onClick;
  const showArrow = (_b = arrow !== null && arrow !== void 0 ? arrow : arrowIcon) !== null && _b !== void 0 ? _b : clickable;
  const mergedArrowIcon = (0, _withDefaultProps.mergeProp)(componentConfig.arrowIcon, arrow !== true ? arrow : null, arrowIcon !== true ? arrowIcon : null);
  const content = _react.default.createElement("div", {
    className: `${classPrefix}-content`
  }, (0, _isNodeWithContent.isNodeWithContent)(props.prefix) && _react.default.createElement("div", {
    className: `${classPrefix}-content-prefix`
  }, props.prefix), _react.default.createElement("div", {
    className: `${classPrefix}-content-main`
  }, (0, _isNodeWithContent.isNodeWithContent)(props.title) && _react.default.createElement("div", {
    className: `${classPrefix}-title`
  }, props.title), props.children, (0, _isNodeWithContent.isNodeWithContent)(props.description) && _react.default.createElement("div", {
    className: `${classPrefix}-description`
  }, props.description)), (0, _isNodeWithContent.isNodeWithContent)(props.extra) && _react.default.createElement("div", {
    className: `${classPrefix}-content-extra`
  }, props.extra), showArrow && _react.default.createElement("div", {
    className: `${classPrefix}-content-arrow`
  }, mergedArrowIcon || _react.default.createElement(_antdMobileIcons.RightOutline, null)));
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement(clickable ? 'a' : 'div', {
    className: (0, _classnames.default)(`${classPrefix}`, clickable ? ['adm-plain-anchor'] : [], props.disabled && `${classPrefix}-disabled`),
    onClick: props.disabled ? undefined : props.onClick
  }, content));
};
exports.ListItem = ListItem;