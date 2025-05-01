"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.undefinedFallback = undefinedFallback;
function undefinedFallback(...items) {
  let i;
  for (i = 0; i < items.length; i++) {
    if (items[i] !== undefined) break;
  }
  return items[i];
}