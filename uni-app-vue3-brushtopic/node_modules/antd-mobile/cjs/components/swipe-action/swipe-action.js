"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.SwipeAction = void 0;
var _web = require("@react-spring/web");
var _react = require("@use-gesture/react");
var _react2 = _interopRequireWildcard(require("react"));
var _nativeProps = require("../../utils/native-props");
var _nearest = require("../../utils/nearest");
var _withDefaultProps = require("../../utils/with-default-props");
var _withStopPropagation = require("../../utils/with-stop-propagation");
var _button = _interopRequireDefault(require("../button"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const classPrefix = `adm-swipe-action`;
const defaultProps = {
  rightActions: [],
  leftActions: [],
  closeOnTouchOutside: true,
  closeOnAction: true,
  stopPropagation: []
};
const SwipeAction = (0, _react2.forwardRef)((p, ref) => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const rootRef = (0, _react2.useRef)(null);
  const leftRef = (0, _react2.useRef)(null);
  const rightRef = (0, _react2.useRef)(null);
  function getWidth(ref) {
    const element = ref.current;
    if (!element) return 0;
    return element.offsetWidth;
  }
  function getLeftWidth() {
    return getWidth(leftRef);
  }
  function getRightWidth() {
    return getWidth(rightRef);
  }
  const [{
    x
  }, api] = (0, _web.useSpring)(() => ({
    x: 0,
    config: {
      tension: 200,
      friction: 30
    }
  }), []);
  const draggingRef = (0, _react2.useRef)(false);
  const dragCancelRef = (0, _react2.useRef)(null);
  function forceCancelDrag() {
    var _a;
    (_a = dragCancelRef.current) === null || _a === void 0 ? void 0 : _a.call(dragCancelRef);
    draggingRef.current = false;
  }
  const bind = (0, _react.useDrag)(state => {
    var _a;
    dragCancelRef.current = state.cancel;
    if (!state.intentional) return;
    if (state.down) {
      draggingRef.current = true;
    }
    if (!draggingRef.current) return;
    const [offsetX] = state.offset;
    if (state.last) {
      const leftWidth = getLeftWidth();
      const rightWidth = getRightWidth();
      let position = offsetX + state.velocity[0] * state.direction[0] * 50;
      if (offsetX > 0) {
        position = Math.max(0, position);
      } else if (offsetX < 0) {
        position = Math.min(0, position);
      } else {
        position = 0;
      }
      const targetX = (0, _nearest.nearest)([-rightWidth, 0, leftWidth], position);
      api.start({
        x: targetX
      });
      if (targetX !== 0) {
        (_a = p.onActionsReveal) === null || _a === void 0 ? void 0 : _a.call(p, targetX > 0 ? 'left' : 'right');
      }
      window.setTimeout(() => {
        draggingRef.current = false;
      });
    } else {
      api.start({
        x: offsetX,
        immediate: true
      });
    }
  }, {
    from: () => [x.get(), 0],
    bounds: () => {
      const leftWidth = getLeftWidth();
      const rightWidth = getRightWidth();
      return {
        left: -rightWidth,
        right: leftWidth
      };
    },
    axis: 'x',
    preventScroll: true,
    pointer: {
      touch: true
    },
    triggerAllEvents: true
  });
  const close = () => {
    var _a;
    api.start({
      x: 0
    });
    forceCancelDrag();
    (_a = props.onClose) === null || _a === void 0 ? void 0 : _a.call(props);
  };
  (0, _react2.useImperativeHandle)(ref, () => ({
    show: (side = 'right') => {
      var _a;
      if (side === 'right') {
        api.start({
          x: -getRightWidth()
        });
      } else if (side === 'left') {
        api.start({
          x: getLeftWidth()
        });
      }
      (_a = p.onActionsReveal) === null || _a === void 0 ? void 0 : _a.call(p, side);
    },
    close
  }));
  (0, _react2.useEffect)(() => {
    if (!props.closeOnTouchOutside) return;
    function handle(e) {
      if (x.get() === 0) {
        return;
      }
      const root = rootRef.current;
      if (root && !root.contains(e.target)) {
        close();
      }
    }
    document.addEventListener('touchstart', handle);
    return () => {
      document.removeEventListener('touchstart', handle);
    };
  }, [props.closeOnTouchOutside]);
  function renderAction(action) {
    var _a, _b;
    const color = (_a = action.color) !== null && _a !== void 0 ? _a : 'light';
    return _react2.default.createElement(_button.default, {
      key: action.key,
      className: `${classPrefix}-action-button`,
      style: {
        '--background-color': (_b = colorRecord[color]) !== null && _b !== void 0 ? _b : color
      },
      onClick: e => {
        var _a, _b;
        if (props.closeOnAction) {
          close();
        }
        (_a = action.onClick) === null || _a === void 0 ? void 0 : _a.call(action, e);
        (_b = props.onAction) === null || _b === void 0 ? void 0 : _b.call(props, action, e);
      }
    }, action.text);
  }
  return (0, _nativeProps.withNativeProps)(props, _react2.default.createElement("div", Object.assign({
    className: classPrefix
  }, bind(), {
    ref: rootRef,
    onClickCapture: e => {
      if (draggingRef.current) {
        e.stopPropagation();
        e.preventDefault();
      }
    }
  }), _react2.default.createElement(_web.animated.div, {
    className: `${classPrefix}-track`,
    style: {
      x
    }
  }, (0, _withStopPropagation.withStopPropagation)(props.stopPropagation, _react2.default.createElement("div", {
    className: `${classPrefix}-actions ${classPrefix}-actions-left`,
    ref: leftRef
  }, props.leftActions.map(renderAction))), _react2.default.createElement("div", {
    className: `${classPrefix}-content`,
    onClickCapture: e => {
      if (x.goal !== 0) {
        e.preventDefault();
        e.stopPropagation();
        close();
      }
    }
  }, _react2.default.createElement(_web.animated.div, {
    style: {
      pointerEvents: x.to(v => v !== 0 && x.goal !== 0 ? 'none' : 'auto')
    }
  }, props.children)), (0, _withStopPropagation.withStopPropagation)(props.stopPropagation, _react2.default.createElement("div", {
    className: `${classPrefix}-actions ${classPrefix}-actions-right`,
    ref: rightRef
  }, props.rightActions.map(renderAction))))));
});
exports.SwipeAction = SwipeAction;
const colorRecord = {
  light: 'var(--adm-color-light)',
  weak: 'var(--adm-color-weak)',
  primary: 'var(--adm-color-primary)',
  success: 'var(--adm-color-success)',
  warning: 'var(--adm-color-warning)',
  danger: 'var(--adm-color-danger)'
};