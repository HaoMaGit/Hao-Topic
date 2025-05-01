import React from 'react';
import { withNativeProps } from '../../utils/native-props';
const classPrefix = 'adm-auto-center';
export const AutoCenter = props => withNativeProps(props, React.createElement("div", {
  className: classPrefix
}, React.createElement("div", {
  className: `${classPrefix}-content`
}, props.children)));