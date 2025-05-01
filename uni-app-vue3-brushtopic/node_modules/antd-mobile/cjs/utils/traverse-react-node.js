"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.traverseReactNode = traverseReactNode;
var _react = _interopRequireDefault(require("react"));
var _reactIs = require("react-is");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function traverseReactNode(children, fn) {
  let i = 0;
  function handle(target) {
    _react.default.Children.forEach(target, child => {
      if (!(0, _reactIs.isFragment)(child)) {
        fn(child, i);
        i += 1;
      } else {
        handle(child.props.children);
      }
    });
  }
  handle(children);
}