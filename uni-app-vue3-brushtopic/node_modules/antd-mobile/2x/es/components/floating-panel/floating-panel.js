import { animated, useSpring } from '@react-spring/web';
import { useDrag } from '@use-gesture/react';
import { useMemoizedFn } from 'ahooks';
import classNames from 'classnames';
import React, { forwardRef, useImperativeHandle, useRef, useState } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { nearest } from '../../utils/nearest';
import { supportsPassive } from '../../utils/supports-passive';
import { useLockScroll } from '../../utils/use-lock-scroll';
import { mergeProps } from '../../utils/with-default-props';
const classPrefix = 'adm-floating-panel';
const defaultProps = {
  handleDraggingOfContent: true
};
export const FloatingPanel = forwardRef((p, ref) => {
  var _a, _b;
  const props = mergeProps(defaultProps, p);
  const {
    anchors,
    placement = 'bottom'
  } = props;
  const maxHeight = (_a = anchors[anchors.length - 1]) !== null && _a !== void 0 ? _a : window.innerHeight;
  const isBottomPlacement = placement !== 'top';
  const possibles = isBottomPlacement ? anchors.map(x => -x) : anchors;
  const elementRef = useRef(null);
  const headerRef = useRef(null);
  const contentRef = useRef(null);
  const [pulling, setPulling] = useState(false);
  const pullingRef = useRef(false);
  const bounds = {
    top: Math.min(...possibles),
    bottom: Math.max(...possibles)
  };
  const onHeightChange = useMemoizedFn((_b = props.onHeightChange) !== null && _b !== void 0 ? _b : () => {});
  const [{
    y
  }, api] = useSpring(() => ({
    y: isBottomPlacement ? bounds.bottom : bounds.top,
    config: {
      tension: 300
    },
    onChange: result => {
      onHeightChange(-result.value.y, y.isAnimating);
    }
  }));
  useDrag(state => {
    const [, offsetY] = state.offset;
    if (state.first) {
      const target = state.event.target;
      const header = headerRef.current;
      if (header === target || (header === null || header === void 0 ? void 0 : header.contains(target))) {
        pullingRef.current = true;
      } else {
        if (!props.handleDraggingOfContent) return;
        const reachedTop = y.goal <= bounds.top;
        const content = contentRef.current;
        if (!content) return;
        if (reachedTop) {
          if (content.scrollTop <= 0 && state.direction[1] > 0) {
            pullingRef.current = true;
          }
        } else {
          pullingRef.current = true;
        }
      }
    }
    setPulling(pullingRef.current);
    if (!pullingRef.current) return;
    const {
      event
    } = state;
    if (event.cancelable && supportsPassive) {
      event.preventDefault();
    }
    event.stopPropagation();
    let nextY = offsetY;
    if (state.last) {
      pullingRef.current = false;
      setPulling(false);
      nextY = nearest(possibles, offsetY);
    }
    api.start({
      y: nextY
    });
  }, {
    axis: 'y',
    bounds,
    rubberband: true,
    from: () => [0, y.get()],
    pointer: {
      touch: true
    },
    target: elementRef,
    eventOptions: supportsPassive ? {
      passive: false
    } : undefined
  });
  useImperativeHandle(ref, () => ({
    setHeight: (height, options) => {
      api.start({
        y: -height,
        immediate: options === null || options === void 0 ? void 0 : options.immediate
      });
    }
  }), [api]);
  useLockScroll(elementRef, true);
  const HeaderNode = React.createElement("div", {
    className: `${classPrefix}-header`,
    ref: headerRef
  }, React.createElement("div", {
    className: `${classPrefix}-bar`
  }));
  return withNativeProps(props, React.createElement(animated.div, {
    ref: elementRef,
    className: classNames(classPrefix, `${classPrefix}-${placement}`),
    style: {
      height: Math.round(maxHeight),
      translateY: y.to(y => {
        if (isBottomPlacement) {
          return `calc(100% + (${Math.round(y)}px))`;
        }
        if (placement === 'top') {
          return `calc(-100% + (${Math.round(y)}px))`;
        }
        return y;
      })
    }
  }, React.createElement("div", {
    className: `${classPrefix}-mask`,
    style: {
      display: pulling ? 'block' : 'none'
    }
  }), isBottomPlacement && HeaderNode, React.createElement("div", {
    className: `${classPrefix}-content`,
    ref: contentRef
  }, props.children), placement === 'top' && HeaderNode));
});