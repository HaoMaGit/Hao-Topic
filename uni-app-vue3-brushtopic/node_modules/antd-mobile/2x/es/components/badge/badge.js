import classNames from 'classnames';
import React from 'react';
import { withNativeProps } from '../../utils/native-props';
const classPrefix = `adm-badge`;
export const dot = React.createElement(React.Fragment, null);
export const Badge = props => {
  const {
    content,
    color,
    children
  } = props;
  const isDot = content === dot;
  const badgeClass = classNames(classPrefix, {
    [`${classPrefix}-fixed`]: !!children,
    [`${classPrefix}-dot`]: isDot,
    [`${classPrefix}-bordered`]: props.bordered
  });
  const element = content || content === 0 ? withNativeProps(props, React.createElement("div", {
    className: badgeClass,
    style: {
      '--color': color
    }
  }, !isDot && React.createElement("div", {
    className: `${classPrefix}-content`
  }, content))) : null;
  return children ? React.createElement("div", {
    className: classNames(`${classPrefix}-wrapper`, props.wrapperClassName),
    style: props.wrapperStyle
  }, children, element) : element;
};