import React, { useState, useEffect, useMemo } from 'react';
import classNames from 'classnames';
import Tabs from '../tabs';
import CheckList from '../check-list';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import { usePropsValue } from '../../utils/use-props-value';
import { useCascaderValueExtend } from './use-cascader-value-extend';
import { useConfig } from '../config-provider';
import { optionSkeleton } from './option-skeleton';
import Skeleton from '../skeleton';
import { useUpdateEffect } from 'ahooks';
import { useFieldNames } from '../../hooks';
const classPrefix = `adm-cascader-view`;
const defaultProps = {
  defaultValue: []
};
export const CascaderView = p => {
  const props = mergeProps(defaultProps, p);
  const {
    locale
  } = useConfig();
  const [labelName, valueName, childrenName, disabledName] = useFieldNames(props.fieldNames);
  const generateValueExtend = useCascaderValueExtend(props.options, {
    valueName,
    childrenName
  });
  const [value, setValue] = usePropsValue(Object.assign(Object.assign({}, props), {
    onChange: val => {
      var _a;
      (_a = props.onChange) === null || _a === void 0 ? void 0 : _a.call(props, val, generateValueExtend(val));
    }
  }));
  const [tabActiveIndex, setTabActiveIndex] = useState(0);
  const levels = useMemo(() => {
    const ret = [];
    let currentOptions = props.options;
    let reachedEnd = false;
    for (const v of value) {
      const target = currentOptions.find(option => option[valueName] === v);
      ret.push({
        selected: target,
        options: currentOptions
      });
      if (!target || !target[childrenName]) {
        reachedEnd = true;
        break;
      }
      currentOptions = target[childrenName];
    }
    if (!reachedEnd) {
      ret.push({
        selected: undefined,
        options: currentOptions
      });
    }
    return ret;
  }, [value, props.options]);
  useUpdateEffect(() => {
    var _a;
    (_a = props.onTabsChange) === null || _a === void 0 ? void 0 : _a.call(props, tabActiveIndex);
  }, [tabActiveIndex]);
  useEffect(() => {
    setTabActiveIndex(levels.length - 1);
  }, [value]);
  useEffect(() => {
    const max = levels.length - 1;
    if (tabActiveIndex > max) {
      setTabActiveIndex(max);
    }
  }, [tabActiveIndex, levels]);
  const onItemSelect = (selectValue, depth) => {
    const next = value.slice(0, depth);
    if (selectValue !== undefined) {
      next[depth] = selectValue;
    }
    setValue(next);
  };
  const whetherLoading = options => props.loading || options === optionSkeleton;
  const placeholder = props.placeholder || locale.Cascader.placeholder;
  return withNativeProps(props, React.createElement("div", {
    className: classPrefix
  }, React.createElement(Tabs, {
    activeKey: tabActiveIndex.toString(),
    onChange: key => {
      const activeIndex = parseInt(key);
      setTabActiveIndex(activeIndex);
    },
    stretch: false,
    className: `${classPrefix}-tabs`
  }, levels.map((level, index) => {
    const selected = level.selected;
    return React.createElement(Tabs.Tab, {
      key: index.toString(),
      title: React.createElement("div", {
        className: `${classPrefix}-header-title`
      }, selected ? selected[labelName] : typeof placeholder === 'function' ? placeholder(index) : placeholder),
      forceRender: true
    }, React.createElement("div", {
      className: `${classPrefix}-content`
    }, whetherLoading(level.options) ? React.createElement("div", {
      className: `${classPrefix}-skeleton`
    }, React.createElement(Skeleton, {
      className: `${classPrefix}-skeleton-line-1`,
      animated: true
    }), React.createElement(Skeleton, {
      className: `${classPrefix}-skeleton-line-2`,
      animated: true
    }), React.createElement(Skeleton, {
      className: `${classPrefix}-skeleton-line-3`,
      animated: true
    }), React.createElement(Skeleton, {
      className: `${classPrefix}-skeleton-line-4`,
      animated: true
    })) : React.createElement(CheckList, {
      value: [value[index]],
      onChange: selectValue => onItemSelect(selectValue[0], index),
      activeIcon: props.activeIcon
    }, level.options.map(option => {
      const active = value[index] === option[valueName];
      return React.createElement(CheckList.Item, {
        value: option[valueName],
        key: option[valueName],
        disabled: option[disabledName],
        className: classNames(`${classPrefix}-item`, {
          [`${classPrefix}-item-active`]: active
        })
      }, option[labelName]);
    }))));
  }))));
};