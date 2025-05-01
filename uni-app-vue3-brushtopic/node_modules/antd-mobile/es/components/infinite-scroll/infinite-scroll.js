import { __awaiter } from "tslib";
import { mergeProps } from '../../utils/with-default-props';
import React, { useEffect, useRef, useState } from 'react';
import { useLockFn, useThrottleFn } from 'ahooks';
import { withNativeProps } from '../../utils/native-props';
import { getScrollParent } from '../../utils/get-scroll-parent';
import { useConfig } from '../config-provider';
import DotLoading from '../dot-loading';
function isWindow(element) {
  return element === window;
}
const classPrefix = `adm-infinite-scroll`;
const defaultProps = {
  threshold: 250,
  children: (hasMore, failed, retry) => React.createElement(InfiniteScrollContent, {
    hasMore: hasMore,
    failed: failed,
    retry: retry
  })
};
export const InfiniteScroll = p => {
  const props = mergeProps(defaultProps, p);
  const [failed, setFailed] = useState(false);
  const doLoadMore = useLockFn(isRetry => __awaiter(void 0, void 0, void 0, function* () {
    try {
      yield props.loadMore(isRetry);
    } catch (e) {
      setFailed(true);
      throw e;
    }
  }));
  const elementRef = useRef(null);
  // Prevent duplicated trigger of `check` function
  const [flag, setFlag] = useState({});
  const nextFlagRef = useRef(flag);
  const [scrollParent, setScrollParent] = useState();
  const {
    run: check
  } = useThrottleFn(() => __awaiter(void 0, void 0, void 0, function* () {
    if (nextFlagRef.current !== flag) return;
    if (!props.hasMore) return;
    const element = elementRef.current;
    if (!element) return;
    if (!element.offsetParent) return;
    const parent = getScrollParent(element);
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
  useEffect(() => {
    check();
  });
  useEffect(() => {
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
    return __awaiter(this, void 0, void 0, function* () {
      setFailed(false);
      try {
        yield doLoadMore(true);
        setFlag(nextFlagRef.current);
      } catch (e) {}
    });
  }
  return withNativeProps(props, React.createElement("div", {
    className: classPrefix,
    ref: elementRef
  }, typeof props.children === 'function' ? props.children(props.hasMore, failed, retry) : props.children));
};
const InfiniteScrollContent = props => {
  const {
    locale
  } = useConfig();
  if (!props.hasMore) {
    return React.createElement("span", null, locale.InfiniteScroll.noMore);
  }
  if (props.failed) {
    return React.createElement("span", null, React.createElement("span", {
      className: `${classPrefix}-failed-text`
    }, locale.InfiniteScroll.failedToLoad), React.createElement("a", {
      onClick: () => {
        props.retry();
      }
    }, locale.InfiniteScroll.retry));
  }
  return React.createElement(React.Fragment, null, React.createElement("span", null, locale.common.loading), React.createElement(DotLoading, null));
};