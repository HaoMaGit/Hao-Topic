"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.ItemChildrenWrap = void 0;
var _antdMobileIcons = require("antd-mobile-icons");
var _classnames = _interopRequireDefault(require("classnames"));
var _react = _interopRequireDefault(require("react"));
var _nativeProps = require("../../utils/native-props");
var _shouldRender = require("../../utils/should-render");
var _withDefaultProps = require("../../utils/with-default-props");
var _configProvider = require("../config-provider");
var _context = require("./context");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-dropdown-item`;
const Item = props => {
  const {
    dropdown: componentConfig = {}
  } = (0, _configProvider.useConfig)();
  const mergedProps = (0, _withDefaultProps.mergeProps)(componentConfig, props);
  const {
    active,
    highlight,
    onClick,
    title
  } = mergedProps;
  const cls = (0, _classnames.default)(classPrefix, {
    [`${classPrefix}-active`]: active,
    [`${classPrefix}-highlight`]: highlight !== null && highlight !== void 0 ? highlight : active
  });
  const contextArrowIcon = _react.default.useContext(_context.IconContext);
  const mergedArrowIcon = (0, _withDefaultProps.mergeProp)(_react.default.createElement(_antdMobileIcons.DownFill, null), contextArrowIcon, mergedProps.arrow, mergedProps.arrowIcon);
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: cls,
    onClick: onClick
  }, _react.default.createElement("div", {
    className: `${classPrefix}-title`
  }, _react.default.createElement("span", {
    className: `${classPrefix}-title-text`
  }, title), _react.default.createElement("span", {
    className: (0, _classnames.default)(`${classPrefix}-title-arrow`, {
      [`${classPrefix}-title-arrow-active`]: active
    })
  }, mergedArrowIcon))));
};
var _default = Item;
exports.default = _default;
const ItemChildrenWrap = props => {
  const {
    active = false
  } = props;
  const shouldRender = (0, _shouldRender.useShouldRender)(active, props.forceRender, props.destroyOnClose);
  const cls = (0, _classnames.default)(`${classPrefix}-content`, {
    [`${classPrefix}-content-hidden`]: !active
  });
  return shouldRender ? _react.default.createElement("div", {
    className: cls,
    onClick: props.onClick
  }, props.children) : null;
};
exports.ItemChildrenWrap = ItemChildrenWrap;