import React from 'react';
import classNames from 'classnames';
import Button from '../button';
import { withNativeProps } from '../../utils/native-props';
export const DialogActionButton = props => {
  const {
    action
  } = props;
  return withNativeProps(props.action, React.createElement(Button, {
    key: action.key,
    onClick: props.onAction,
    className: classNames('adm-dialog-button', {
      'adm-dialog-button-bold': action.bold
    }),
    fill: 'none',
    shape: 'rectangular',
    block: true,
    color: action.danger ? 'danger' : 'primary',
    loading: 'auto',
    disabled: action.disabled
  }, action.text));
};