import { animated, useSpring } from '@react-spring/web';
import { useMount } from 'ahooks';
import { DownOutline } from 'antd-mobile-icons';
import classNames from 'classnames';
import React, { isValidElement, useRef } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { useShouldRender } from '../../utils/should-render';
import { traverseReactNode } from '../../utils/traverse-react-node';
import { useIsomorphicUpdateLayoutEffect } from '../../utils/use-isomorphic-update-layout-effect';
import { observe } from '../../utils/use-mutation-effect';
import { usePropsValue } from '../../utils/use-props-value';
import { mergeProp, mergeProps } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
import List from '../list';
const classPrefix = `adm-collapse`;
export const CollapsePanel = () => {
  return null;
};
const CollapsePanelContent = props => {
  const {
    visible
  } = props;
  const innerRef = useRef(null);
  const shouldRender = useShouldRender(visible, props.forceRender, props.destroyOnClose);
  const [{
    height
  }, api] = useSpring(() => ({
    from: {
      height: 0
    },
    config: {
      precision: 0.01,
      mass: 1,
      tension: 200,
      friction: 25,
      clamp: true
    }
  }));
  useMount(() => {
    if (!visible) return;
    const inner = innerRef.current;
    if (!inner) return;
    api.start({
      height: inner.offsetHeight,
      immediate: true
    });
  });
  useIsomorphicUpdateLayoutEffect(() => {
    const inner = innerRef.current;
    if (!inner) return;
    if (visible) {
      let lastMotionId = 0;
      let cancelObserve = () => {};
      const handleMotion = () => {
        lastMotionId += 1;
        const motionId = lastMotionId;
        api.start({
          height: inner.offsetHeight
        })[0].then(() => {
          if (motionId === lastMotionId) {
            cancelObserve();
          }
        });
      };
      cancelObserve = observe(inner, {
        childList: true,
        subtree: true
      }, handleMotion);
      handleMotion();
      return cancelObserve;
    } else {
      api.start({
        height: inner.offsetHeight,
        immediate: true
      });
      api.start({
        height: 0
      });
    }
  }, [visible]);
  return React.createElement(animated.div, {
    className: classNames(`${classPrefix}-panel-content`, {
      [`${classPrefix}-panel-content-active`]: visible
    }),
    style: {
      height: height.to(v => {
        if (height.idle && visible) {
          return 'auto';
        } else {
          return v;
        }
      })
    }
  }, React.createElement("div", {
    className: `${classPrefix}-panel-content-inner`,
    ref: innerRef
  }, React.createElement(List.Item, null, shouldRender && props.children)));
};
export const Collapse = props => {
  const {
    collapse: componentConfig = {}
  } = useConfig();
  const mergedProps = mergeProps(componentConfig, props);
  const panels = [];
  traverseReactNode(mergedProps.children, child => {
    if (!isValidElement(child)) return;
    const key = child.key;
    if (typeof key !== 'string') return;
    panels.push(child);
  });
  const handlePropsValue = () => {
    var _a;
    if (!mergedProps.accordion) {
      return {
        value: mergedProps.activeKey,
        defaultValue: (_a = mergedProps.defaultActiveKey) !== null && _a !== void 0 ? _a : [],
        onChange: mergedProps.onChange
      };
    }
    const initValue = {
      value: [],
      defaultValue: [],
      onChange: v => {
        var _a, _b;
        (_a = mergedProps.onChange) === null || _a === void 0 ? void 0 : _a.call(mergedProps, (_b = v[0]) !== null && _b !== void 0 ? _b : null);
      }
    };
    if (mergedProps.activeKey === undefined) {
      initValue.value = undefined;
    } else if (mergedProps.activeKey !== null) {
      initValue.value = [mergedProps.activeKey];
    }
    if (![null, undefined].includes(mergedProps.defaultActiveKey)) {
      initValue.defaultValue = [mergedProps.defaultActiveKey];
    }
    return initValue;
  };
  const [activeKey, setActiveKey] = usePropsValue(handlePropsValue());
  const activeKeyList = activeKey === null ? [] : Array.isArray(activeKey) ? activeKey : [activeKey];
  return withNativeProps(mergedProps, React.createElement("div", {
    className: classPrefix
  }, React.createElement(List, null, panels.map(panel => {
    const key = panel.key;
    const active = activeKeyList.includes(key);
    function handleClick(event) {
      var _a, _b;
      if (mergedProps.accordion) {
        if (active) {
          setActiveKey([]);
        } else {
          setActiveKey([key]);
        }
      } else {
        if (active) {
          setActiveKey(activeKeyList.filter(v => v !== key));
        } else {
          setActiveKey([...activeKeyList, key]);
        }
      }
      (_b = (_a = panel.props).onClick) === null || _b === void 0 ? void 0 : _b.call(_a, event);
    }
    const arrow = mergeProp(React.createElement(DownOutline, null), mergedProps.arrow, mergedProps.arrowIcon, panel.props.arrow, panel.props.arrowIcon);
    const arrowIcon = typeof arrow === 'function' ? arrow(active) : React.createElement("div", {
      className: classNames(`${classPrefix}-arrow`, {
        [`${classPrefix}-arrow-active`]: active
      })
    }, arrow);
    return React.createElement(React.Fragment, {
      key: key
    }, withNativeProps(panel.props, React.createElement(List.Item, {
      className: `${classPrefix}-panel-header`,
      onClick: handleClick,
      disabled: panel.props.disabled,
      arrowIcon: arrowIcon
    }, panel.props.title)), React.createElement(CollapsePanelContent, {
      visible: active,
      forceRender: !!panel.props.forceRender,
      destroyOnClose: !!panel.props.destroyOnClose
    }, panel.props.children));
  }))));
};