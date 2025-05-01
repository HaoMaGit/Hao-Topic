"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.InnerStepper = InnerStepper;
exports.Stepper = void 0;
var _classnames = _interopRequireDefault(require("classnames"));
var _react = _interopRequireWildcard(require("react"));
var _antdMobileIcons = require("antd-mobile-icons");
var _rcUtil = require("rc-util");
var _miniDecimal = _interopRequireWildcard(require("@rc-component/mini-decimal"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _input = _interopRequireDefault(require("../input"));
var _button = _interopRequireDefault(require("../button"));
var _configProvider = require("../config-provider");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = `adm-stepper`;
const defaultProps = {
  step: 1,
  disabled: false,
  allowEmpty: false
};
function InnerStepper(p, ref) {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const {
    defaultValue = 0,
    value,
    onChange,
    disabled,
    step,
    max,
    min,
    inputReadOnly,
    digits,
    stringMode,
    formatter,
    parser
  } = props;
  const {
    locale
  } = (0, _configProvider.useConfig)();
  // ========================== Ref ==========================
  (0, _react.useImperativeHandle)(ref, () => ({
    focus: () => {
      var _a;
      (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.focus();
    },
    blur: () => {
      var _a;
      (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.blur();
    },
    get nativeElement() {
      var _a, _b;
      return (_b = (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.nativeElement) !== null && _b !== void 0 ? _b : null;
    }
  }));
  // ========================== Parse / Format ==========================
  const fixedValue = value => {
    const fixedValue = digits !== undefined ? (0, _miniDecimal.toFixed)(value.toString(), '.', digits) : value;
    return fixedValue.toString();
  };
  const getValueAsType = value => stringMode ? value.toString() : value.toNumber();
  const parseValue = text => {
    if (text === '') return null;
    if (parser) {
      return String(parser(text));
    }
    const decimal = (0, _miniDecimal.default)(text);
    return decimal.isInvalidate() ? null : decimal.toString();
  };
  const formatValue = value => {
    if (value === null) return '';
    return formatter ? formatter(value) : fixedValue(value);
  };
  // ======================== Value & InputValue ========================
  const [mergedValue, setMergedValue] = (0, _rcUtil.useMergedState)(defaultValue, {
    value,
    onChange: nextValue => {
      onChange === null || onChange === void 0 ? void 0 : onChange(nextValue);
    }
  });
  const [inputValue, setInputValue] = (0, _react.useState)(() => formatValue(mergedValue));
  // >>>>> Value
  function setValueWithCheck(nextValue) {
    if (nextValue.isNaN()) return;
    let target = nextValue;
    // Put into range
    if (min !== undefined) {
      const minDecimal = (0, _miniDecimal.default)(min);
      if (target.lessEquals(minDecimal)) {
        target = minDecimal;
      }
    }
    if (max !== undefined) {
      const maxDecimal = (0, _miniDecimal.default)(max);
      if (maxDecimal.lessEquals(target)) {
        target = maxDecimal;
      }
    }
    // Fix digits
    if (digits !== undefined) {
      target = (0, _miniDecimal.default)(fixedValue(getValueAsType(target)));
    }
    setMergedValue(getValueAsType(target));
  }
  // >>>>> Input
  const handleInputChange = v => {
    setInputValue(v);
    const valueStr = parseValue(v);
    if (valueStr === null) {
      if (props.allowEmpty) {
        setMergedValue(null);
      } else {
        setMergedValue(defaultValue);
      }
    } else {
      setValueWithCheck((0, _miniDecimal.default)(valueStr));
    }
  };
  // ============================== Focus ===============================
  const [focused, setFocused] = (0, _react.useState)(false);
  const inputRef = _react.default.useRef(null);
  function triggerFocus(nextFocus) {
    setFocused(nextFocus);
    // We will convert value to original text when focus
    if (nextFocus) {
      setInputValue(mergedValue !== null && mergedValue !== undefined ? String(mergedValue) : '');
    }
  }
  (0, _react.useEffect)(() => {
    var _a, _b, _c;
    if (focused) {
      (_c = (_b = (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.nativeElement) === null || _b === void 0 ? void 0 : _b.select) === null || _c === void 0 ? void 0 : _c.call(_b);
    }
  }, [focused]);
  // Focus change to format value
  (0, _react.useEffect)(() => {
    if (!focused) {
      setInputValue(formatValue(mergedValue));
    }
  }, [focused, mergedValue, digits]);
  // ============================ Operations ============================
  const handleOffset = positive => {
    let stepValue = (0, _miniDecimal.default)(step);
    if (!positive) {
      stepValue = stepValue.negate();
    }
    setValueWithCheck((0, _miniDecimal.default)(mergedValue !== null && mergedValue !== void 0 ? mergedValue : 0).add(stepValue.toString()));
  };
  const handleMinus = () => {
    handleOffset(false);
  };
  const handlePlus = () => {
    handleOffset(true);
  };
  const minusDisabled = () => {
    if (disabled) return true;
    if (mergedValue === null) return false;
    if (min !== undefined) {
      return mergedValue <= min;
    }
    return false;
  };
  const plusDisabled = () => {
    if (disabled) return true;
    if (mergedValue === null) return false;
    if (max !== undefined) {
      return mergedValue >= max;
    }
    return false;
  };
  // ============================== Render ==============================
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: (0, _classnames.default)(classPrefix, {
      [`${classPrefix}-active`]: focused
    })
  }, _react.default.createElement(_button.default, {
    className: `${classPrefix}-minus`,
    onClick: handleMinus,
    disabled: minusDisabled(),
    fill: 'none',
    shape: 'rectangular',
    color: 'primary',
    "aria-label": locale.Stepper.decrease
  }, _react.default.createElement(_antdMobileIcons.MinusOutline, null)), _react.default.createElement("div", {
    className: `${classPrefix}-middle`
  }, _react.default.createElement(_input.default, {
    ref: inputRef,
    className: `${classPrefix}-input`,
    onFocus: e => {
      var _a;
      triggerFocus(true);
      (_a = props.onFocus) === null || _a === void 0 ? void 0 : _a.call(props, e);
    },
    value: inputValue,
    onChange: val => {
      disabled || handleInputChange(val);
    },
    disabled: disabled,
    onBlur: e => {
      var _a;
      triggerFocus(false);
      (_a = props.onBlur) === null || _a === void 0 ? void 0 : _a.call(props, e);
    },
    readOnly: inputReadOnly,
    role: 'spinbutton',
    "aria-valuenow": Number(inputValue),
    "aria-valuemax": Number(max),
    "aria-valuemin": Number(min),
    inputMode: 'decimal'
  })), _react.default.createElement(_button.default, {
    className: `${classPrefix}-plus`,
    onClick: handlePlus,
    disabled: plusDisabled(),
    fill: 'none',
    shape: 'rectangular',
    color: 'primary',
    "aria-label": locale.Stepper.increase
  }, _react.default.createElement(_antdMobileIcons.AddOutline, null))));
}
const Stepper = (0, _react.forwardRef)(InnerStepper);
exports.Stepper = Stepper;