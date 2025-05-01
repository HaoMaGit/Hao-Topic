"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useCascaderValueExtend = useCascaderValueExtend;
var _react = require("react");
var _reactFastCompare = _interopRequireDefault(require("react-fast-compare"));
var _nanoMemoize = _interopRequireDefault(require("nano-memoize"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function useCascaderValueExtend(options, fieldNames) {
  const {
    valueName,
    childrenName
  } = fieldNames;
  const generateItems = (0, _react.useMemo)(() => {
    return (0, _nanoMemoize.default)(val => {
      const ret = [];
      let currentOptions = options;
      for (const v of val) {
        const target = currentOptions.find(option => option[valueName] === v);
        if (!target) {
          break;
        }
        ret.push(target);
        if (!target[childrenName]) break;
        currentOptions = target[childrenName];
      }
      return ret;
    }, {
      equals: _reactFastCompare.default
    });
  }, [options]);
  const generateIsLeaf = (0, _react.useMemo)(() => {
    return (0, _nanoMemoize.default)(val => {
      const children = val.reduce((currentOptions, v) => {
        var _a;
        return ((_a = currentOptions.find(option => option[valueName] === v)) === null || _a === void 0 ? void 0 : _a[childrenName]) || [];
      }, options);
      return children.length === 0;
    }, {
      equals: _reactFastCompare.default
    });
  }, [options]);
  function generateValueExtend(val) {
    return {
      get items() {
        return generateItems(val);
      },
      get isLeaf() {
        return generateIsLeaf(val);
      }
    };
  }
  return generateValueExtend;
}