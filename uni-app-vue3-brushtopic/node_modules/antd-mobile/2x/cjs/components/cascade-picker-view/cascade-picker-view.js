"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.CascadePickerView = void 0;
var _tslib = require("tslib");
var _react = _interopRequireDefault(require("react"));
var _pickerView = _interopRequireDefault(require("../picker-view"));
var _cascadePickerUtils = require("../cascade-picker/cascade-picker-utils");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const CascadePickerView = props => {
  const {
      options
    } = props,
    pickerProps = (0, _tslib.__rest)(props, ["options"]);
  const columnsFn = (0, _cascadePickerUtils.useColumnsFn)(options);
  return _react.default.createElement(_pickerView.default, Object.assign({}, pickerProps, {
    columns: columnsFn
  }));
};
exports.CascadePickerView = CascadePickerView;