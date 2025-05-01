"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.CascadePicker = void 0;
var _tslib = require("tslib");
var _react = _interopRequireWildcard(require("react"));
var _picker = _interopRequireDefault(require("../picker"));
var _cascadePickerUtils = require("./cascade-picker-utils");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const CascadePicker = (0, _react.forwardRef)((props, ref) => {
  const {
      options
    } = props,
    pickerProps = (0, _tslib.__rest)(props, ["options"]);
  const columnsFn = (0, _cascadePickerUtils.useColumnsFn)(options);
  return _react.default.createElement(_picker.default, Object.assign({}, pickerProps, {
    ref: ref,
    columns: columnsFn
  }));
});
exports.CascadePicker = CascadePicker;