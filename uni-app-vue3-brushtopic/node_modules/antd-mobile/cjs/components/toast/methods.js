"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.clear = clear;
exports.config = config;
exports.show = show;
var _react = _interopRequireDefault(require("react"));
var _renderImperatively = require("../../utils/render-imperatively");
var _withDefaultProps = require("../../utils/with-default-props");
var _toast = require("./toast");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
let currentHandler = null;
let currentTimeout = null;
const defaultProps = {
  duration: 2000,
  position: 'center',
  maskClickable: true
};
const ToastInner = props => _react.default.createElement(_toast.InternalToast, Object.assign({}, props));
function show(p) {
  var _a;
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, typeof p === 'string' ? {
    content: p
  } : p);
  const element = _react.default.createElement(ToastInner, Object.assign({}, props, {
    onClose: () => {
      currentHandler = null;
    }
  }));
  if (currentHandler) {
    if ((_a = currentHandler.isRendered) === null || _a === void 0 ? void 0 : _a.call(currentHandler)) {
      currentHandler.replace(element);
    } else {
      currentHandler.close();
      currentHandler = (0, _renderImperatively.renderImperatively)(element);
    }
  } else {
    currentHandler = (0, _renderImperatively.renderImperatively)(element);
  }
  if (currentTimeout) {
    window.clearTimeout(currentTimeout);
  }
  if (props.duration !== 0) {
    currentTimeout = window.setTimeout(() => {
      clear();
    }, props.duration);
  }
  return currentHandler;
}
function clear() {
  currentHandler === null || currentHandler === void 0 ? void 0 : currentHandler.close();
  currentHandler = null;
}
function config(val) {
  if (val.duration !== undefined) {
    defaultProps.duration = val.duration;
  }
  if (val.position !== undefined) {
    defaultProps.position = val.position;
  }
  if (val.maskClickable !== undefined) {
    defaultProps.maskClickable = val.maskClickable;
  }
}