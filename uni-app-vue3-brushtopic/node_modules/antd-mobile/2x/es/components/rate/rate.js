import React, { useRef } from 'react';
import classNames from 'classnames';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import { usePropsValue } from '../../utils/use-props-value';
import { Star } from './star';
import { useDrag } from '@use-gesture/react';
import { bound } from '../../utils/bound';
const classPrefix = `adm-rate`;
const defaultProps = {
  count: 5,
  allowHalf: false,
  character: React.createElement(Star, null),
  defaultValue: 0,
  readOnly: false,
  allowClear: true
};
export const Rate = p => {
  const props = mergeProps(defaultProps, p);
  const [value, setValue] = usePropsValue(props);
  const containerRef = useRef(null);
  const starList = Array(props.count).fill(null);
  function renderStar(v, half) {
    return React.createElement("div", {
      className: classNames(`${classPrefix}-star`, {
        [`${classPrefix}-star-active`]: value >= v,
        [`${classPrefix}-star-half`]: half,
        [`${classPrefix}-star-readonly`]: props.readOnly
      }),
      role: 'radio',
      "aria-checked": value >= v,
      "aria-label": '' + v
    }, props.character);
  }
  const bind = useDrag(state => {
    if (props.readOnly) return;
    const {
      xy: [clientX],
      tap
    } = state;
    const container = containerRef.current;
    if (!container) return;
    const rect = container.getBoundingClientRect();
    const rawValue = (clientX - rect.left) / rect.width * props.count;
    const ceiledValue = props.allowHalf ? Math.ceil(rawValue * 2) / 2 : Math.ceil(rawValue);
    const boundValue = bound(ceiledValue, 0, props.count);
    if (tap) {
      if (props.allowClear && boundValue === value) {
        setValue(0);
        return;
      }
    }
    setValue(boundValue);
  }, {
    axis: 'x',
    pointer: {
      touch: true
    },
    filterTaps: true
  });
  return withNativeProps(props, React.createElement("div", Object.assign({
    className: classNames(classPrefix, {
      [`${classPrefix}-half`]: props.allowHalf
    }),
    role: 'radiogroup',
    "aria-readonly": props.readOnly,
    ref: containerRef
  }, bind()), starList.map((_, i) => React.createElement("div", {
    key: i,
    className: classNames(`${classPrefix}-box`)
  }, props.allowHalf && renderStar(i + 0.5, true), renderStar(i + 1, false)))));
};