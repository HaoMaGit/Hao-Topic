"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _react = _interopRequireWildcard(require("react"));
var _react2 = require("@use-gesture/react");
var _thumbIcon = require("./thumb-icon");
var _popover = _interopRequireDefault(require("../popover"));
var _configProvider = require("../config-provider");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const classPrefix = `adm-slider`;
const Thumb = props => {
  const {
    value,
    min,
    max,
    disabled,
    icon,
    residentPopover,
    onDrag
  } = props;
  const prevValue = (0, _react.useRef)(value);
  const {
    locale
  } = (0, _configProvider.useConfig)();
  const currentPosition = () => {
    return {
      left: `${(value - min) / (max - min) * 100}%`,
      right: 'auto'
    };
  };
  const [dragging, setDragging] = (0, _react.useState)(false);
  const bind = (0, _react2.useDrag)(state => {
    var _a;
    if (disabled) return;
    if (state.first) {
      prevValue.current = value;
    }
    const x = state.xy[0] - state.initial[0];
    const sliderOffsetWith = (_a = props.trackRef.current) === null || _a === void 0 ? void 0 : _a.offsetWidth;
    if (!sliderOffsetWith) return;
    const diff = x / Math.ceil(sliderOffsetWith) * (max - min);
    onDrag(prevValue.current + diff, state.first, state.last);
    setDragging(!state.last);
  }, {
    axis: 'x',
    pointer: {
      touch: true
    }
  });
  const renderPopoverContent = typeof props.popover === 'function' ? props.popover : props.popover ? value => value.toString() : null;
  const thumbElement = _react.default.createElement("div", {
    className: `${classPrefix}-thumb`
  }, icon ? icon : _react.default.createElement(_thumbIcon.ThumbIcon, {
    className: `${classPrefix}-thumb-icon`
  }));
  return _react.default.createElement("div", Object.assign({
    className: `${classPrefix}-thumb-container`,
    style: currentPosition()
  }, bind(), {
    role: 'slider',
    "aria-label": props['aria-label'] || locale.Slider.name,
    "aria-valuemax": max,
    "aria-valuemin": min,
    "aria-valuenow": value,
    "aria-disabled": disabled
  }), renderPopoverContent ? _react.default.createElement(_popover.default, {
    content: renderPopoverContent(value),
    placement: 'top',
    visible: residentPopover || dragging,
    getContainer: null,
    mode: 'dark'
  }, thumbElement) : thumbElement);
};
var _default = Thumb;
exports.default = _default;