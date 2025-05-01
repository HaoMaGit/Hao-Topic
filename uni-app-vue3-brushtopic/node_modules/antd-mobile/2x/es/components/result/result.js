import React from 'react';
import classNames from 'classnames';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import { useResultIcon } from './use-result-icon';
const classPrefix = `adm-result`;
const defaultProps = {
  status: 'info'
};
export const Result = p => {
  const props = mergeProps(defaultProps, p);
  const {
    status,
    title,
    description,
    icon
  } = props;
  const fallbackIcon = useResultIcon(status);
  if (!status) return null;
  return withNativeProps(props, React.createElement("div", {
    className: classNames(classPrefix, `${classPrefix}-${status}`)
  }, React.createElement("div", {
    className: `${classPrefix}-icon`
  }, icon || fallbackIcon), React.createElement("div", {
    className: `${classPrefix}-title`
  }, title), !!description && React.createElement("div", {
    className: `${classPrefix}-description`
  }, description)));
};