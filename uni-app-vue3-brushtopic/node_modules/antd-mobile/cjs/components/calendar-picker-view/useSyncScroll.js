"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = useSyncScroll;
var _rcUtil = require("rc-util");
var _react = require("react");
function useSyncScroll(current, visible, bodyRef) {
  const rafRef = (0, _react.useRef)();
  const clean = () => {
    if (rafRef.current) {
      cancelAnimationFrame(rafRef.current);
    }
  };
  const scrollTo = (0, _rcUtil.useEvent)(date => {
    clean();
    rafRef.current = requestAnimationFrame(() => {
      if (bodyRef.current) {
        const yearMonth = date.format('YYYY-M');
        const target = bodyRef.current.querySelector(`[data-year-month="${yearMonth}"]`);
        if (target) {
          // Scroll to the top of view
          target.scrollIntoView({
            block: 'start',
            inline: 'nearest'
          });
        }
      }
    });
  });
  (0, _react.useEffect)(() => {
    if (visible && current) {
      scrollTo(current);
      return clean;
    }
  }, [current, visible]);
  return scrollTo;
}