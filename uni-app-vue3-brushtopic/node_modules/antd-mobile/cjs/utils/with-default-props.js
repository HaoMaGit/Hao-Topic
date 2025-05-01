"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.mergeProp = mergeProp;
exports.mergeProps = mergeProps;
function mergeProps(...items) {
  const ret = {};
  items.forEach(item => {
    if (item) {
      Object.keys(item).forEach(key => {
        if (item[key] !== undefined) {
          ret[key] = item[key];
        }
      });
    }
  });
  return ret;
}
/**
 * Merge props and return the first non-undefined value.
 * The later has higher priority. e.g. (10, 1, 5) => 5 wins.
 * This is useful with legacy props that have been deprecated.
 */
function mergeProp(defaultProp, ...propList) {
  for (let i = propList.length - 1; i >= 0; i -= 1) {
    if (propList[i] !== undefined) {
      return propList[i];
    }
  }
  return defaultProp;
}