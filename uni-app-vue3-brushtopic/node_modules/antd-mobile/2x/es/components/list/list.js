import React, { forwardRef, useImperativeHandle, useRef } from 'react';
import classNames from 'classnames';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
const classPrefix = `adm-list`;
const defaultProps = {
  mode: 'default'
};
export const List = forwardRef((p, ref) => {
  const props = mergeProps(defaultProps, p);
  const nativeElementRef = useRef(null);
  useImperativeHandle(ref, () => ({
    get nativeElement() {
      return nativeElementRef.current;
    }
  }));
  return withNativeProps(props, React.createElement("div", {
    className: classNames(classPrefix, `${classPrefix}-${props.mode}`),
    ref: nativeElementRef
  }, props.header && React.createElement("div", {
    className: `${classPrefix}-header`
  }, props.header), React.createElement("div", {
    className: `${classPrefix}-body`
  }, React.createElement("div", {
    className: `${classPrefix}-body-inner`
  }, props.children))));
});