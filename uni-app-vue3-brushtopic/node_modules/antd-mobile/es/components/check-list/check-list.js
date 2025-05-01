import { CheckOutline } from 'antd-mobile-icons';
import React from 'react';
import { withNativeProps } from '../../utils/native-props';
import { usePropsValue } from '../../utils/use-props-value';
import { mergeProps } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
import List from '../list';
import { CheckListContext } from './context';
const classPrefix = 'adm-check-list';
const defaultProps = {
  multiple: false,
  defaultValue: [],
  activeIcon: React.createElement(CheckOutline, null)
};
export const CheckList = props => {
  const {
    checkList: componentConfig = {}
  } = useConfig();
  const mergedProps = mergeProps(defaultProps, componentConfig, props);
  const [value, setValue] = usePropsValue(mergedProps);
  function check(val) {
    if (mergedProps.multiple) {
      setValue([...value, val]);
    } else {
      setValue([val]);
    }
  }
  function uncheck(val) {
    setValue(value.filter(item => item !== val));
  }
  const {
    activeIcon,
    extra,
    disabled,
    readOnly
  } = mergedProps;
  return React.createElement(CheckListContext.Provider, {
    value: {
      value,
      check,
      uncheck,
      activeIcon,
      extra,
      disabled,
      readOnly
    }
  }, withNativeProps(mergedProps, React.createElement(List, {
    mode: mergedProps.mode,
    className: classPrefix
  }, mergedProps.children)));
};