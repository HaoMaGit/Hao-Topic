import React from 'react';
import classNames from 'classnames';
import { mergeProps } from '../../utils/with-default-props';
import { withNativeProps } from '../../utils/native-props';
const classPrefix = `adm-steps`;
const stepClassPrefix = `adm-step`;
const defaultIcon = React.createElement("span", {
  className: `${stepClassPrefix}-icon-dot`
});
const defaultProps = {
  current: 0,
  direction: 'horizontal'
};
export const Steps = p => {
  const props = mergeProps(defaultProps, p);
  const {
    direction,
    current
  } = props;
  const classString = classNames(classPrefix, `${classPrefix}-${direction}`);
  return withNativeProps(props, React.createElement("div", {
    className: classString
  }, React.Children.map(props.children, (child, index) => {
    var _a;
    if (!React.isValidElement(child)) {
      return child;
    }
    const childProps = child.props;
    let status = childProps.status || 'wait';
    if (index < current) {
      status = childProps.status || 'finish';
    } else if (index === current) {
      status = childProps.status || 'process';
    }
    const icon = (_a = childProps.icon) !== null && _a !== void 0 ? _a : defaultIcon;
    return React.cloneElement(child, {
      status,
      icon
    });
  })));
};