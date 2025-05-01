"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.createErrorBlock = createErrorBlock;
var _react = _interopRequireDefault(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _withDefaultProps = require("../../utils/with-default-props");
var _nativeProps = require("../../utils/native-props");
var _configProvider = require("../config-provider");
require("./error-block.css");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-error-block`;
const defaultProps = {
  status: 'default'
};
function createErrorBlock(imageRecord) {
  const ErrorBlock = p => {
    var _a;
    const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
    const {
      locale
    } = (0, _configProvider.useConfig)();
    const contentPack = locale.ErrorBlock[props.status];
    const desc = 'description' in props ? props.description : contentPack.description;
    const title = 'title' in props ? props.title : contentPack.title;
    const image = (_a = props.image) !== null && _a !== void 0 ? _a : imageRecord[props.status];
    const imageNode = typeof image === 'string' ? _react.default.createElement("img", {
      src: image,
      alt: 'error block image'
    }) : image;
    return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
      className: (0, _classnames.default)(classPrefix, {
        [`${classPrefix}-full-page`]: props.fullPage
      })
    }, _react.default.createElement("div", {
      className: `${classPrefix}-image`
    }, imageNode), _react.default.createElement("div", {
      className: `${classPrefix}-description`
    }, ![undefined, null].includes(title) && _react.default.createElement("div", {
      className: `${classPrefix}-description-title`
    }, title), ![undefined, null].includes(desc) && _react.default.createElement("div", {
      className: `${classPrefix}-description-subtitle`
    }, desc)), props.children && _react.default.createElement("div", {
      className: `${classPrefix}-content`
    }, props.children)));
  };
  return ErrorBlock;
}