"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useColumnsFn = useColumnsFn;
var _react = require("react");
function useColumnsFn(options) {
  const depth = (0, _react.useMemo)(() => {
    let depth = 0;
    function traverse(options, currentDepth) {
      if (currentDepth > depth) depth = currentDepth;
      const nextDepth = currentDepth + 1;
      options.forEach(option => {
        if (option.children) {
          traverse(option.children, nextDepth);
        }
      });
    }
    traverse(options, 1);
    return depth;
  }, [options]);
  return selected => {
    const columns = [];
    let currentOptions = options;
    let i = 0;
    while (true) {
      columns.push(currentOptions.map(option => ({
        label: option.label,
        value: option.value
      })));
      const x = selected[i];
      const targetOptions = currentOptions.find(option => option.value === x);
      if (!targetOptions || !targetOptions.children) break;
      currentOptions = targetOptions.children;
      i++;
    }
    while (i < depth - 1) {
      columns.push([]);
      i++;
    }
    return columns;
  };
}