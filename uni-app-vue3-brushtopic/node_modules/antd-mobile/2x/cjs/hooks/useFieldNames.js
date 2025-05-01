"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useFieldNames = void 0;
var _react = require("react");
const useFieldNames = (fieldNames = {}) => {
  const fields = (0, _react.useMemo)(() => {
    const {
      label = 'label',
      value = 'value',
      disabled = 'disabled',
      children = 'children'
    } = fieldNames;
    return [label, value, children, disabled];
  }, [JSON.stringify(fieldNames)]);
  return fields;
};
exports.useFieldNames = useFieldNames;