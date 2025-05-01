import { animated, useSpring } from '@react-spring/web';
import { useDrag } from '@use-gesture/react';
import React, { forwardRef, useEffect, useImperativeHandle, useRef } from 'react';
import { withNativeProps } from '../../utils/native-props';
import { nearest } from '../../utils/nearest';
import { mergeProps } from '../../utils/with-default-props';
import { withStopPropagation } from '../../utils/with-stop-propagation';
import Button from '../button';
const classPrefix = `adm-swipe-action`;
const defaultProps = {
  rightActions: [],
  leftActions: [],
  closeOnTouchOutside: true,
  closeOnAction: true,
  stopPropagation: []
};
export const SwipeAction = forwardRef((p, ref) => {
  const props = mergeProps(defaultProps, p);
  const rootRef = useRef(null);
  const leftRef = useRef(null);
  const rightRef = useRef(null);
  function getWidth(ref) {
    const element = ref.current;
    if (!element) return 0;
    return element.offsetWidth;
  }
  function getLeftWidth() {
    return getWidth(leftRef);
  }
  function getRightWidth() {
    return getWidth(rightRef);
  }
  const [{
    x
  }, api] = useSpring(() => ({
    x: 0,
    config: {
      tension: 200,
      friction: 30
    }
  }), []);
  const draggingRef = useRef(false);
  const dragCancelRef = useRef(null);
  function forceCancelDrag() {
    var _a;
    (_a = dragCancelRef.current) === null || _a === void 0 ? void 0 : _a.call(dragCancelRef);
    draggingRef.current = false;
  }
  const bind = useDrag(state => {
    var _a;
    dragCancelRef.current = state.cancel;
    if (!state.intentional) return;
    if (state.down) {
      draggingRef.current = true;
    }
    if (!draggingRef.current) return;
    const [offsetX] = state.offset;
    if (state.last) {
      const leftWidth = getLeftWidth();
      const rightWidth = getRightWidth();
      let position = offsetX + state.velocity[0] * state.direction[0] * 50;
      if (offsetX > 0) {
        position = Math.max(0, position);
      } else if (offsetX < 0) {
        position = Math.min(0, position);
      } else {
        position = 0;
      }
      const targetX = nearest([-rightWidth, 0, leftWidth], position);
      api.start({
        x: targetX
      });
      if (targetX !== 0) {
        (_a = p.onActionsReveal) === null || _a === void 0 ? void 0 : _a.call(p, targetX > 0 ? 'left' : 'right');
      }
      window.setTimeout(() => {
        draggingRef.current = false;
      });
    } else {
      api.start({
        x: offsetX,
        immediate: true
      });
    }
  }, {
    from: () => [x.get(), 0],
    bounds: () => {
      const leftWidth = getLeftWidth();
      const rightWidth = getRightWidth();
      return {
        left: -rightWidth,
        right: leftWidth
      };
    },
    axis: 'x',
    preventScroll: true,
    pointer: {
      touch: true
    },
    triggerAllEvents: true
  });
  const close = () => {
    var _a;
    api.start({
      x: 0
    });
    forceCancelDrag();
    (_a = props.onClose) === null || _a === void 0 ? void 0 : _a.call(props);
  };
  useImperativeHandle(ref, () => ({
    show: (side = 'right') => {
      var _a;
      if (side === 'right') {
        api.start({
          x: -getRightWidth()
        });
      } else if (side === 'left') {
        api.start({
          x: getLeftWidth()
        });
      }
      (_a = p.onActionsReveal) === null || _a === void 0 ? void 0 : _a.call(p, side);
    },
    close
  }));
  useEffect(() => {
    if (!props.closeOnTouchOutside) return;
    function handle(e) {
      if (x.get() === 0) {
        return;
      }
      const root = rootRef.current;
      if (root && !root.contains(e.target)) {
        close();
      }
    }
    document.addEventListener('touchstart', handle);
    return () => {
      document.removeEventListener('touchstart', handle);
    };
  }, [props.closeOnTouchOutside]);
  function renderAction(action) {
    var _a, _b;
    const color = (_a = action.color) !== null && _a !== void 0 ? _a : 'light';
    return React.createElement(Button, {
      key: action.key,
      className: `${classPrefix}-action-button`,
      style: {
        '--background-color': (_b = colorRecord[color]) !== null && _b !== void 0 ? _b : color
      },
      onClick: e => {
        var _a, _b;
        if (props.closeOnAction) {
          close();
        }
        (_a = action.onClick) === null || _a === void 0 ? void 0 : _a.call(action, e);
        (_b = props.onAction) === null || _b === void 0 ? void 0 : _b.call(props, action, e);
      }
    }, action.text);
  }
  return withNativeProps(props, React.createElement("div", Object.assign({
    className: classPrefix
  }, bind(), {
    ref: rootRef,
    onClickCapture: e => {
      if (draggingRef.current) {
        e.stopPropagation();
        e.preventDefault();
      }
    }
  }), React.createElement(animated.div, {
    className: `${classPrefix}-track`,
    style: {
      x
    }
  }, withStopPropagation(props.stopPropagation, React.createElement("div", {
    className: `${classPrefix}-actions ${classPrefix}-actions-left`,
    ref: leftRef
  }, props.leftActions.map(renderAction))), React.createElement("div", {
    className: `${classPrefix}-content`,
    onClickCapture: e => {
      if (x.goal !== 0) {
        e.preventDefault();
        e.stopPropagation();
        close();
      }
    }
  }, React.createElement(animated.div, {
    style: {
      pointerEvents: x.to(v => v !== 0 && x.goal !== 0 ? 'none' : 'auto')
    }
  }, props.children)), withStopPropagation(props.stopPropagation, React.createElement("div", {
    className: `${classPrefix}-actions ${classPrefix}-actions-right`,
    ref: rightRef
  }, props.rightActions.map(renderAction))))));
});
const colorRecord = {
  light: 'var(--adm-color-light)',
  weak: 'var(--adm-color-weak)',
  primary: 'var(--adm-color-primary)',
  success: 'var(--adm-color-success)',
  warning: 'var(--adm-color-warning)',
  danger: 'var(--adm-color-danger)'
};