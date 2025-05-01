"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.CascaderView = void 0;
var _react = _interopRequireWildcard(require("react"));
var _classnames = _interopRequireDefault(require("classnames"));
var _tabs = _interopRequireDefault(require("../tabs"));
var _checkList = _interopRequireDefault(require("../check-list"));
var _nativeProps = require("../../utils/native-props");
var _withDefaultProps = require("../../utils/with-default-props");
var _usePropsValue = require("../../utils/use-props-value");
var _useCascaderValueExtend = require("./use-cascader-value-extend");
var _configProvider = require("../config-provider");
var _optionSkeleton = require("./option-skeleton");
var _skeleton = _interopRequireDefault(require("../skeleton"));
var _ahooks = require("ahooks");
var _hooks = require("../../hooks");
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const classPrefix = `adm-cascader-view`;
const defaultProps = {
  defaultValue: []
};
const CascaderView = p => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const {
    locale
  } = (0, _configProvider.useConfig)();
  const [labelName, valueName, childrenName, disabledName] = (0, _hooks.useFieldNames)(props.fieldNames);
  const generateValueExtend = (0, _useCascaderValueExtend.useCascaderValueExtend)(props.options, {
    valueName,
    childrenName
  });
  const [value, setValue] = (0, _usePropsValue.usePropsValue)(Object.assign(Object.assign({}, props), {
    onChange: val => {
      var _a;
      (_a = props.onChange) === null || _a === void 0 ? void 0 : _a.call(props, val, generateValueExtend(val));
    }
  }));
  const [tabActiveIndex, setTabActiveIndex] = (0, _react.useState)(0);
  const levels = (0, _react.useMemo)(() => {
    const ret = [];
    let currentOptions = props.options;
    let reachedEnd = false;
    for (const v of value) {
      const target = currentOptions.find(option => option[valueName] === v);
      ret.push({
        selected: target,
        options: currentOptions
      });
      if (!target || !target[childrenName]) {
        reachedEnd = true;
        break;
      }
      currentOptions = target[childrenName];
    }
    if (!reachedEnd) {
      ret.push({
        selected: undefined,
        options: currentOptions
      });
    }
    return ret;
  }, [value, props.options]);
  (0, _ahooks.useUpdateEffect)(() => {
    var _a;
    (_a = props.onTabsChange) === null || _a === void 0 ? void 0 : _a.call(props, tabActiveIndex);
  }, [tabActiveIndex]);
  (0, _react.useEffect)(() => {
    setTabActiveIndex(levels.length - 1);
  }, [value]);
  (0, _react.useEffect)(() => {
    const max = levels.length - 1;
    if (tabActiveIndex > max) {
      setTabActiveIndex(max);
    }
  }, [tabActiveIndex, levels]);
  const onItemSelect = (selectValue, depth) => {
    const next = value.slice(0, depth);
    if (selectValue !== undefined) {
      next[depth] = selectValue;
    }
    setValue(next);
  };
  const whetherLoading = options => props.loading || options === _optionSkeleton.optionSkeleton;
  const placeholder = props.placeholder || locale.Cascader.placeholder;
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: classPrefix
  }, _react.default.createElement(_tabs.default, {
    activeKey: tabActiveIndex.toString(),
    onChange: key => {
      const activeIndex = parseInt(key);
      setTabActiveIndex(activeIndex);
    },
    stretch: false,
    className: `${classPrefix}-tabs`
  }, levels.map((level, index) => {
    const selected = level.selected;
    return _react.default.createElement(_tabs.default.Tab, {
      key: index.toString(),
      title: _react.default.createElement("div", {
        className: `${classPrefix}-header-title`
      }, selected ? selected[labelName] : typeof placeholder === 'function' ? placeholder(index) : placeholder),
      forceRender: true
    }, _react.default.createElement("div", {
      className: `${classPrefix}-content`
    }, whetherLoading(level.options) ? _react.default.createElement("div", {
      className: `${classPrefix}-skeleton`
    }, _react.default.createElement(_skeleton.default, {
      className: `${classPrefix}-skeleton-line-1`,
      animated: true
    }), _react.default.createElement(_skeleton.default, {
      className: `${classPrefix}-skeleton-line-2`,
      animated: true
    }), _react.default.createElement(_skeleton.default, {
      className: `${classPrefix}-skeleton-line-3`,
      animated: true
    }), _react.default.createElement(_skeleton.default, {
      className: `${classPrefix}-skeleton-line-4`,
      animated: true
    })) : _react.default.createElement(_checkList.default, {
      value: [value[index]],
      onChange: selectValue => onItemSelect(selectValue[0], index),
      activeIcon: props.activeIcon
    }, level.options.map(option => {
      const active = value[index] === option[valueName];
      return _react.default.createElement(_checkList.default.Item, {
        value: option[valueName],
        key: option[valueName],
        disabled: option[disabledName],
        className: (0, _classnames.default)(`${classPrefix}-item`, {
          [`${classPrefix}-item-active`]: active
        })
      }, option[labelName]);
    }))));
  }))));
};
exports.CascaderView = CascaderView;