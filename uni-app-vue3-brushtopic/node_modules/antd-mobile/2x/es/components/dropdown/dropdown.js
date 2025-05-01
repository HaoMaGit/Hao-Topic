import { useClickAway } from 'ahooks';
import classNames from 'classnames';
import React, { cloneElement, forwardRef, isValidElement, useEffect, useImperativeHandle, useRef, useState } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { usePropsValue } from '../../utils/use-props-value';
import { mergeProp, mergeProps } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
import Popup from '../popup';
import { defaultPopupBaseProps } from '../popup/popup-base-props';
import { IconContext } from './context';
import { ItemChildrenWrap } from './item';
const classPrefix = `adm-dropdown`;
const defaultProps = {
  defaultActiveKey: null,
  closeOnMaskClick: true,
  closeOnClickAway: false,
  getContainer: defaultPopupBaseProps['getContainer']
};
const Dropdown = forwardRef((props, ref) => {
  const {
    dropdown: componentConfig = {}
  } = useConfig();
  const mergedProps = mergeProps(defaultProps, componentConfig, props);
  const arrowIcon = mergeProp(componentConfig.arrowIcon, props.arrow, props.arrowIcon);
  const [value, setValue] = usePropsValue({
    value: mergedProps.activeKey,
    defaultValue: mergedProps.defaultActiveKey,
    onChange: mergedProps.onChange
  });
  const navRef = useRef(null);
  const contentRef = useRef(null);
  // 点击外部区域，关闭
  useClickAway(() => {
    if (!mergedProps.closeOnClickAway) return;
    setValue(null);
  }, [navRef, contentRef]);
  // 计算 navs 的 top 值
  const [top, setTop] = useState();
  const containerRef = useRef(null);
  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;
    if (value) {
      const rect = container.getBoundingClientRect();
      setTop(rect.bottom);
    }
  }, [value]);
  const changeActive = key => {
    if (value === key) {
      setValue(null);
    } else {
      setValue(key);
    }
  };
  let popupForceRender = false;
  const items = [];
  const navs = React.Children.map(mergedProps.children, child => {
    if (isValidElement(child)) {
      const childProps = Object.assign(Object.assign({}, child.props), {
        onClick: event => {
          var _a, _b;
          changeActive(child.key);
          (_b = (_a = child.props).onClick) === null || _b === void 0 ? void 0 : _b.call(_a, event);
        },
        active: child.key === value
      });
      items.push(child);
      if (child.props.forceRender) popupForceRender = true;
      return cloneElement(child, childProps);
    } else {
      return child;
    }
  });
  useImperativeHandle(ref, () => ({
    close: () => {
      setValue(null);
    }
  }), [setValue]);
  return withNativeProps(mergedProps, React.createElement("div", {
    className: classNames(classPrefix, {
      [`${classPrefix}-open`]: !!value
    }),
    ref: containerRef
  }, React.createElement(IconContext.Provider, {
    value: arrowIcon
  }, React.createElement("div", {
    className: `${classPrefix}-nav`,
    ref: navRef
  }, navs)), React.createElement(Popup, {
    visible: !!value,
    position: 'top',
    getContainer: mergedProps.getContainer,
    className: `${classPrefix}-popup`,
    maskClassName: `${classPrefix}-popup-mask`,
    bodyClassName: `${classPrefix}-popup-body`,
    style: {
      top
    },
    forceRender: popupForceRender,
    onMaskClick: mergedProps.closeOnMaskClick ? () => {
      changeActive(null);
    } : undefined
  }, React.createElement("div", {
    ref: contentRef
  }, items.map(item => {
    const isActive = item.key === value;
    return React.createElement(ItemChildrenWrap, {
      key: item.key,
      active: isActive,
      forceRender: item.props.forceRender,
      destroyOnClose: item.props.destroyOnClose
    }, item.props.children);
  })))));
});
export default Dropdown;