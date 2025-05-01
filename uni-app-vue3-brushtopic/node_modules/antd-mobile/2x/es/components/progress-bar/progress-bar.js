import React from 'react';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import classNames from 'classnames';
import { isNodeWithContent } from '../../utils/is-node-with-content';
const classPrefix = `adm-progress-bar`;
const defaultProps = {
  percent: 0,
  rounded: true,
  text: false
};
export const ProgressBar = p => {
  const props = mergeProps(defaultProps, p);
  const fillStyle = {
    width: `${props.percent}%`
  };
  const textElement = function () {
    if (props.text === true) {
      return `${props.percent}%`;
    }
    if (typeof props.text === 'function') {
      return props.text(props.percent);
    }
    return props.text;
  }();
  return withNativeProps(props, React.createElement("div", {
    className: classNames(classPrefix, props.rounded && `${classPrefix}-rounded`)
  }, React.createElement("div", {
    className: `${classPrefix}-trail`
  }, React.createElement("div", {
    className: `${classPrefix}-fill`,
    style: fillStyle
  })), isNodeWithContent(textElement) && React.createElement("div", {
    className: `${classPrefix}-text`
  }, textElement)));
};