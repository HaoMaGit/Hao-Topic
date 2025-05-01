import React from 'react';
import { withNativeProps } from '../../utils/native-props';
import classNames from 'classnames';
const classPrefix = `adm-result-page-card`;
export const ResultPageCard = props => {
  return withNativeProps(props, React.createElement('div', {
    className: classNames(`${classPrefix}`)
  }, props.children));
};