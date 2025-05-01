"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Wheel = void 0;
var _react = _interopRequireWildcard(require("react"));
var _web = require("@react-spring/web");
var _react2 = require("@use-gesture/react");
var _rubberband = require("../../utils/rubberband");
var _bound = require("../../utils/bound");
var _reactFastCompare = _interopRequireDefault(require("react-fast-compare"));
var _ahooks = require("ahooks");
var _measureCssLength = require("../../utils/measure-css-length");
var _supportsPassive = require("../../utils/supports-passive");
var _classnames = _interopRequireDefault(require("classnames"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const classPrefix = `adm-picker-view`;
const Wheel = (0, _react.memo)(props => {
  const {
    value,
    column,
    renderLabel
  } = props;
  function onSelect(val) {
    props.onSelect(val, props.index);
  }
  const [{
    y
  }, api] = (0, _web.useSpring)(() => ({
    from: {
      y: 0
    },
    config: {
      tension: 400,
      mass: 0.8
    }
  }));
  const draggingRef = (0, _react.useRef)(false);
  const rootRef = (0, _react.useRef)(null);
  const itemHeightMeasureRef = (0, _react.useRef)(null);
  const itemHeight = (0, _react.useRef)(34);
  (0, _ahooks.useIsomorphicLayoutEffect)(() => {
    const itemHeightMeasure = itemHeightMeasureRef.current;
    if (!itemHeightMeasure) return;
    itemHeight.current = (0, _measureCssLength.measureCSSLength)(window.getComputedStyle(itemHeightMeasure).getPropertyValue('height'));
  });
  (0, _ahooks.useIsomorphicLayoutEffect)(() => {
    if (draggingRef.current) return;
    if (value === null) return;
    const targetIndex = column.findIndex(item => item.value === value);
    if (targetIndex < 0) return;
    const finalPosition = targetIndex * -itemHeight.current;
    api.start({
      y: finalPosition,
      immediate: y.goal !== finalPosition
    });
  }, [value, column]);
  (0, _ahooks.useIsomorphicLayoutEffect)(() => {
    if (column.length === 0) {
      if (value !== null) {
        onSelect(null);
      }
    } else {
      if (!column.some(item => item.value === value)) {
        const firstItem = column[0];
        onSelect(firstItem.value);
      }
    }
  }, [column, value]);
  function scrollSelect(index) {
    const finalPosition = index * -itemHeight.current;
    api.start({
      y: finalPosition
    });
    const item = column[index];
    if (!item) return;
    onSelect(item.value);
  }
  const handleGestureState = state => {
    const {
      direction: [, direction],
      distance: [, distance],
      velocity: [, velocity],
      offset: [, offset],
      last
    } = state;
    return {
      direction,
      distance,
      velocity,
      offset,
      last
    };
  };
  const handleDrag = state => {
    draggingRef.current = true;
    const min = -((column.length - 1) * itemHeight.current);
    const max = 0;
    const {
      direction,
      last,
      velocity,
      offset
    } = handleGestureState(state);
    if (last) {
      draggingRef.current = false;
      const position = offset + velocity * direction * 50;
      const boundNum = (0, _bound.bound)(position, min, max);
      const targetIndex = -Math.round(boundNum / itemHeight.current);
      scrollSelect(targetIndex);
    } else {
      const position = offset;
      api.start({
        y: (0, _rubberband.rubberbandIfOutOfBounds)(position, min, max, itemHeight.current * 50, 0.2)
      });
    }
  };
  const handleWheel = state => {
    draggingRef.current = true;
    const min = -((column.length - 1) * itemHeight.current);
    const max = 0;
    const {
      direction,
      last,
      velocity,
      distance
    } = handleGestureState(state);
    const whellDir = -direction; // 取反
    const scrollY = y.get();
    if (last) {
      draggingRef.current = false;
      const speed = velocity * whellDir * 50;
      const position = scrollY + distance * whellDir + speed;
      const boundNum = (0, _bound.bound)(position, min, max);
      const targetIndex = -Math.round(boundNum / itemHeight.current);
      scrollSelect(targetIndex);
    } else {
      const position = scrollY + distance * whellDir;
      api.start({
        y: (0, _rubberband.rubberbandIfOutOfBounds)(position, min, max, itemHeight.current * 50, 0.2)
      });
    }
  };
  (0, _react2.useDrag)(state => {
    state.event.stopPropagation();
    handleDrag(state);
  }, {
    axis: 'y',
    from: () => [0, y.get()],
    filterTaps: true,
    pointer: {
      touch: true
    },
    target: rootRef
  });
  (0, _react2.useWheel)(state => {
    state.event.stopPropagation();
    handleWheel(state);
  }, {
    target: props.mouseWheel ? rootRef : undefined,
    axis: 'y',
    from: () => [0, y.get()],
    preventDefault: true,
    eventOptions: _supportsPassive.supportsPassive ? {
      passive: false
    } : undefined
  });
  let selectedIndex = null;
  function renderAccessible() {
    if (selectedIndex === null) {
      return null;
    }
    const current = column[selectedIndex];
    const previousIndex = selectedIndex - 1;
    const nextIndex = selectedIndex + 1;
    const previous = column[previousIndex];
    const next = column[nextIndex];
    return _react.default.createElement("div", {
      className: `${classPrefix}-column-accessible`
    }, _react.default.createElement("div", {
      className: `${classPrefix}-column-accessible-current`,
      role: 'button',
      "aria-label": current ? `当前选择的是：${current.label}` : '当前未选择'
    }, "-"), _react.default.createElement("div", {
      className: `${classPrefix}-column-accessible-button`,
      onClick: () => {
        if (!previous) return;
        scrollSelect(previousIndex);
      },
      role: previous ? 'button' : 'text',
      "aria-label": !previous ? '没有上一项' : `选择上一项：${previous.label}`
    }, "-"), _react.default.createElement("div", {
      className: `${classPrefix}-column-accessible-button`,
      onClick: () => {
        if (!next) return;
        scrollSelect(nextIndex);
      },
      role: next ? 'button' : 'text',
      "aria-label": !next ? '没有下一项' : `选择下一项：${next.label}`
    }, "-"));
  }
  return _react.default.createElement("div", {
    className: `${classPrefix}-column`
  }, _react.default.createElement("div", {
    className: `${classPrefix}-item-height-measure`,
    ref: itemHeightMeasureRef
  }), _react.default.createElement(_web.animated.div, {
    ref: rootRef,
    style: {
      translateY: y
    },
    className: `${classPrefix}-column-wheel`,
    "aria-hidden": true
  }, column.map((item, index) => {
    var _a;
    const selected = props.value === item.value;
    if (selected) selectedIndex = index;
    function handleClick() {
      draggingRef.current = false;
      scrollSelect(index);
    }
    return _react.default.createElement("div", {
      key: (_a = item.key) !== null && _a !== void 0 ? _a : item.value,
      "data-selected": selected,
      className: (0, _classnames.default)(`${classPrefix}-column-item`, {
        [`${classPrefix}-column-item-active`]: selected
      }),
      onClick: handleClick,
      "aria-hidden": !selected,
      "aria-label": selected ? 'active' : ''
    }, _react.default.createElement("div", {
      className: `${classPrefix}-column-item-label`
    }, renderLabel(item)));
  })), renderAccessible());
}, (prev, next) => {
  if (prev.index !== next.index) return false;
  if (prev.value !== next.value) return false;
  if (prev.onSelect !== next.onSelect) return false;
  if (prev.renderLabel !== next.renderLabel) return false;
  if (prev.mouseWheel !== next.mouseWheel) return false;
  if (!(0, _reactFastCompare.default)(prev.column, next.column)) return false;
  return true;
});
exports.Wheel = Wheel;
Wheel.displayName = 'Wheel';