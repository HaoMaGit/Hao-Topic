"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.observe = observe;
exports.useMutationEffect = useMutationEffect;
var _ahooks = require("ahooks");
var _react = require("react");
function observe(element, options, callback) {
  if (element && typeof MutationObserver !== 'undefined') {
    let observer = new MutationObserver(() => {
      callback();
    });
    observer.observe(element, options);
    // Return cleanup function
    return () => {
      if (observer) {
        observer.disconnect();
        observer = null;
      }
    };
  }
  return () => {};
}
function useMutationEffect(effect, targetRef, options) {
  const fn = (0, _ahooks.useMemoizedFn)(effect);
  (0, _react.useEffect)(() => {
    const cleanup = observe(targetRef.current, options, fn);
    return cleanup;
  }, [targetRef]);
}