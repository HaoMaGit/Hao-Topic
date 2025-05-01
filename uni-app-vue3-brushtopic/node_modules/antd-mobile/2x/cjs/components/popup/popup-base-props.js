"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.defaultPopupBaseProps = void 0;
var _react = _interopRequireDefault(require("react"));
var _antdMobileIcons = require("antd-mobile-icons");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const defaultPopupBaseProps = {
  closeOnMaskClick: false,
  closeIcon: _react.default.createElement(_antdMobileIcons.CloseOutline, null),
  destroyOnClose: false,
  disableBodyScroll: true,
  forceRender: false,
  getContainer: () => document.body,
  mask: true,
  showCloseButton: false,
  stopPropagation: ['click'],
  visible: false
};
exports.defaultPopupBaseProps = defaultPopupBaseProps;