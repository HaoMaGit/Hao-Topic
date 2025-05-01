import React, { useRef, useState } from 'react';
import { useDrag } from '@use-gesture/react';
import { ThumbIcon } from './thumb-icon';
import Popover from '../popover';
import { useConfig } from '../config-provider';
const classPrefix = `adm-slider`;
const Thumb = props => {
  const {
    value,
    min,
    max,
    disabled,
    icon,
    residentPopover,
    onDrag
  } = props;
  const prevValue = useRef(value);
  const {
    locale
  } = useConfig();
  const currentPosition = () => {
    return {
      left: `${(value - min) / (max - min) * 100}%`,
      right: 'auto'
    };
  };
  const [dragging, setDragging] = useState(false);
  const bind = useDrag(state => {
    var _a;
    if (disabled) return;
    if (state.first) {
      prevValue.current = value;
    }
    const x = state.xy[0] - state.initial[0];
    const sliderOffsetWith = (_a = props.trackRef.current) === null || _a === void 0 ? void 0 : _a.offsetWidth;
    if (!sliderOffsetWith) return;
    const diff = x / Math.ceil(sliderOffsetWith) * (max - min);
    onDrag(prevValue.current + diff, state.first, state.last);
    setDragging(!state.last);
  }, {
    axis: 'x',
    pointer: {
      touch: true
    }
  });
  const renderPopoverContent = typeof props.popover === 'function' ? props.popover : props.popover ? value => value.toString() : null;
  const thumbElement = React.createElement("div", {
    className: `${classPrefix}-thumb`
  }, icon ? icon : React.createElement(ThumbIcon, {
    className: `${classPrefix}-thumb-icon`
  }));
  return React.createElement("div", Object.assign({
    className: `${classPrefix}-thumb-container`,
    style: currentPosition()
  }, bind(), {
    role: 'slider',
    "aria-label": props['aria-label'] || locale.Slider.name,
    "aria-valuemax": max,
    "aria-valuemin": min,
    "aria-valuenow": value,
    "aria-disabled": disabled
  }), renderPopoverContent ? React.createElement(Popover, {
    content: renderPopoverContent(value),
    placement: 'top',
    visible: residentPopover || dragging,
    getContainer: null,
    mode: 'dark'
  }, thumbElement) : thumbElement);
};
export default Thumb;