import React, { memo } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import { useSpring, animated } from '@react-spring/web';
import { useMotionReduced } from '../../utils/reduce-and-restore-motion';
const classPrefix = 'adm-spin-loading';
const colorRecord = {
  default: 'var(--adm-color-weak)',
  primary: 'var(--adm-color-primary)',
  white: 'var(--adm-color-white)'
};
const defaultProps = {
  color: 'default'
};
const circumference = 15 * 3.14159265358979 * 2;
export const SpinLoading = memo(p => {
  var _a;
  const props = mergeProps(defaultProps, p);
  const motionReduced = useMotionReduced();
  const {
    percent
  } = useSpring({
    cancel: motionReduced,
    loop: {
      reverse: true
    },
    from: {
      percent: 80
    },
    to: {
      percent: 30
    },
    config: {
      duration: 1200
    }
  });
  return withNativeProps(props, React.createElement(animated.div, {
    className: classPrefix,
    style: {
      '--color': (_a = colorRecord[props.color]) !== null && _a !== void 0 ? _a : props.color,
      '--percent': percent
    }
  }, React.createElement("svg", {
    className: `${classPrefix}-svg`,
    viewBox: '0 0 32 32'
  }, React.createElement(animated.circle, {
    className: `${classPrefix}-fill`,
    fill: 'transparent',
    strokeWidth: '2',
    strokeDasharray: circumference,
    strokeDashoffset: percent,
    strokeLinecap: 'square',
    r: 15,
    cx: 16,
    cy: 16
  }))));
});