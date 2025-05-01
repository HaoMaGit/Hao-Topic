"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useLockScroll = useLockScroll;
var _useTouch = require("./use-touch");
var _react = require("react");
var _getScrollParent = require("./get-scroll-parent");
var _supportsPassive = require("./supports-passive");
let totalLockCount = 0;
const BODY_LOCK_CLASS = 'adm-overflow-hidden';
function getScrollableElement(el) {
  let current = el === null || el === void 0 ? void 0 : el.parentElement;
  while (current) {
    if (current.clientHeight < current.scrollHeight) {
      return current;
    }
    current = current.parentElement;
  }
  return null;
}
// 移植自vant：https://github.com/youzan/vant/blob/HEAD/src/composables/use-lock-scroll.ts
function useLockScroll(rootRef, shouldLock) {
  const touch = (0, _useTouch.useTouch)();
  const onTouchMove = event => {
    touch.move(event);
    const direction = touch.deltaY.current > 0 ? '10' : '01';
    const el = (0, _getScrollParent.getScrollParent)(event.target, rootRef.current);
    if (!el) return;
    // This has perf cost but we have to compatible with iOS 12
    if (shouldLock === 'strict') {
      const scrollableParent = getScrollableElement(event.target);
      if (scrollableParent === document.body || scrollableParent === document.documentElement) {
        event.preventDefault();
        return;
      }
    }
    const {
      scrollHeight,
      offsetHeight,
      scrollTop
    } = el;
    const {
      height
    } = el.getBoundingClientRect();
    let status = '11';
    if (scrollTop === 0) {
      status = offsetHeight >= scrollHeight ? '00' : '01';
    } else if (scrollHeight <= Math.round(height + scrollTop)) {
      status = '10';
    }
    if (status !== '11' && touch.isVertical() && !(parseInt(status, 2) & parseInt(direction, 2))) {
      if (event.cancelable && _supportsPassive.supportsPassive) {
        // https://github.com/ant-design/ant-design-mobile/issues/6282
        event.preventDefault();
      }
    }
  };
  const lock = () => {
    document.addEventListener('touchstart', touch.start);
    document.addEventListener('touchmove', onTouchMove, _supportsPassive.supportsPassive ? {
      passive: false
    } : false);
    if (!totalLockCount) {
      document.body.classList.add(BODY_LOCK_CLASS);
    }
    totalLockCount++;
  };
  const unlock = () => {
    if (totalLockCount) {
      document.removeEventListener('touchstart', touch.start);
      document.removeEventListener('touchmove', onTouchMove);
      totalLockCount--;
      if (!totalLockCount) {
        document.body.classList.remove(BODY_LOCK_CLASS);
      }
    }
  };
  (0, _react.useEffect)(() => {
    if (shouldLock) {
      lock();
      return () => {
        unlock();
      };
    }
  }, [shouldLock]);
}