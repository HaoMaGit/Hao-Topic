import { __rest } from "tslib";
import classNames from 'classnames';
import RcSegmented from 'rc-segmented';
import * as React from 'react';
import { withNativeProps } from '../../utils/native-props';
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
    restProps = __rest(props
    // syntactic sugar to support `icon` for Segmented Item
    , ["prefixCls", "className", "block", "options"]);
  // syntactic sugar to support `icon` for Segmented Item
  const extendedOptions = React.useMemo(() => options.map(option => {
    if (isSegmentedLabeledOptionWithIcon(option)) {
      const {
          icon,
          label
        } = option,
        restOption = __rest(option, ["icon", "label"]);
      return Object.assign(Object.assign({}, restOption), {
        label: React.createElement(React.Fragment, null, React.createElement("span", {
          className: `${classPrefix}-item-icon`
        }, icon), label && React.createElement("span", null, label))
      });
    }
    return option;
  }), [options, classPrefix]);
  return withNativeProps(props, React.createElement(RcSegmented, Object.assign({}, restProps, {
    className: classNames(className, {
      [`${classPrefix}-block`]: block
    }),
    options: extendedOptions,
    ref: ref,
    prefixCls: classPrefix
  })));
});
if (process.env.NODE_ENV !== 'production') {
  Segmented.displayName = 'Segmented';
}
export { Segmented };