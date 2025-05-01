"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ResultPage = void 0;
var _react = _interopRequireWildcard(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _isNodeWithContent = require("../../utils/is-node-with-content");
var _button = _interopRequireDefault(require("../button"));
var _useResultIcon = require("../result/use-result-icon");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const classPrefix = `adm-result-page`;
const defaultProps = {
  status: 'info',
  details: []
};
const ResultPage = p => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const {
    status,
    title,
    description,
    details,
    icon,
    primaryButtonText,
    secondaryButtonText,
    onPrimaryButtonClick,
    onSecondaryButtonClick
  } = props;
  const fallbackIcon = (0, _useResultIcon.useResultIcon)(status);
  const [collapse, setCollapse] = (0, _react.useState)(true);
  const showSecondaryButton = (0, _isNodeWithContent.isNodeWithContent)(secondaryButtonText);
  const showPrimaryButton = (0, _isNodeWithContent.isNodeWithContent)(primaryButtonText);
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: classPrefix
  }, _react.default.createElement("div", {
    className: `${classPrefix}-header`
  }, _react.default.createElement("div", {
    className: `${classPrefix}-icon`
  }, icon || fallbackIcon), _react.default.createElement("div", {
    className: `${classPrefix}-title`
  }, title), (0, _isNodeWithContent.isNodeWithContent)(description) ? _react.default.createElement("div", {
    className: `${classPrefix}-description`
  }, description) : null, (details === null || details === void 0 ? void 0 : details.length) ? _react.default.createElement("div", {
    className: `${classPrefix}-details`
  }, (collapse ? details.slice(0, 3) : details).map((detail, index) => {
    return _react.default.createElement("div", {
      className: (0, _classnames.default)(`${classPrefix}-detail`, detail.bold && `${classPrefix}-detail-bold`),
      key: index
    }, _react.default.createElement("span", null, detail.label), _react.default.createElement("span", null, detail.value));
  }), details.length > 3 && _react.default.createElement("div", {
    onClick: () => setCollapse(prev => !prev)
  }, _react.default.createElement("div", {
    className: (0, _classnames.default)(`${classPrefix}-collapse`, !collapse && `${classPrefix}-collapse-active`)
  }))) : null, _react.default.createElement("div", {
    className: `${classPrefix}-bgWrapper`
  }, _react.default.createElement("div", {
    className: `${classPrefix}-bg`
  }))), _react.default.createElement("div", {
    className: `${classPrefix}-content`
  }, props.children), (showPrimaryButton || showSecondaryButton) && _react.default.createElement("div", {
    className: `${classPrefix}-footer`
  }, showSecondaryButton && _react.default.createElement(_button.default, {
    block: true,
    color: 'default',
    fill: 'solid',
    size: 'large',
    onClick: onSecondaryButtonClick,
    className: `${classPrefix}-footer-btn`
  }, secondaryButtonText), showPrimaryButton && showSecondaryButton && _react.default.createElement("div", {
    className: `${classPrefix}-footer-space`
  }), showPrimaryButton && _react.default.createElement(_button.default, {
    block: true,
    color: 'primary',
    fill: 'solid',
    size: 'large',
    onClick: onPrimaryButtonClick,
    className: `${classPrefix}-footer-btn`
  }, primaryButtonText))));
};
exports.ResultPage = ResultPage;