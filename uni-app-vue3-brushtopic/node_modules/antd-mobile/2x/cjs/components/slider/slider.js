"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Slider = void 0;
var _react = _interopRequireWildcard(require("react"));
var _nativeProps = require("../../utils/native-props");
var _classnames = _interopRequireDefault(require("classnames"));
var _ticks = _interopRequireDefault(require("./ticks"));
var _marks = _interopRequireDefault(require("./marks"));
var _miniDecimal = _interopRequireWildcard(require("@rc-component/mini-decimal"));
var _thumb = _interopRequireDefault(require("./thumb"));
var _withDefaultProps = require("../../utils/with-default-props");
var _nearest = require("../../utils/nearest");
var _usePropsValue = require("../../utils/use-props-value");
var _devLog = require("../../utils/dev-log");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const classPrefix = `adm-slider`;
const defaultProps = {
  min: 0,
  max: 100,
  step: 1,
  ticks: false,
  range: false,
  disabled: false,
  popover: false,
  residentPopover: false
};
const Slider = p => {
  var _a;
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const {
    min,
    max,
    disabled,
    marks,
    ticks,
    step,
    icon
  } = props;
  function sortValue(val) {
    return val.sort((a, b) => a - b);
  }
  function convertValue(value) {
    return props.range ? value : [props.min, value];
  }
  function alignValue(value, decimalLen) {
    const decimal = (0, _miniDecimal.default)(value);
    const fixedStr = (0, _miniDecimal.toFixed)(decimal.toString(), '.', decimalLen);
    return (0, _miniDecimal.default)(fixedStr).toNumber();
  }
  function reverseValue(value) {
    const mergedDecimalLen = Math.max(getDecimalLen(step), getDecimalLen(value[0]), getDecimalLen(value[1]));
    return props.range ? value.map(v => alignValue(v, mergedDecimalLen)) : alignValue(value[1], mergedDecimalLen);
  }
  function getDecimalLen(n) {
    return (`${n}`.split('.')[1] || '').length;
  }
  function onAfterChange(value) {
    var _a;
    (_a = props.onAfterChange) === null || _a === void 0 ? void 0 : _a.call(props, reverseValue(value));
  }
  let propsValue = props.value;
  if (props.range && typeof props.value === 'number') {
    (0, _devLog.devWarning)('Slider', 'When `range` prop is enabled, the `value` prop should be an array, like: [0, 0]');
    propsValue = [0, props.value];
  }
  const [rawValue, setRawValue] = (0, _usePropsValue.usePropsValue)({
    value: propsValue,
    defaultValue: (_a = props.defaultValue) !== null && _a !== void 0 ? _a : props.range ? [min, min] : min,
    onChange: props.onChange
  });
  const sliderValue = sortValue(convertValue(rawValue));
  function setSliderValue(value) {
    const next = sortValue(value);
    const current = sliderValue;
    if (next[0] === current[0] && next[1] === current[1]) return;
    setRawValue(reverseValue(next));
  }
  const trackRef = (0, _react.useRef)(null);
  const fillSize = `${100 * (sliderValue[1] - sliderValue[0]) / (max - min)}%`;
  const fillStart = `${100 * (sliderValue[0] - min) / (max - min)}%`;
  // 计算要显示的点
  const pointList = (0, _react.useMemo)(() => {
    if (marks) {
      return Object.keys(marks).map(parseFloat).sort((a, b) => a - b);
    } else if (ticks) {
      const points = [];
      for (let i = (0, _miniDecimal.default)(min); i.lessEquals((0, _miniDecimal.default)(max)); i = i.add(step)) {
        points.push(i.toNumber());
      }
      return points;
    }
    return [];
  }, [marks, ticks, step, min, max]);
  function getValueByPosition(position) {
    const newPosition = position < min ? min : position > max ? max : position;
    let value = min;
    // 显示了刻度点，就只能移动到点上
    if (pointList.length) {
      value = (0, _nearest.nearest)(pointList, newPosition);
    } else {
      // 使用 MiniDecimal 避免精度问题
      const cell = Math.round((newPosition - min) / step);
      const nextVal = (0, _miniDecimal.default)(cell).multi(step);
      value = (0, _miniDecimal.default)(min).add(nextVal.toString()).toNumber();
    }
    return value;
  }
  const dragLockRef = (0, _react.useRef)(0);
  const onTrackClick = event => {
    if (dragLockRef.current > 0) return;
    event.stopPropagation();
    if (disabled) return;
    const track = trackRef.current;
    if (!track) return;
    const sliderOffsetLeft = track.getBoundingClientRect().left;
    const position = (event.clientX - sliderOffsetLeft) / Math.ceil(track.offsetWidth) * (max - min) + min;
    const targetValue = getValueByPosition(position);
    let nextSliderValue;
    if (props.range) {
      // 移动的滑块采用就近原则
      if (Math.abs(targetValue - sliderValue[0]) > Math.abs(targetValue - sliderValue[1])) {
        nextSliderValue = [sliderValue[0], targetValue];
      } else {
        nextSliderValue = [targetValue, sliderValue[1]];
      }
    } else {
      nextSliderValue = [props.min, targetValue];
    }
    setSliderValue(nextSliderValue);
    onAfterChange(nextSliderValue);
  };
  const valueBeforeDragRef = (0, _react.useRef)();
  const renderThumb = index => {
    return _react.default.createElement(_thumb.default, {
      key: index,
      value: sliderValue[index],
      min: min,
      max: max,
      disabled: disabled,
      trackRef: trackRef,
      icon: icon,
      popover: props.popover,
      residentPopover: props.residentPopover,
      onDrag: (position, first, last) => {
        if (first) {
          dragLockRef.current += 1;
          valueBeforeDragRef.current = sliderValue;
        }
        const val = getValueByPosition(position);
        const valueBeforeDrag = valueBeforeDragRef.current;
        if (!valueBeforeDrag) return;
        const next = [...valueBeforeDrag];
        next[index] = val;
        setSliderValue(next);
        if (last) {
          onAfterChange(next);
          window.setTimeout(() => {
            dragLockRef.current -= 1;
          }, 100);
        }
      },
      "aria-label": props['aria-label']
    });
  };
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: (0, _classnames.default)(classPrefix, {
      [`${classPrefix}-disabled`]: disabled
    })
  }, _react.default.createElement("div", {
    className: `${classPrefix}-track-container`,
    onClick: onTrackClick
  }, _react.default.createElement("div", {
    className: `${classPrefix}-track`,
    onClick: onTrackClick,
    ref: trackRef
  }, _react.default.createElement("div", {
    className: `${classPrefix}-fill`,
    style: {
      width: fillSize,
      left: fillStart
    }
  }), props.ticks && _react.default.createElement(_ticks.default, {
    points: pointList,
    min: min,
    max: max,
    lowerBound: sliderValue[0],
    upperBound: sliderValue[1]
  }), props.range && renderThumb(0), renderThumb(1))), marks && _react.default.createElement(_marks.default, {
    min: min,
    max: max,
    marks: marks,
    lowerBound: sliderValue[0],
    upperBound: sliderValue[1]
  })));
};
exports.Slider = Slider;