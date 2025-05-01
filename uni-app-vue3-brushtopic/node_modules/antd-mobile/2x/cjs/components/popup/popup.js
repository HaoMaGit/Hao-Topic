"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Popup = void 0;
var _classnames = _interopRequireDefault(require("classnames"));
var _react = _interopRequireWildcard(require("react"));
var _ahooks = require("ahooks");
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _mask = _interopRequireDefault(require("../mask"));
var _useLockScroll = require("../../utils/use-lock-scroll");
var _renderToContainer = require("../../utils/render-to-container");
var _web = require("@react-spring/web");
var _withStopPropagation = require("../../utils/with-stop-propagation");
var _shouldRender = require("../../utils/should-render");
var _popupBaseProps = require("./popup-base-props");
var _useInnerVisible = require("../../utils/use-inner-visible");
var _configProvider = require("../config-provider");
var _react2 = require("@use-gesture/react");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-popup`;
const defaultProps = Object.assign(Object.assign({}, _popupBaseProps.defaultPopupBaseProps), {
  closeOnSwipe: false,
  position: 'bottom'
});
const Popup = p => {
  const {
    locale,
    popup: componentConfig = {}
  } = (0, _configProvider.useConfig)();
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, componentConfig, p);
  const bodyCls = (0, _classnames.default)(`${classPrefix}-body`, props.bodyClassName, `${classPrefix}-body-position-${props.position}`);
  const [active, setActive] = (0, _react.useState)(props.visible);
  const ref = (0, _react.useRef)(null);
  (0, _useLockScroll.useLockScroll)(ref, props.disableBodyScroll && active ? 'strict' : false);
  (0, _ahooks.useIsomorphicLayoutEffect)(() => {
    if (props.visible) {
      setActive(true);
    }
  }, [props.visible]);
  const unmountedRef = (0, _ahooks.useUnmountedRef)();
  const {
    percent
  } = (0, _web.useSpring)({
    percent: props.visible ? 0 : 100,
    config: {
      precision: 0.1,
      mass: 0.4,
      tension: 300,
      friction: 30
    },
    onRest: () => {
      var _a, _b;
      if (unmountedRef.current) return;
      setActive(props.visible);
      if (props.visible) {
        (_a = props.afterShow) === null || _a === void 0 ? void 0 : _a.call(props);
      } else {
        (_b = props.afterClose) === null || _b === void 0 ? void 0 : _b.call(props);
      }
    }
  });
  const bind = (0, _react2.useDrag)(({
    swipe: [, swipeY]
  }) => {
    var _a;
    if (!props.closeOnSwipe) return;
    if (swipeY === 1 && props.position === 'bottom' || swipeY === -1 && props.position === 'top') {
      (_a = props.onClose) === null || _a === void 0 ? void 0 : _a.call(props);
    }
  }, {
    axis: 'y',
    enabled: ['top', 'bottom'].includes(props.position)
  });
  const maskVisible = (0, _useInnerVisible.useInnerVisible)(active && props.visible);
  const node = (0, _withStopPropagation.withStopPropagation)(props.stopPropagation, (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", Object.assign({
    className: classPrefix,
    onClick: props.onClick,
    style: {
      display: active ? undefined : 'none',
      touchAction: ['top', 'bottom'].includes(props.position) ? 'none' : 'auto'
    }
  }, bind()), props.mask && _react.default.createElement(_mask.default, {
    visible: maskVisible,
    forceRender: props.forceRender,
    destroyOnClose: props.destroyOnClose,
    onMaskClick: e => {
      var _a, _b;
      (_a = props.onMaskClick) === null || _a === void 0 ? void 0 : _a.call(props, e);
      if (props.closeOnMaskClick) {
        (_b = props.onClose) === null || _b === void 0 ? void 0 : _b.call(props);
      }
    },
    className: props.maskClassName,
    style: props.maskStyle,
    disableBodyScroll: false,
    stopPropagation: props.stopPropagation
  }), _react.default.createElement(_web.animated.div, {
    className: bodyCls,
    style: Object.assign(Object.assign({}, props.bodyStyle), {
      pointerEvents: percent.to(v => v === 0 ? 'unset' : 'none'),
      transform: percent.to(v => {
        if (props.position === 'bottom') {
          return `translate(0, ${v}%)`;
        }
        if (props.position === 'top') {
          return `translate(0, -${v}%)`;
        }
        if (props.position === 'left') {
          return `translate(-${v}%, 0)`;
        }
        if (props.position === 'right') {
          return `translate(${v}%, 0)`;
        }
        return 'none';
      })
    }),
    ref: ref
  }, props.showCloseButton && _react.default.createElement("a", {
    className: (0, _classnames.default)(`${classPrefix}-close-icon`, 'adm-plain-anchor'),
    onClick: () => {
      var _a;
      (_a = props.onClose) === null || _a === void 0 ? void 0 : _a.call(props);
    },
    role: 'button',
    "aria-label": locale.common.close
  }, props.closeIcon), props.children))));
  return _react.default.createElement(_shouldRender.ShouldRender, {
    active: active,
    forceRender: props.forceRender,
    destroyOnClose: props.destroyOnClose
  }, (0, _renderToContainer.renderToContainer)(props.getContainer, node));
};
exports.Popup = Popup;