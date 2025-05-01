"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useInnerVisible = useInnerVisible;
var _react = require("react");
var _ahooks = require("ahooks");
function useInnerVisible(outerVisible) {
  const [innerVisible, setInnerVisible] = (0, _react.useState)(outerVisible);
  (0, _ahooks.useIsomorphicLayoutEffect)(() => {
    setInnerVisible(outerVisible);
  }, [outerVisible]);
  return innerVisible;
}