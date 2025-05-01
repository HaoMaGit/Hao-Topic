import { animated, useSpring } from '@react-spring/web';
import { useIsomorphicLayoutEffect, useUnmountedRef } from 'ahooks';
import classNames from 'classnames';
import React, { useRef, useState } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { renderToContainer } from '../../utils/render-to-container';
import { ShouldRender } from '../../utils/should-render';
import { useInnerVisible } from '../../utils/use-inner-visible';
import { useLockScroll } from '../../utils/use-lock-scroll';
import { mergeProps } from '../../utils/with-default-props';
import { withStopPropagation } from '../../utils/with-stop-propagation';
import { useConfig } from '../config-provider';
import Mask from '../mask';
import { defaultPopupBaseProps } from '../popup/popup-base-props';
const classPrefix = 'adm-center-popup';
const defaultProps = Object.assign(Object.assign({}, defaultPopupBaseProps), {
  getContainer: null
});
export const CenterPopup = props => {
  const {
    popup: componentConfig = {}
  } = useConfig();
  const mergedProps = mergeProps(defaultProps, componentConfig, props);
  const unmountedRef = useUnmountedRef();
  const style = useSpring({
    scale: mergedProps.visible ? 1 : 0.8,
    opacity: mergedProps.visible ? 1 : 0,
    config: {
      mass: 1.2,
      tension: 200,
      friction: 25,
      clamp: true
    },
    onRest: () => {
      var _a, _b;
      if (unmountedRef.current) return;
      setActive(mergedProps.visible);
      if (mergedProps.visible) {
        (_a = mergedProps.afterShow) === null || _a === void 0 ? void 0 : _a.call(mergedProps);
      } else {
        (_b = mergedProps.afterClose) === null || _b === void 0 ? void 0 : _b.call(mergedProps);
      }
    }
  });
  const [active, setActive] = useState(mergedProps.visible);
  useIsomorphicLayoutEffect(() => {
    if (mergedProps.visible) {
      setActive(true);
    }
  }, [mergedProps.visible]);
  const ref = useRef(null);
  useLockScroll(ref, mergedProps.disableBodyScroll && active);
  const maskVisible = useInnerVisible(active && mergedProps.visible);
  const body = React.createElement("div", {
    className: classNames(`${classPrefix}-body`, mergedProps.bodyClassName),
    style: mergedProps.bodyStyle
  }, mergedProps.children);
  const node = withStopPropagation(mergedProps.stopPropagation, withNativeProps(mergedProps, React.createElement("div", {
    className: classPrefix,
    style: {
      display: active ? undefined : 'none',
      pointerEvents: active ? undefined : 'none'
    }
  }, mergedProps.mask && React.createElement(Mask, {
    visible: maskVisible,
    forceRender: mergedProps.forceRender,
    destroyOnClose: mergedProps.destroyOnClose,
    onMaskClick: e => {
      var _a, _b;
      (_a = mergedProps.onMaskClick) === null || _a === void 0 ? void 0 : _a.call(mergedProps, e);
      if (mergedProps.closeOnMaskClick) {
        (_b = mergedProps.onClose) === null || _b === void 0 ? void 0 : _b.call(mergedProps);
      }
    },
    style: mergedProps.maskStyle,
    className: classNames(`${classPrefix}-mask`, mergedProps.maskClassName),
    disableBodyScroll: false,
    stopPropagation: mergedProps.stopPropagation
  }), React.createElement("div", {
    className: `${classPrefix}-wrap`,
    role: mergedProps.role,
    "aria-label": mergedProps['aria-label']
  }, React.createElement(animated.div, {
    style: Object.assign(Object.assign({}, style), {
      pointerEvents: style.opacity.to(v => v === 1 ? 'unset' : 'none')
    }),
    ref: ref
  }, mergedProps.showCloseButton && React.createElement("a", {
    className: classNames(`${classPrefix}-close`, 'adm-plain-anchor'),
    onClick: () => {
      var _a;
      (_a = mergedProps.onClose) === null || _a === void 0 ? void 0 : _a.call(mergedProps);
    }
  }, mergedProps.closeIcon), body)))));
  return React.createElement(ShouldRender, {
    active: active,
    forceRender: mergedProps.forceRender,
    destroyOnClose: mergedProps.destroyOnClose
  }, renderToContainer(mergedProps.getContainer, node));
};