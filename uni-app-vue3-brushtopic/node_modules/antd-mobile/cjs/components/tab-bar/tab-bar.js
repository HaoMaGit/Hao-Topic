"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.TabBarItem = exports.TabBar = void 0;
var _react = _interopRequireWildcard(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _badge = _interopRequireDefault(require("../badge"));
var _safeArea = _interopRequireDefault(require("../safe-area"));
var _usePropsValue = require("../../utils/use-props-value");
var _traverseReactNode = require("../../utils/traverse-react-node");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
/* istanbul ignore next */
const TabBarItem = () => {
  return null;
};
exports.TabBarItem = TabBarItem;
const classPrefix = `adm-tab-bar`;
const defaultProps = {
  safeArea: false
};
const TabBar = p => {
  var _a;
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  let firstActiveKey = null;
  const items = [];
  (0, _traverseReactNode.traverseReactNode)(props.children, (child, index) => {
    if (!(0, _react.isValidElement)(child)) return;
    const key = child.key;
    if (typeof key !== 'string') return;
    if (index === 0) {
      firstActiveKey = key;
    }
    items.push(child);
  });
  const [activeKey, setActiveKey] = (0, _usePropsValue.usePropsValue)({
    value: props.activeKey,
    defaultValue: (_a = props.defaultActiveKey) !== null && _a !== void 0 ? _a : firstActiveKey,
    onChange: v => {
      var _a;
      if (v === null) return;
      (_a = props.onChange) === null || _a === void 0 ? void 0 : _a.call(props, v);
    }
  });
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: classPrefix
  }, _react.default.createElement("div", {
    className: `${classPrefix}-wrap`
  }, items.map(item => {
    const active = item.key === activeKey;
    function renderContent() {
      const iconElement = item.props.icon && _react.default.createElement("div", {
        className: `${classPrefix}-item-icon`
      }, typeof item.props.icon === 'function' ? item.props.icon(active) : item.props.icon);
      const titleElement = item.props.title && _react.default.createElement("div", {
        className: (0, _classnames.default)(`${classPrefix}-item-title`, Boolean(iconElement) && `${classPrefix}-item-title-with-icon`)
      }, typeof item.props.title === 'function' ? item.props.title(active) : item.props.title);
      if (iconElement) {
        return _react.default.createElement(_react.default.Fragment, null, _react.default.createElement(_badge.default, {
          content: item.props.badge,
          className: `${classPrefix}-icon-badge`
        }, iconElement), titleElement);
      } else if (titleElement) {
        return _react.default.createElement(_badge.default, {
          content: item.props.badge,
          className: `${classPrefix}-title-badge`
        }, titleElement);
      }
      return null;
    }
    return (0, _nativeProps.withNativeProps)(item.props, _react.default.createElement("div", {
      key: item.key,
      onClick: () => {
        var _a, _b;
        const {
          key
        } = item;
        if (key === undefined || key === null) return;
        setActiveKey(key.toString());
        (_b = (_a = item.props).onClick) === null || _b === void 0 ? void 0 : _b.call(_a);
      },
      className: (0, _classnames.default)(`${classPrefix}-item`, {
        [`${classPrefix}-item-active`]: active
      })
    }, renderContent()));
  })), props.safeArea && _react.default.createElement(_safeArea.default, {
    position: 'bottom'
  })));
};
exports.TabBar = TabBar;