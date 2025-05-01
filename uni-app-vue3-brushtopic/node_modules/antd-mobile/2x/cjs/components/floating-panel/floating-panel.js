"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.FloatingPanel = void 0;
var _web = require("@react-spring/web");
var _react = require("@use-gesture/react");
var _ahooks = require("ahooks");
var _classnames = _interopRequireDefault(require("classnames"));
var _react2 = _interopRequireWildcard(require("react"));
var _nativeProps = require("../../utils/native-props");
var _nearest = require("../../utils/nearest");
var _supportsPassive = require("../../utils/supports-passive");
var _useLockScroll = require("../../utils/use-lock-scroll");
var _withDefaultProps = require("../../utils/with-default-props");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = 'adm-floating-panel';
const defaultProps = {
  handleDraggingOfContent: true
};
const FloatingPanel = (0, _react2.forwardRef)((p, ref) => {
  var _a, _b;
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const {
    anchors,
    placement = 'bottom'
  } = props;
  const maxHeight = (_a = anchors[anchors.length - 1]) !== null && _a !== void 0 ? _a : window.innerHeight;
  const isBottomPlacement = placement !== 'top';
  const possibles = isBottomPlacement ? anchors.map(x => -x) : anchors;
  const elementRef = (0, _react2.useRef)(null);
  const headerRef = (0, _react2.useRef)(null);
  const contentRef = (0, _react2.useRef)(null);
  const [pulling, setPulling] = (0, _react2.useState)(false);
  const pullingRef = (0, _react2.useRef)(false);
  const bounds = {
    top: Math.min(...possibles),
    bottom: Math.max(...possibles)
  };
  const onHeightChange = (0, _ahooks.useMemoizedFn)((_b = props.onHeightChange) !== null && _b !== void 0 ? _b : () => {});
  const [{
    y
  }, api] = (0, _web.useSpring)(() => ({
    y: isBottomPlacement ? bounds.bottom : bounds.top,
    config: {
      tension: 300
    },
    onChange: result => {
      onHeightChange(-result.value.y, y.isAnimating);
    }
  }));
  (0, _react.useDrag)(state => {
    const [, offsetY] = state.offset;
    if (state.first) {
      const target = state.event.target;
      const header = headerRef.current;
      if (header === target || (header === null || header === void 0 ? void 0 : header.contains(target))) {
        pullingRef.current = true;
      } else {
        if (!props.handleDraggingOfContent) return;
        const reachedTop = y.goal <= bounds.top;
        const content = contentRef.current;
        if (!content) return;
        if (reachedTop) {
          if (content.scrollTop <= 0 && state.direction[1] > 0) {
            pullingRef.current = true;
          }
        } else {
          pullingRef.current = true;
        }
      }
    }
    setPulling(pullingRef.current);
    if (!pullingRef.current) return;
    const {
      event
    } = state;
    if (event.cancelable && _supportsPassive.supportsPassive) {
      event.preventDefault();
    }
    event.stopPropagation();
    let nextY = offsetY;
    if (state.last) {
      pullingRef.current = false;
      setPulling(false);
      nextY = (0, _nearest.nearest)(possibles, offsetY);
    }
    api.start({
      y: nextY
    });
  }, {
    axis: 'y',
    bounds,
    rubberband: true,
    from: () => [0, y.get()],
    pointer: {
      touch: true
    },
    target: elementRef,
    eventOptions: _supportsPassive.supportsPassive ? {
      passive: false
    } : undefined
  });
  (0, _react2.useImperativeHandle)(ref, () => ({
    setHeight: (height, options) => {
      api.start({
        y: -height,
        immediate: options === null || options === void 0 ? void 0 : options.immediate
      });
    }
  }), [api]);
  (0, _useLockScroll.useLockScroll)(elementRef, true);
  const HeaderNode = _react2.default.createElement("div", {
    className: `${classPrefix}-header`,
    ref: headerRef
  }, _react2.default.createElement("div", {
    className: `${classPrefix}-bar`
  }));
  return (0, _nativeProps.withNativeProps)(props, _react2.default.createElement(_web.animated.div, {
    ref: elementRef,
    className: (0, _classnames.default)(classPrefix, `${classPrefix}-${placement}`),
    style: {
      height: Math.round(maxHeight),
      translateY: y.to(y => {
        if (isBottomPlacement) {
          return `calc(100% + (${Math.round(y)}px))`;
        }
        if (placement === 'top') {
          return `calc(-100% + (${Math.round(y)}px))`;
        }
        return y;
      })
    }
  }, _react2.default.createElement("div", {
    className: `${classPrefix}-mask`,
    style: {
      display: pulling ? 'block' : 'none'
    }
  }), isBottomPlacement && HeaderNode, _react2.default.createElement("div", {
    className: `${classPrefix}-content`,
    ref: contentRef
  }, props.children), placement === 'top' && HeaderNode));
});
exports.FloatingPanel = FloatingPanel;