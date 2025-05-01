import React, { useRef, useEffect, useState } from 'react';
import { useSpring, animated, to } from '@react-spring/web';
import { useDrag } from '@use-gesture/react';
import { mergeProps } from '../../utils/with-default-props';
import { withNativeProps } from '../../utils/native-props';
const classPrefix = `adm-floating-bubble`;
const defaultProps = {
  axis: 'y',
  defaultOffset: {
    x: 0,
    y: 0
  }
};
export const FloatingBubble = p => {
  const props = mergeProps(defaultProps, p);
  const boundaryRef = useRef(null);
  const buttonRef = useRef(null);
  const [innerValue, setInnerValue] = useState(props.offset === undefined ? props.defaultOffset : props.offset);
  useEffect(() => {
    if (props.offset === undefined) return;
    api.start({
      x: props.offset.x,
      y: props.offset.y
    });
  }, [props.offset]);
  /**
   * memoize the `to` function
   * inside a component that renders frequently
   * to prevent an unintended restart
   */
  const [{
    x,
    y,
    opacity
  }, api] = useSpring(() => ({
    x: innerValue.x,
    y: innerValue.y,
    opacity: 1
  }));
  const bind = useDrag(state => {
    var _a;
    let nextX = state.offset[0];
    let nextY = state.offset[1];
    if (state.last && props.magnetic) {
      const boundary = boundaryRef.current;
      const button = buttonRef.current;
      if (!boundary || !button) return;
      const boundaryRect = boundary.getBoundingClientRect();
      const buttonRect = button.getBoundingClientRect();
      if (props.magnetic === 'x') {
        const compensation = x.goal - x.get();
        const leftDistance = buttonRect.left + compensation - boundaryRect.left;
        const rightDistance = boundaryRect.right - (buttonRect.right + compensation);
        if (rightDistance <= leftDistance) {
          nextX += rightDistance;
        } else {
          nextX -= leftDistance;
        }
      } else if (props.magnetic === 'y') {
        const compensation = y.goal - y.get();
        const topDistance = buttonRect.top + compensation - boundaryRect.top;
        const bottomDistance = boundaryRect.bottom - (buttonRect.bottom + compensation);
        if (bottomDistance <= topDistance) {
          nextY += bottomDistance;
        } else {
          nextY -= topDistance;
        }
      }
    }
    const nextOffest = {
      x: nextX,
      y: nextY
    };
    if (props.offset === undefined) {
      // Uncontrolled mode
      api.start(nextOffest);
    } else {
      setInnerValue(nextOffest);
    }
    (_a = props.onOffsetChange) === null || _a === void 0 ? void 0 : _a.call(props, nextOffest);
    // active status
    api.start({
      opacity: state.active ? 0.8 : 1
    });
  }, {
    axis: props.axis === 'xy' ? undefined : props.axis,
    pointer: {
      touch: true
    },
    // the component won't trigger drag logic if the user just clicked on the component.
    filterTaps: true,
    // set constraints to the user gesture
    bounds: boundaryRef,
    from: () => [x.get(), y.get()]
  });
  return withNativeProps(props, React.createElement("div", {
    className: classPrefix
  }, React.createElement("div", {
    className: `${classPrefix}-boundary-outer`
  }, React.createElement("div", {
    className: `${classPrefix}-boundary`,
    ref: boundaryRef
  })), React.createElement(animated.div, Object.assign({}, bind(), {
    style: {
      opacity,
      transform: to([x, y], (x, y) => `translate(${x}px, ${y}px)`)
    },
    onClick: props.onClick,
    className: `${classPrefix}-button`,
    ref: buttonRef
  }), props.children)));
};