import React from 'react';
import { withNativeProps } from '../../utils/native-props';
export const ThumbIcon = props => {
  return withNativeProps(props, React.createElement("svg", {
    viewBox: '0 0 24 24',
    xmlns: 'http://www.w3.org/2000/svg'
  }, React.createElement("g", {
    fill: 'currentColor',
    fillRule: 'evenodd'
  }, React.createElement("rect", {
    x: 10,
    width: 4,
    height: 24,
    rx: 2
  }), React.createElement("rect", {
    y: 4,
    width: 4,
    height: 16,
    rx: 2
  }), React.createElement("rect", {
    x: 20,
    y: 4,
    width: 4,
    height: 16,
    rx: 2
  }))));
};