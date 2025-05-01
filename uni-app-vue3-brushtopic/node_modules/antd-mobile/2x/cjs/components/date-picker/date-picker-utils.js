"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.generateDatePickerColumns = exports.convertStringArrayToDate = exports.convertDateToStringArray = void 0;
var dateUtils = _interopRequireWildcard(require("./date-picker-date-utils"));
var quarterUtils = _interopRequireWildcard(require("./date-picker-quarter-utils"));
var weekUtils = _interopRequireWildcard(require("./date-picker-week-utils"));
var _util = require("./util");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const precisionLengthRecord = {
  year: 1,
  month: 2,
  day: 3,
  hour: 4,
  minute: 5,
  second: 6
};
const convertDateToStringArray = (date, precision) => {
  if (precision.includes('week')) {
    return weekUtils.convertDateToStringArray(date);
  } else if (precision.includes('quarter')) {
    return quarterUtils.convertDateToStringArray(date);
  } else {
    const datePrecision = precision;
    const stringArray = dateUtils.convertDateToStringArray(date);
    return stringArray.slice(0, precisionLengthRecord[datePrecision]);
  }
};
exports.convertDateToStringArray = convertDateToStringArray;
const convertStringArrayToDate = (value, precision) => {
  // Special case for DATE_NOW
  if ((value === null || value === void 0 ? void 0 : value[0]) === _util.TILL_NOW) {
    const now = new Date();
    now.tillNow = true;
    return now;
  }
  if (precision.includes('week')) {
    return weekUtils.convertStringArrayToDate(value);
  } else if (precision.includes('quarter')) {
    return quarterUtils.convertStringArrayToDate(value);
  } else {
    return dateUtils.convertStringArrayToDate(value);
  }
};
exports.convertStringArrayToDate = convertStringArrayToDate;
const generateDatePickerColumns = (selected, min, max, precision, renderLabel, filter, tillNow) => {
  if (precision.startsWith('week')) {
    return weekUtils.generateDatePickerColumns(selected, min, max, precision, renderLabel, filter);
  } else if (precision.startsWith('quarter')) {
    return quarterUtils.generateDatePickerColumns(selected, min, max, precision, renderLabel, filter);
  } else {
    return dateUtils.generateDatePickerColumns(selected, min, max, precision, renderLabel, filter, tillNow);
  }
};
exports.generateDatePickerColumns = generateDatePickerColumns;