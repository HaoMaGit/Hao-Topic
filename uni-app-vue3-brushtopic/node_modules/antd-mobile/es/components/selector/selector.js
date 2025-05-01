import classNames from 'classnames';
import React from 'react';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import Space from '../space';
import Grid from '../grid';
import { usePropsValue } from '../../utils/use-props-value';
import { CheckMark } from './check-mark';
import { useConfig } from '../config-provider';
import { useFieldNames } from '../../hooks';
const classPrefix = `adm-selector`;
const defaultProps = {
  multiple: false,
  defaultValue: [],
  showCheckMark: true
};
export const Selector = p => {
  const props = mergeProps(defaultProps, p);
  const [labelName, valueName,, disabledName] = useFieldNames(props.fieldNames);
  const [value, setValue] = usePropsValue({
    value: props.value,
    defaultValue: props.defaultValue,
    onChange: val => {
      var _a;
      const extend = {
        get items() {
          return props.options.filter(option => val.includes(option[valueName]));
        }
      };
      (_a = props.onChange) === null || _a === void 0 ? void 0 : _a.call(props, val, extend);
    }
  });
  const {
    locale
  } = useConfig();
  const items = props.options.map(option => {
    const active = (value || []).includes(option[valueName]);
    const disabled = option[disabledName] || props.disabled;
    const itemCls = classNames(`${classPrefix}-item`, {
      [`${classPrefix}-item-active`]: active && !props.multiple,
      [`${classPrefix}-item-multiple-active`]: active && props.multiple,
      [`${classPrefix}-item-disabled`]: disabled
    });
    return React.createElement("div", {
      key: option[valueName],
      className: itemCls,
      onClick: () => {
        if (disabled) {
          return;
        }
        if (props.multiple) {
          const val = active ? value.filter(v => v !== option[valueName]) : [...value, option[valueName]];
          setValue(val);
        } else {
          const val = active ? [] : [option[valueName]];
          setValue(val);
        }
      },
      role: 'option',
      "aria-selected": active && !props.multiple || active && props.multiple
    }, option[labelName], option.description && React.createElement("div", {
      className: `${classPrefix}-item-description`
    }, option.description), active && props.showCheckMark && React.createElement("div", {
      className: `${classPrefix}-check-mark-wrapper`
    }, React.createElement(CheckMark, null)));
  });
  return withNativeProps(props, React.createElement("div", {
    className: classPrefix,
    role: 'listbox',
    "aria-label": locale.Selector.name
  }, props.columns ? React.createElement(Grid, {
    columns: props.columns
  }, items) : React.createElement(Space, {
    wrap: true
  }, items)));
};