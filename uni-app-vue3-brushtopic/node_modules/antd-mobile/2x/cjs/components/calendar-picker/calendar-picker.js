"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.CalendarPicker = void 0;
var _tslib = require("tslib");
var _classnames = _interopRequireDefault(require("classnames"));
var _react = _interopRequireWildcard(require("react"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _button = _interopRequireDefault(require("../button"));
var _calendarPickerView = _interopRequireDefault(require("../calendar-picker-view"));
var _calendarPickerView2 = require("../calendar-picker-view/calendar-picker-view");
var _configProvider = require("../config-provider");
var _divider = _interopRequireDefault(require("../divider"));
var _popup = _interopRequireDefault(require("../popup"));
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const classPrefix = 'adm-calendar-picker';
const defaultProps = {
  weekStartsOn: 'Sunday',
  defaultValue: null,
  allowClear: true,
  usePopup: true,
  selectionMode: 'single'
};
const CalendarPicker = (0, _react.forwardRef)((p, ref) => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const {
    locale
  } = (0, _configProvider.useConfig)();
  const calendarRef = ref !== null && ref !== void 0 ? ref : (0, _react.useRef)(null);
  const {
      visible,
      confirmText,
      popupClassName,
      popupStyle,
      popupBodyStyle,
      forceRender,
      closeOnMaskClick,
      onClose,
      onConfirm,
      onMaskClick,
      getContainer
    } = props,
    calendarViewProps = (0, _tslib.__rest)(props, ["visible", "confirmText", "popupClassName", "popupStyle", "popupBodyStyle", "forceRender", "closeOnMaskClick", "onClose", "onConfirm", "onMaskClick", "getContainer"]);
  const viewContext = _react.default.useMemo(() => ({
    visible: !!visible
  }), [visible]);
  const footer = _react.default.createElement("div", {
    className: `${classPrefix}-footer`
  }, _react.default.createElement(_divider.default, null), _react.default.createElement("div", {
    className: `${classPrefix}-footer-bottom`
  }, _react.default.createElement(_button.default, {
    color: 'primary',
    onClick: () => {
      var _a, _b, _c, _d;
      const dateRange = (_b = (_a = calendarRef.current) === null || _a === void 0 ? void 0 : _a.getDateRange()) !== null && _b !== void 0 ? _b : null;
      if (props.selectionMode === 'single') {
        (_c = props.onConfirm) === null || _c === void 0 ? void 0 : _c.call(props, dateRange ? dateRange[0] : null);
      } else if (props.selectionMode === 'range') {
        (_d = props.onConfirm) === null || _d === void 0 ? void 0 : _d.call(props, dateRange);
      }
      onClose === null || onClose === void 0 ? void 0 : onClose();
    }
  }, confirmText !== null && confirmText !== void 0 ? confirmText : locale.Calendar.confirm)));
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: classPrefix
  }, _react.default.createElement(_popup.default, {
    visible: visible,
    className: (0, _classnames.default)(`${classPrefix}-popup`, popupClassName),
    showCloseButton: true,
    forceRender: ref ? true : forceRender,
    style: popupStyle,
    bodyStyle: Object.assign({
      borderTopLeftRadius: '8px',
      borderTopRightRadius: '8px',
      minHeight: '80vh',
      overflow: 'auto'
    }, popupBodyStyle),
    onClose: onClose,
    onMaskClick: () => {
      onMaskClick === null || onMaskClick === void 0 ? void 0 : onMaskClick();
      if (closeOnMaskClick) {
        onClose === null || onClose === void 0 ? void 0 : onClose();
      }
    },
    getContainer: getContainer
  }, _react.default.createElement(_calendarPickerView2.Context.Provider, {
    value: viewContext
  }, _react.default.createElement(_calendarPickerView.default, Object.assign({
    ref: calendarRef
  }, calendarViewProps))), footer)));
});
exports.CalendarPicker = CalendarPicker;