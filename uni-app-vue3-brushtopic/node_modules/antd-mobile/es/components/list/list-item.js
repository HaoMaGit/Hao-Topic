import { RightOutline } from 'antd-mobile-icons';
import classNames from 'classnames';
import React from 'react';
import { isNodeWithContent } from '../../utils/is-node-with-content';
import { withNativeProps } from '../../utils/native-props';
import { mergeProp } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
const classPrefix = `adm-list-item`;
export const ListItem = props => {
  var _a, _b;
  const {
    arrow,
    arrowIcon
  } = props;
  const {
    list: componentConfig = {}
  } = useConfig();
  const clickable = (_a = props.clickable) !== null && _a !== void 0 ? _a : !!props.onClick;
  const showArrow = (_b = arrow !== null && arrow !== void 0 ? arrow : arrowIcon) !== null && _b !== void 0 ? _b : clickable;
  const mergedArrowIcon = mergeProp(componentConfig.arrowIcon, arrow !== true ? arrow : null, arrowIcon !== true ? arrowIcon : null);
  const content = React.createElement("div", {
    className: `${classPrefix}-content`
  }, isNodeWithContent(props.prefix) && React.createElement("div", {
    className: `${classPrefix}-content-prefix`
  }, props.prefix), React.createElement("div", {
    className: `${classPrefix}-content-main`
  }, isNodeWithContent(props.title) && React.createElement("div", {
    className: `${classPrefix}-title`
  }, props.title), props.children, isNodeWithContent(props.description) && React.createElement("div", {
    className: `${classPrefix}-description`
  }, props.description)), isNodeWithContent(props.extra) && React.createElement("div", {
    className: `${classPrefix}-content-extra`
  }, props.extra), showArrow && React.createElement("div", {
    className: `${classPrefix}-content-arrow`
  }, mergedArrowIcon || React.createElement(RightOutline, null)));
  return withNativeProps(props, React.createElement(clickable ? 'a' : 'div', {
    className: classNames(`${classPrefix}`, clickable ? ['adm-plain-anchor'] : [], props.disabled && `${classPrefix}-disabled`),
    onClick: props.disabled ? undefined : props.onClick
  }, content));
};