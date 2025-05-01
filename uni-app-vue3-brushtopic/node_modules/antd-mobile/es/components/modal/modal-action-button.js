import React from 'react';
import classNames from 'classnames';
import Button from '../button';
import { withNativeProps } from '../../utils/native-props';
export const ModalActionButton = props => {
  const {
    action
  } = props;
  return withNativeProps(props.action, React.createElement(Button, {
    key: action.key,
    onClick: props.onAction,
    className: classNames('adm-modal-button', {
      'adm-modal-button-primary': props.action.primary
    }),
    fill: props.action.primary ? 'solid' : 'none',
    size: props.action.primary ? 'large' : 'middle',
    block: true,
    color: action.danger ? 'danger' : 'primary',
    loading: 'auto',
    disabled: action.disabled
  }, action.text));
};