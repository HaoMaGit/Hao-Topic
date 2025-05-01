"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.isNodeWithContent = isNodeWithContent;
/**
 * Check if the `node` is visible Node (not null, undefined, or false)
 */
function isNodeWithContent(node) {
  return node !== undefined && node !== null && node !== false;
}