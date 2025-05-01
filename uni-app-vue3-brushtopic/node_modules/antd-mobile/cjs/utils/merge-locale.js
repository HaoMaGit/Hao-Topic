"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.mergeLocale = mergeLocale;
function mergeLocale(base, patch) {
  function merge(a, b) {
    if (typeof a !== 'object' || typeof b !== 'object' || Array.isArray(a) || Array.isArray(b)) {
      return b !== undefined ? b : a;
    }
    const result = {};
    for (const key in a) {
      if (a.hasOwnProperty(key)) {
        result[key] = merge(a[key], b[key]);
      }
    }
    return result;
  }
  return merge(base, patch);
}