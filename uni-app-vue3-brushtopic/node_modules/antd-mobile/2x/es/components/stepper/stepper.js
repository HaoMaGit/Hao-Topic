import classNames from 'classnames';
import React, { useEffect, useState, forwardRef, useImperativeHandle } from 'react';
import { MinusOutline, AddOutline } from 'antd-mobile-icons';
import { useMergedState } from 'rc-util';
import getMiniDecimal, { toFixed } from '@rc-component/mini-decimal';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import Input from '../input';
import Button from '../button';
import { useConfig } from '../config-provider';
const classPrefix = `adm-stepper`;
const defaultProps = {
  step: 1,
  disabled: false,
  allowEmpty: false
};
export function InnerStepper(p, ref) {
  const props = mergeProps(defaultProps, p);
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
  } = useConfig();
  // ========================== Ref ==========================
  useImperativeHandle(ref, () => ({
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
    const fixedValue = digits !== undefined ? toFixed(value.toString(), '.', digits) : value;
    return fixedValue.toString();
  };
  const getValueAsType = value => stringMode ? value.toString() : value.toNumber();
  const parseValue = text => {
    if (text === '') return null;
    if (parser) {
      return String(parser(text));
    }
    const decimal = getMiniDecimal(text);
    return decimal.isInvalidate() ? null : decimal.toString();
  };
  const formatValue = value => {
    if (value === null) return '';
    return formatter ? formatter(value) : fixedValue(value);
  };
  // ======================== Value & InputValue ========================
  const [mergedValue, setMergedValue] = useMergedState(defaultValue, {
    value,
    onChange: nextValue => {
      onChange === null || onChange === void 0 ? void 0 : onChange(nextValue);
    }
  });
  const [inputValue, setInputValue] = useState(() => formatValue(mergedValue));
  // >>>>> Value
  function setValueWithCheck(nextValue) {
    if (nextValue.isNaN()) return;
    let target = nextValue;
    // Put into range
    if (min !== undefined) {
      const minDecimal = getMiniDecimal(min);
      if (target.lessEquals(minDecimal)) {
        target = minDecimal;
      }
    }
    if (max !== undefined) {
      const maxDecimal = getMiniDecimal(max);
      if (maxDecimal.lessEquals(target)) {
        target = maxDecimal;
      }
    }
    // Fix digits
    if (digits !== undefined) {
      target = getMiniDecimal(fixedValue(getValueAsType(target)));
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
      setValueWithCheck(getMiniDecimal(valueStr));
    }
  };
  // ============================== Focus ===============================
  const [focused, setFocused] = useState(false);
  const inputRef = React.useRef(null);
  function triggerFocus(nextFocus) {
    setFocused(nextFocus);
    // We will convert value to original text when focus
    if (nextFocus) {
      setInputValue(mergedValue !== null && mergedValue !== undefined ? String(mergedValue) : '');
    }
  }
  useEffect(() => {
    var _a, _b, _c;
    if (focused) {
      (_c = (_b = (_a = inputRef.current) === null || _a === void 0 ? void 0 : _a.nativeElement) === null || _b === void 0 ? void 0 : _b.select) === null || _c === void 0 ? void 0 : _c.call(_b);
    }
  }, [focused]);
  // Focus change to format value
  useEffect(() => {
    if (!focused) {
      setInputValue(formatValue(mergedValue));
    }
  }, [focused, mergedValue, digits]);
  // ============================ Operations ============================
  const handleOffset = positive => {
    let stepValue = getMiniDecimal(step);
    if (!positive) {
      stepValue = stepValue.negate();
    }
    setValueWithCheck(getMiniDecimal(mergedValue !== null && mergedValue !== void 0 ? mergedValue : 0).add(stepValue.toString()));
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
  return withNativeProps(props, React.createElement("div", {
    className: classNames(classPrefix, {
      [`${classPrefix}-active`]: focused
    })
  }, React.createElement(Button, {
    className: `${classPrefix}-minus`,
    onClick: handleMinus,
    disabled: minusDisabled(),
    fill: 'none',
    shape: 'rectangular',
    color: 'primary',
    "aria-label": locale.Stepper.decrease
  }, React.createElement(MinusOutline, null)), React.createElement("div", {
    className: `${classPrefix}-middle`
  }, React.createElement(Input, {
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
  })), React.createElement(Button, {
    className: `${classPrefix}-plus`,
    onClick: handlePlus,
    disabled: plusDisabled(),
    fill: 'none',
    shape: 'rectangular',
    color: 'primary',
    "aria-label": locale.Stepper.increase
  }, React.createElement(AddOutline, null))));
}
export const Stepper = forwardRef(InnerStepper);