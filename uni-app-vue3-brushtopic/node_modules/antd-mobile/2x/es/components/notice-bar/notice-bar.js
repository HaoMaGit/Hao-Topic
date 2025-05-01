import { useTimeout } from 'ahooks';
import { CloseOutline, SoundOutline } from 'antd-mobile-icons';
import classNames from 'classnames';
import React, { memo, useRef, useState } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { useMutationEffect } from '../../utils/use-mutation-effect';
import { useResizeEffect } from '../../utils/use-resize-effect';
import { mergeProp, mergeProps } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
const classPrefix = `adm-notice-bar`;
const defaultProps = {
  color: 'default',
  delay: 2000,
  speed: 50,
  icon: React.createElement(SoundOutline, null),
  wrap: false,
  shape: 'rectangular',
  bordered: 'block'
};
export const NoticeBar = memo(props => {
  const {
    noticeBar: componentConfig = {}
  } = useConfig();
  const mergedProps = mergeProps(defaultProps, componentConfig, props);
  const closeIcon = mergeProp(React.createElement(CloseOutline, {
    className: `${classPrefix}-close-icon`
  }), componentConfig.closeIcon, props.closeIcon);
  const containerRef = useRef(null);
  const textRef = useRef(null);
  const [visible, setVisible] = useState(true);
  const speed = mergedProps.speed;
  const delayLockRef = useRef(true);
  const animatingRef = useRef(false);
  function start() {
    if (delayLockRef.current || mergedProps.wrap) return;
    const container = containerRef.current;
    const text = textRef.current;
    if (!container || !text) return;
    if (container.offsetWidth >= text.offsetWidth) {
      animatingRef.current = false;
      text.style.removeProperty('transition-duration');
      text.style.removeProperty('transform');
      return;
    }
    if (animatingRef.current) return;
    const initial = !text.style.transform;
    text.style.transitionDuration = '0s';
    if (initial) {
      text.style.transform = 'translateX(0)';
    } else {
      text.style.transform = `translateX(${container.offsetWidth}px)`;
    }
    const distance = initial ? text.offsetWidth : container.offsetWidth + text.offsetWidth;
    animatingRef.current = true;
    text.style.transitionDuration = `${Math.round(distance / speed)}s`;
    text.style.transform = `translateX(-${text.offsetWidth}px)`;
  }
  useTimeout(() => {
    delayLockRef.current = false;
    start();
  }, mergedProps.delay);
  useResizeEffect(() => {
    start();
  }, containerRef);
  useMutationEffect(() => {
    start();
  }, textRef, {
    subtree: true,
    childList: true,
    characterData: true
  });
  if (!visible) return null;
  return withNativeProps(mergedProps, React.createElement("div", {
    className: classNames(classPrefix, `${classPrefix}-${mergedProps.color}`, `${classPrefix}-${mergedProps.shape}`, {
      [`${classPrefix}-wrap`]: mergedProps.wrap,
      [`${classPrefix}-bordered`]: mergedProps.bordered === true,
      [`${classPrefix}-without-border`]: mergedProps.bordered === false
    }),
    onClick: mergedProps.onClick
  }, mergedProps.icon && React.createElement("span", {
    className: `${classPrefix}-left`
  }, mergedProps.icon), React.createElement("span", {
    ref: containerRef,
    className: `${classPrefix}-content`
  }, React.createElement("span", {
    onTransitionEnd: () => {
      animatingRef.current = false;
      start();
    },
    ref: textRef,
    className: `${classPrefix}-content-inner`
  }, mergedProps.content)), (mergedProps.closeable || mergedProps.extra) && React.createElement("span", {
    className: `${classPrefix}-right`
  }, mergedProps.extra, mergedProps.closeable && React.createElement("div", {
    className: `${classPrefix}-close`,
    onClick: () => {
      var _a;
      setVisible(false);
      (_a = mergedProps.onClose) === null || _a === void 0 ? void 0 : _a.call(mergedProps);
    }
  }, closeIcon))));
});