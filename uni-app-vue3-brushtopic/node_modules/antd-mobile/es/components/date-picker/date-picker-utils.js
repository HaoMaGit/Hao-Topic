import * as dateUtils from './date-picker-date-utils';
import * as quarterUtils from './date-picker-quarter-utils';
import * as weekUtils from './date-picker-week-utils';
import { TILL_NOW } from './util';
const precisionLengthRecord = {
  year: 1,
  month: 2,
  day: 3,
  hour: 4,
  minute: 5,
  second: 6
};
export const convertDateToStringArray = (date, precision) => {
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
export const convertStringArrayToDate = (value, precision) => {
  // Special case for DATE_NOW
  if ((value === null || value === void 0 ? void 0 : value[0]) === TILL_NOW) {
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
export const generateDatePickerColumns = (selected, min, max, precision, renderLabel, filter, tillNow) => {
  if (precision.startsWith('week')) {
    return weekUtils.generateDatePickerColumns(selected, min, max, precision, renderLabel, filter);
  } else if (precision.startsWith('quarter')) {
    return quarterUtils.generateDatePickerColumns(selected, min, max, precision, renderLabel, filter);
  } else {
    return dateUtils.generateDatePickerColumns(selected, min, max, precision, renderLabel, filter, tillNow);
  }
};