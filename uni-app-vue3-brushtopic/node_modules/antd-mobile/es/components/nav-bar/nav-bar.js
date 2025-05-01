import { LeftOutline } from 'antd-mobile-icons';
import classNames from 'classnames';
import React from 'react';
import { withNativeProps } from '../../utils/native-props';
import { mergeProp, mergeProps } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
const classPrefix = `adm-nav-bar`;
const defaultBackIcon = React.createElement(LeftOutline, null);
export const NavBar = props => {
  const {
    navBar: componentConfig = {}
  } = useConfig();
  const mergedProps = mergeProps(componentConfig, props);
  const {
    back,
    backIcon,
    backArrow
  } = mergedProps;
  const mergedDefaultBackIcon = componentConfig.backIcon || defaultBackIcon;
  const mergedBackIcon = mergeProp(defaultBackIcon, componentConfig.backIcon, backArrow === true ? mergedDefaultBackIcon : backArrow, backIcon === true ? mergedDefaultBackIcon : backIcon);
  return withNativeProps(mergedProps, React.createElement("div", {
    className: classNames(classPrefix)
  }, React.createElement("div", {
    className: `${classPrefix}-left`,
    role: 'button'
  }, back !== null && React.createElement("div", {
    className: `${classPrefix}-back`,
    onClick: mergedProps.onBack
  }, mergedBackIcon && React.createElement("span", {
    className: `${classPrefix}-back-arrow`
  }, mergedBackIcon), React.createElement("span", {
    "aria-hidden": 'true'
  }, back)), mergedProps.left), React.createElement("div", {
    className: `${classPrefix}-title`
  }, mergedProps.children), React.createElement("div", {
    className: `${classPrefix}-right`
  }, mergedProps.right)));
};