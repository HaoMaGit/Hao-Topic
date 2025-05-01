"use strict";

var _base = require("./base");
var _zhCN = _interopRequireDefault(require("./zh-CN"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
test('zh-CN', () => {
  function compareKeys(a, b) {
    return !Object.keys(a).some(key => {
      if (typeof b[key] === 'string') {
        return false;
      } else if (b[key]) {
        return !compareKeys(a[key], b[key]);
      } else {
        return true;
      }
    });
  }
  expect(compareKeys(_base.base, _zhCN.default)).toBeTruthy();
});