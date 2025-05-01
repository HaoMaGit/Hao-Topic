"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useResizeEffect = useResizeEffect;
var _ahooks = require("ahooks");
function useResizeEffect(effect, targetRef) {
  const fn = (0, _ahooks.useMemoizedFn)(effect);
  (0, _ahooks.useIsomorphicLayoutEffect)(() => {
    const target = targetRef.current;
    if (!target) return;
    if (window.ResizeObserver) {
      let animationFrame;
      const observer = new ResizeObserver(() => {
        animationFrame = window.requestAnimationFrame(() => fn(target));
      });
      observer.observe(target);
      return () => {
        window.cancelAnimationFrame(animationFrame);
        observer.disconnect();
      };
    } else {
      fn(target);
    }
  }, [targetRef]);
}