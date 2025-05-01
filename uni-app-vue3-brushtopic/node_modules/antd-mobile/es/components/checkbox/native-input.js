import { useMemoizedFn } from 'ahooks';
import React, { useEffect, useRef } from 'react';
export const NativeInput = props => {
  const inputRef = useRef(null);
  const handleClick = useMemoizedFn(e => {
    e.stopPropagation();
    e.stopImmediatePropagation();
    const latestChecked = e.target.checked;
    if (latestChecked === props.checked) return;
    props.onChange(latestChecked);
  });
  useEffect(() => {
    if (props.disabled) return;
    if (!inputRef.current) return;
    const input = inputRef.current;
    input.addEventListener('click', handleClick);
    return () => {
      input.removeEventListener('click', handleClick);
    };
  }, [props.disabled, props.onChange]);
  return React.createElement("input", {
    ref: inputRef,
    type: props.type,
    checked: props.checked,
    onChange: () => {},
    disabled: props.disabled,
    id: props.id
  });
};