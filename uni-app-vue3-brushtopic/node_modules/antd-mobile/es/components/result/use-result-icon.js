import React from 'react';
import { CheckCircleFill, CloseCircleFill, InformationCircleFill, ClockCircleFill, ExclamationCircleFill } from 'antd-mobile-icons';
import { useConfig } from '../config-provider';
export const useResultIcon = status => {
  const {
    result: componentConfig = {}
  } = useConfig();
  const {
    successIcon = React.createElement(CheckCircleFill, null),
    errorIcon = React.createElement(CloseCircleFill, null),
    infoIcon = React.createElement(InformationCircleFill, null),
    waitingIcon = React.createElement(ClockCircleFill, null),
    warningIcon = React.createElement(ExclamationCircleFill, null)
  } = componentConfig || {};
  switch (status) {
    case 'success':
      return successIcon;
    case 'error':
      return errorIcon;
    case 'info':
      return infoIcon;
    case 'waiting':
      return waitingIcon;
    case 'warning':
      return warningIcon;
    default:
      return null;
  }
};