"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ModalActionButton = void 0;
var _react = _interopRequireDefault(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _button = _interopRequireDefault(require("../button"));
var _nativeProps = require("../../utils/native-props");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const ModalActionButton = props => {
  const {
    action
  } = props;
  return (0, _nativeProps.withNativeProps)(props.action, _react.default.createElement(_button.default, {
    key: action.key,
    onClick: props.onAction,
    className: (0, _classnames.default)('adm-modal-button', {
      'adm-modal-button-primary': props.action.primary
    }),
    fill: props.action.primary ? 'solid' : 'none',
    size: props.action.primary ? 'large' : 'middle',
    block: true,
    color: action.danger ? 'danger' : 'primary',
    loading: 'auto',
    disabled: action.disabled
  }, action.text));
};
exports.ModalActionButton = ModalActionButton;