"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Segmented = void 0;
var _tslib = require("tslib");
var _classnames = _interopRequireDefault(require("classnames"));
var _rcSegmented = _interopRequireDefault(require("rc-segmented"));
var React = _interopRequireWildcard(require("react"));
var _nativeProps = require("../../utils/native-props");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function isSegmentedLabeledOptionWithIcon(option) {
  return typeof option === 'object' && !!(option === null || option === void 0 ? void 0 : option.icon);
}
const classPrefix = `adm-segmented`;
const Segmented = React.forwardRef((props, ref) => {
  const {
      prefixCls: customizePrefixCls,
      className,
      block,
      options = []
    } = props,
    restProps = (0, _tslib.__rest)(props
    // syntactic sugar to support `icon` for Segmented Item
    , ["prefixCls", "className", "block", "options"]);
  // syntactic sugar to support `icon` for Segmented Item
  const extendedOptions = React.useMemo(() => options.map(option => {
    if (isSegmentedLabeledOptionWithIcon(option)) {
      const {
          icon,
          label
        } = option,
        restOption = (0, _tslib.__rest)(option, ["icon", "label"]);
      return Object.assign(Object.assign({}, restOption), {
        label: React.createElement(React.Fragment, null, React.createElement("span", {
          className: `${classPrefix}-item-icon`
        }, icon), label && React.createElement("span", null, label))
      });
    }
    return option;
  }), [options, classPrefix]);
  return (0, _nativeProps.withNativeProps)(props, React.createElement(_rcSegmented.default, Object.assign({}, restProps, {
    className: (0, _classnames.default)(className, {
      [`${classPrefix}-block`]: block
    }),
    options: extendedOptions,
    ref: ref,
    prefixCls: classPrefix
  })));
});
exports.Segmented = Segmented;
if (process.env.NODE_ENV !== 'production') {
  Segmented.displayName = 'Segmented';
}