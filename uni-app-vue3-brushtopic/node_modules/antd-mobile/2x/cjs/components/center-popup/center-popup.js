"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.CenterPopup = void 0;
var _web = require("@react-spring/web");
var _ahooks = require("ahooks");
var _classnames = _interopRequireDefault(require("classnames"));
var _react = _interopRequireWildcard(require("react"));
var _nativeProps = require("../../utils/native-props");
var _renderToContainer = require("../../utils/render-to-container");
var _shouldRender = require("../../utils/should-render");
var _useInnerVisible = require("../../utils/use-inner-visible");
var _useLockScroll = require("../../utils/use-lock-scroll");
var _withDefaultProps = require("../../utils/with-default-props");
var _withStopPropagation = require("../../utils/with-stop-propagation");
var _configProvider = require("../config-provider");
var _mask = _interopRequireDefault(require("../mask"));
var _popupBaseProps = require("../popup/popup-base-props");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = 'adm-center-popup';
const defaultProps = Object.assign(Object.assign({}, _popupBaseProps.defaultPopupBaseProps), {
  getContainer: null
});
const CenterPopup = props => {
  const {
    popup: componentConfig = {}
  } = (0, _configProvider.useConfig)();
  const mergedProps = (0, _withDefaultProps.mergeProps)(defaultProps, componentConfig, props);
  const unmountedRef = (0, _ahooks.useUnmountedRef)();
  const style = (0, _web.useSpring)({
    scale: mergedProps.visible ? 1 : 0.8,
    opacity: mergedProps.visible ? 1 : 0,
    config: {
      mass: 1.2,
      tension: 200,
      friction: 25,
      clamp: true
    },
    onRest: () => {
      var _a, _b;
      if (unmountedRef.current) return;
      setActive(mergedProps.visible);
      if (mergedProps.visible) {
        (_a = mergedProps.afterShow) === null || _a === void 0 ? void 0 : _a.call(mergedProps);
      } else {
        (_b = mergedProps.afterClose) === null || _b === void 0 ? void 0 : _b.call(mergedProps);
      }
    }
  });
  const [active, setActive] = (0, _react.useState)(mergedProps.visible);
  (0, _ahooks.useIsomorphicLayoutEffect)(() => {
    if (mergedProps.visible) {
      setActive(true);
    }
  }, [mergedProps.visible]);
  const ref = (0, _react.useRef)(null);
  (0, _useLockScroll.useLockScroll)(ref, mergedProps.disableBodyScroll && active);
  const maskVisible = (0, _useInnerVisible.useInnerVisible)(active && mergedProps.visible);
  const body = _react.default.createElement("div", {
    className: (0, _classnames.default)(`${classPrefix}-body`, mergedProps.bodyClassName),
    style: mergedProps.bodyStyle
  }, mergedProps.children);
  const node = (0, _withStopPropagation.withStopPropagation)(mergedProps.stopPropagation, (0, _nativeProps.withNativeProps)(mergedProps, _react.default.createElement("div", {
    className: classPrefix,
    style: {
      display: active ? undefined : 'none',
      pointerEvents: active ? undefined : 'none'
    }
  }, mergedProps.mask && _react.default.createElement(_mask.default, {
    visible: maskVisible,
    forceRender: mergedProps.forceRender,
    destroyOnClose: mergedProps.destroyOnClose,
    onMaskClick: e => {
      var _a, _b;
      (_a = mergedProps.onMaskClick) === null || _a === void 0 ? void 0 : _a.call(mergedProps, e);
      if (mergedProps.closeOnMaskClick) {
        (_b = mergedProps.onClose) === null || _b === void 0 ? void 0 : _b.call(mergedProps);
      }
    },
    style: mergedProps.maskStyle,
    className: (0, _classnames.default)(`${classPrefix}-mask`, mergedProps.maskClassName),
    disableBodyScroll: false,
    stopPropagation: mergedProps.stopPropagation
  }), _react.default.createElement("div", {
    className: `${classPrefix}-wrap`,
    role: mergedProps.role,
    "aria-label": mergedProps['aria-label']
  }, _react.default.createElement(_web.animated.div, {
    style: Object.assign(Object.assign({}, style), {
      pointerEvents: style.opacity.to(v => v === 1 ? 'unset' : 'none')
    }),
    ref: ref
  }, mergedProps.showCloseButton && _react.default.createElement("a", {
    className: (0, _classnames.default)(`${classPrefix}-close`, 'adm-plain-anchor'),
    onClick: () => {
      var _a;
      (_a = mergedProps.onClose) === null || _a === void 0 ? void 0 : _a.call(mergedProps);
    }
  }, mergedProps.closeIcon), body)))));
  return _react.default.createElement(_shouldRender.ShouldRender, {
    active: active,
    forceRender: mergedProps.forceRender,
    destroyOnClose: mergedProps.destroyOnClose
  }, (0, _renderToContainer.renderToContainer)(mergedProps.getContainer, node));
};
exports.CenterPopup = CenterPopup;