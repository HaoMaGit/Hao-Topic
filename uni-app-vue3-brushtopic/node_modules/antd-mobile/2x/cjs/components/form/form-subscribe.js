"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Watcher = exports.FormSubscribe = void 0;
var _react = _interopRequireWildcard(require("react"));
var _rcFieldForm = require("rc-field-form");
var _ahooks = require("ahooks");
var _useIsomorphicUpdateLayoutEffect = require("../../utils/use-isomorphic-update-layout-effect");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const FormSubscribe = props => {
  const update = (0, _ahooks.useUpdate)();
  const form = (0, _react.useContext)(_rcFieldForm.FieldContext);
  const value = form.getFieldsValue(props.to);
  // Memo to avoid useless render
  const childNode = _react.default.useMemo(() => props.children(value, form), [JSON.stringify(value), props.children]);
  return _react.default.createElement(_react.default.Fragment, null, childNode, props.to.map(namePath => _react.default.createElement(Watcher, {
    key: namePath.toString(),
    form: form,
    namePath: namePath,
    onChange: update
  })));
};
exports.FormSubscribe = FormSubscribe;
const Watcher = (0, _react.memo)(props => {
  const value = (0, _rcFieldForm.useWatch)(props.namePath, props.form);
  (0, _useIsomorphicUpdateLayoutEffect.useIsomorphicUpdateLayoutEffect)(() => {
    props.onChange();
  }, [value]);
  return null;
});
exports.Watcher = Watcher;