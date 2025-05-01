"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.convertPageToDayjs = convertPageToDayjs;
exports.convertValueToRange = convertValueToRange;
var _dayjs = _interopRequireDefault(require("dayjs"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function convertValueToRange(selectionMode, value) {
  if (selectionMode === undefined || value === null) return null;
  if (Array.isArray(value)) {
    return value;
  }
  return [value, value];
}
function convertPageToDayjs(page) {
  return (0, _dayjs.default)().year(page.year).month(page.month - 1).date(1);
}