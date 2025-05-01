"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useResultIcon = void 0;
var _react = _interopRequireDefault(require("react"));
var _antdMobileIcons = require("antd-mobile-icons");
var _configProvider = require("../config-provider");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const useResultIcon = status => {
  const {
    result: componentConfig = {}
  } = (0, _configProvider.useConfig)();
  const {
    successIcon = _react.default.createElement(_antdMobileIcons.CheckCircleFill, null),
    errorIcon = _react.default.createElement(_antdMobileIcons.CloseCircleFill, null),
    infoIcon = _react.default.createElement(_antdMobileIcons.InformationCircleFill, null),
    waitingIcon = _react.default.createElement(_antdMobileIcons.ClockCircleFill, null),
    warningIcon = _react.default.createElement(_antdMobileIcons.ExclamationCircleFill, null)
  } = componentConfig || {};
  switch (status) {
    case 'success':
      return successIcon;
    case 'error':
      return errorIcon;
    case 'info':
      return infoIcon;
    case 'waiting':
      return waitingIcon;
    case 'warning':
      return warningIcon;
    default:
      return null;
  }
};
exports.useResultIcon = useResultIcon;