import React from 'react';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import classNames from 'classnames';
import Popup from '../popup';
import SafeArea from '../safe-area';
import { renderImperatively } from '../../utils/render-imperatively';
const classPrefix = `adm-action-sheet`;
const defaultProps = {
  visible: false,
  actions: [],
  cancelText: '',
  closeOnAction: false,
  closeOnMaskClick: true,
  safeArea: true,
  destroyOnClose: false,
  forceRender: false
};
export const ActionSheet = p => {
  const props = mergeProps(defaultProps, p);
  const {
    styles
  } = props;
  return React.createElement(Popup, {
    visible: props.visible,
    onMaskClick: () => {
      var _a, _b;
      (_a = props.onMaskClick) === null || _a === void 0 ? void 0 : _a.call(props);
      if (props.closeOnMaskClick) {
        (_b = props.onClose) === null || _b === void 0 ? void 0 : _b.call(props);
      }
    },
    afterClose: props.afterClose,
    className: classNames(`${classPrefix}-popup`, props.popupClassName),
    style: props.popupStyle,
    getContainer: props.getContainer,
    destroyOnClose: props.destroyOnClose,
    forceRender: props.forceRender,
    bodyStyle: styles === null || styles === void 0 ? void 0 : styles.body,
    maskStyle: styles === null || styles === void 0 ? void 0 : styles.mask
  }, withNativeProps(props, React.createElement("div", {
    className: classPrefix
  }, props.extra && React.createElement("div", {
    className: `${classPrefix}-extra`
  }, props.extra), React.createElement("div", {
    className: `${classPrefix}-button-list`
  }, props.actions.map((action, index) => React.createElement("div", {
    key: action.key,
    className: `${classPrefix}-button-item-wrapper`
  }, React.createElement("a", {
    className: classNames('adm-plain-anchor', `${classPrefix}-button-item`, {
      [`${classPrefix}-button-item-danger`]: action.danger,
      [`${classPrefix}-button-item-disabled`]: action.disabled,
      [`${classPrefix}-button-item-bold`]: action.bold
    }),
    onClick: () => {
      var _a, _b, _c;
      (_a = action.onClick) === null || _a === void 0 ? void 0 : _a.call(action);
      (_b = props.onAction) === null || _b === void 0 ? void 0 : _b.call(props, action, index);
      if (props.closeOnAction) {
        (_c = props.onClose) === null || _c === void 0 ? void 0 : _c.call(props);
      }
    },
    role: 'option',
    "aria-disabled": action.disabled
  }, React.createElement("div", {
    className: `${classPrefix}-button-item-name`
  }, action.text), action.description && React.createElement("div", {
    className: `${classPrefix}-button-item-description`
  }, action.description))))), props.cancelText && React.createElement("div", {
    className: `${classPrefix}-cancel`,
    role: 'option',
    "aria-label": props.cancelText
  }, React.createElement("div", {
    className: `${classPrefix}-button-item-wrapper`
  }, React.createElement("a", {
    className: classNames('adm-plain-anchor', `${classPrefix}-button-item`),
    onClick: props.onClose
  }, React.createElement("div", {
    className: `${classPrefix}-button-item-name`
  }, props.cancelText)))), props.safeArea && React.createElement(SafeArea, {
    position: 'bottom'
  }))));
};
export function showActionSheet(props) {
  return renderImperatively(React.createElement(ActionSheet, Object.assign({}, props)));
}