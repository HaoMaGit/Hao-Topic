import React from 'react';
import { CloseOutline } from 'antd-mobile-icons';
export const defaultPopupBaseProps = {
  closeOnMaskClick: false,
  closeIcon: React.createElement(CloseOutline, null),
  destroyOnClose: false,
  disableBodyScroll: true,
  forceRender: false,
  getContainer: () => document.body,
  mask: true,
  showCloseButton: false,
  stopPropagation: ['click'],
  visible: false
};