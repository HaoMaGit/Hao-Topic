"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Rate = void 0;
var _react = _interopRequireWildcard(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _usePropsValue = require("../../utils/use-props-value");
var _star = require("./star");
var _react2 = require("@use-gesture/react");
var _bound = require("../../utils/bound");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const classPrefix = `adm-rate`;
const defaultProps = {
  count: 5,
  allowHalf: false,
  character: _react.default.createElement(_star.Star, null),
  defaultValue: 0,
  readOnly: false,
  allowClear: true
};
const Rate = p => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const [value, setValue] = (0, _usePropsValue.usePropsValue)(props);
  const containerRef = (0, _react.useRef)(null);
  const starList = Array(props.count).fill(null);
  function renderStar(v, half) {
    return _react.default.createElement("div", {
      className: (0, _classnames.default)(`${classPrefix}-star`, {
        [`${classPrefix}-star-active`]: value >= v,
        [`${classPrefix}-star-half`]: half,
        [`${classPrefix}-star-readonly`]: props.readOnly
      }),
      role: 'radio',
      "aria-checked": value >= v,
      "aria-label": '' + v
    }, props.character);
  }
  const bind = (0, _react2.useDrag)(state => {
    if (props.readOnly) return;
    const {
      xy: [clientX],
      tap
    } = state;
    const container = containerRef.current;
    if (!container) return;
    const rect = container.getBoundingClientRect();
    const rawValue = (clientX - rect.left) / rect.width * props.count;
    const ceiledValue = props.allowHalf ? Math.ceil(rawValue * 2) / 2 : Math.ceil(rawValue);
    const boundValue = (0, _bound.bound)(ceiledValue, 0, props.count);
    if (tap) {
      if (props.allowClear && boundValue === value) {
        setValue(0);
        return;
      }
    }
    setValue(boundValue);
  }, {
    axis: 'x',
    pointer: {
      touch: true
    },
    filterTaps: true
  });
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", Object.assign({
    className: (0, _classnames.default)(classPrefix, {
      [`${classPrefix}-half`]: props.allowHalf
    }),
    role: 'radiogroup',
    "aria-readonly": props.readOnly,
    ref: containerRef
  }, bind()), starList.map((_, i) => _react.default.createElement("div", {
    key: i,
    className: (0, _classnames.default)(`${classPrefix}-box`)
  }, props.allowHalf && renderStar(i + 0.5, true), renderStar(i + 1, false)))));
};
exports.Rate = Rate;