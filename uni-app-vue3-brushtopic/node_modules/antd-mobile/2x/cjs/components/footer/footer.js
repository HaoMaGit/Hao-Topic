"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Footer = void 0;
var _react = _interopRequireDefault(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _divider = _interopRequireDefault(require("../divider"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-footer`;
const defaultProps = {
  label: '',
  links: [],
  content: '',
  chips: []
};
const Footer = p => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const {
    label,
    links,
    content,
    chips,
    onChipClick,
    onLinkClick
  } = props;
  const clickChipItem = (item, index) => {
    if ((chips === null || chips === void 0 ? void 0 : chips.length) && item.type === 'link') {
      onChipClick === null || onChipClick === void 0 ? void 0 : onChipClick(item, index);
    }
  };
  const clickLinkItem = (item, index, e) => {
    if (onLinkClick) {
      e.preventDefault();
      onLinkClick(item, index);
    }
  };
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: (0, _classnames.default)(classPrefix)
  }, label && _react.default.createElement("div", {
    className: `${classPrefix}-label`
  }, _react.default.createElement(_divider.default, null, label)), !!(links === null || links === void 0 ? void 0 : links.length) && _react.default.createElement("div", {
    className: `${classPrefix}-links`
  }, links.map((link, index) => _react.default.createElement(_react.default.Fragment, {
    key: index
  }, _react.default.createElement("a", {
    href: link.href,
    rel: 'noopener noreferrer',
    onClick: event => clickLinkItem(link, index, event)
  }, link.text), index !== links.length - 1 && _react.default.createElement(_divider.default, {
    direction: 'vertical'
  })))), content && _react.default.createElement("div", {
    className: `${classPrefix}-content`
  }, content), chips && chips.length > 0 && _react.default.createElement("div", {
    className: `${classPrefix}-chips`
  }, chips.map((chip, index) => _react.default.createElement("div", {
    key: index,
    onClick: () => clickChipItem(chip, index),
    className: (0, _classnames.default)(`${classPrefix}-chip`, {
      [`${classPrefix}-chip-link`]: chip.type === 'link'
    })
  }, chip.text)))));
};
exports.Footer = Footer;