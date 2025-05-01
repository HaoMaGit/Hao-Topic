import { withNativeProps } from '../../utils/native-props';
import React, { useMemo, useRef, useState } from 'react';
import { useUnmountedRef } from 'ahooks';
import { useLockScroll } from '../../utils/use-lock-scroll';
import { useSpring, animated } from '@react-spring/web';
import { renderToContainer } from '../../utils/render-to-container';
import { mergeProps } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
import { ShouldRender } from '../../utils/should-render';
import { withStopPropagation } from '../../utils/with-stop-propagation';
const classPrefix = `adm-mask`;
const opacityRecord = {
  default: 0.55,
  thin: 0.35,
  thick: 0.75
};
const colorRecord = {
  black: '0, 0, 0',
  white: '255, 255, 255'
};
const defaultProps = {
  visible: true,
  destroyOnClose: false,
  forceRender: false,
  color: 'black',
  opacity: 'default',
  disableBodyScroll: true,
  getContainer: null,
  stopPropagation: ['click']
};
export const Mask = p => {
  const props = mergeProps(defaultProps, p);
  const {
    locale
  } = useConfig();
  const ref = useRef(null);
  useLockScroll(ref, props.visible && props.disableBodyScroll);
  const background = useMemo(() => {
    var _a;
    const opacity = (_a = opacityRecord[props.opacity]) !== null && _a !== void 0 ? _a : props.opacity;
    const rgb = colorRecord[props.color];
    return rgb ? `rgba(${rgb}, ${opacity})` : props.color;
  }, [props.color, props.opacity]);
  const [active, setActive] = useState(props.visible);
  const unmountedRef = useUnmountedRef();
  const {
    opacity
  } = useSpring({
    opacity: props.visible ? 1 : 0,
    config: {
      precision: 0.01,
      mass: 1,
      tension: 250,
      friction: 30,
      clamp: true
    },
    onStart: () => {
      setActive(true);
    },
    onRest: () => {
      var _a, _b;
      if (unmountedRef.current) return;
      setActive(props.visible);
      if (props.visible) {
        (_a = props.afterShow) === null || _a === void 0 ? void 0 : _a.call(props);
      } else {
        (_b = props.afterClose) === null || _b === void 0 ? void 0 : _b.call(props);
      }
    }
  });
  const node = withStopPropagation(props.stopPropagation, withNativeProps(props, React.createElement(animated.div, {
    className: classPrefix,
    ref: ref,
    "aria-hidden": true,
    style: Object.assign(Object.assign({}, props.style), {
      background,
      opacity,
      display: active ? undefined : 'none'
    }),
    onClick: e => {
      var _a;
      if (e.target === e.currentTarget) {
        (_a = props.onMaskClick) === null || _a === void 0 ? void 0 : _a.call(props, e);
      }
    }
  }, props.onMaskClick && React.createElement("div", {
    className: `${classPrefix}-aria-button`,
    role: 'button',
    "aria-label": locale.Mask.name,
    onClick: props.onMaskClick
  }), React.createElement("div", {
    className: `${classPrefix}-content`
  }, props.children))));
  return React.createElement(ShouldRender, {
    active: active,
    forceRender: props.forceRender,
    destroyOnClose: props.destroyOnClose
  }, renderToContainer(props.getContainer, node));
};