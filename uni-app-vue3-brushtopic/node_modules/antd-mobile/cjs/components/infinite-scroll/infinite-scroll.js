"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.InfiniteScroll = void 0;
var _tslib = require("tslib");
var _withDefaultProps = require("../../utils/with-default-props");
var _react = _interopRequireWildcard(require("react"));
var _ahooks = require("ahooks");
var _nativeProps = require("../../utils/native-props");
var _getScrollParent = require("../../utils/get-scroll-parent");
var _configProvider = require("../config-provider");
var _dotLoading = _interopRequireDefault(require("../dot-loading"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function isWindow(element) {
  return element === window;
}
const classPrefix = `adm-infinite-scroll`;
const defaultProps = {
  threshold: 250,
  children: (hasMore, failed, retry) => _react.default.createElement(InfiniteScrollContent, {
    hasMore: hasMore,
    failed: failed,
    retry: retry
  })
};
const InfiniteScroll = p => {
  const props = (0, _withDefaultProps.mergeProps)(defaultProps, p);
  const [failed, setFailed] = (0, _react.useState)(false);
  const doLoadMore = (0, _ahooks.useLockFn)(isRetry => (0, _tslib.__awaiter)(void 0, void 0, void 0, function* () {
    try {
      yield props.loadMore(isRetry);
    } catch (e) {
      setFailed(true);
      throw e;
    }
  }));
  const elementRef = (0, _react.useRef)(null);
  // Prevent duplicated trigger of `check` function
  const [flag, setFlag] = (0, _react.useState)({});
  const nextFlagRef = (0, _react.useRef)(flag);
  const [scrollParent, setScrollParent] = (0, _react.useState)();
  const {
    run: check
  } = (0, _ahooks.useThrottleFn)(() => (0, _tslib.__awaiter)(void 0, void 0, void 0, function* () {
    if (nextFlagRef.current !== flag) return;
    if (!props.hasMore) return;
    const element = elementRef.current;
    if (!element) return;
    if (!element.offsetParent) return;
    const parent = (0, _getScrollParent.getScrollParent)(element);
    setScrollParent(parent);
    if (!parent) return;
    const rect = element.getBoundingClientRect();
    const elementTop = rect.top;
    const current = isWindow(parent) ? window.innerHeight : parent.getBoundingClientRect().bottom;
    if (current >= elementTop - props.threshold) {
      const nextFlag = {};
      nextFlagRef.current = nextFlag;
      try {
        yield doLoadMore(false);
        setFlag(nextFlag);
      } catch (e) {}
    }
  }), {
    wait: 100,
    leading: true,
    trailing: true
  });
  // Make sure to trigger `loadMore` when content changes
  (0, _react.useEffect)(() => {
    check();
  });
  (0, _react.useEffect)(() => {
    const element = elementRef.current;
    if (!element) return;
    if (!scrollParent) return;
    function onScroll() {
      check();
    }
    scrollParent.addEventListener('scroll', onScroll);
    return () => {
      scrollParent.removeEventListener('scroll', onScroll);
    };
  }, [scrollParent]);
  function retry() {
    return (0, _tslib.__awaiter)(this, void 0, void 0, function* () {
      setFailed(false);
      try {
        yield doLoadMore(true);
        setFlag(nextFlagRef.current);
      } catch (e) {}
    });
  }
  return (0, _nativeProps.withNativeProps)(props, _react.default.createElement("div", {
    className: classPrefix,
    ref: elementRef
  }, typeof props.children === 'function' ? props.children(props.hasMore, failed, retry) : props.children));
};
exports.InfiniteScroll = InfiniteScroll;
const InfiniteScrollContent = props => {
  const {
    locale
  } = (0, _configProvider.useConfig)();
  if (!props.hasMore) {
    return _react.default.createElement("span", null, locale.InfiniteScroll.noMore);
  }
  if (props.failed) {
    return _react.default.createElement("span", null, _react.default.createElement("span", {
      className: `${classPrefix}-failed-text`
    }, locale.InfiniteScroll.failedToLoad), _react.default.createElement("a", {
      onClick: () => {
        props.retry();
      }
    }, locale.InfiniteScroll.retry));
  }
  return _react.default.createElement(_react.default.Fragment, null, _react.default.createElement("span", null, locale.common.loading), _react.default.createElement(_dotLoading.default, null));
};