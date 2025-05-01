import React, { useState } from 'react';
import classNames from 'classnames';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import { isNodeWithContent } from '../../utils/is-node-with-content';
import Button from '../button';
import { useResultIcon } from '../result/use-result-icon';
const classPrefix = `adm-result-page`;
const defaultProps = {
  status: 'info',
  details: []
};
export const ResultPage = p => {
  const props = mergeProps(defaultProps, p);
  const {
    status,
    title,
    description,
    details,
    icon,
    primaryButtonText,
    secondaryButtonText,
    onPrimaryButtonClick,
    onSecondaryButtonClick
  } = props;
  const fallbackIcon = useResultIcon(status);
  const [collapse, setCollapse] = useState(true);
  const showSecondaryButton = isNodeWithContent(secondaryButtonText);
  const showPrimaryButton = isNodeWithContent(primaryButtonText);
  return withNativeProps(props, React.createElement("div", {
    className: classPrefix
  }, React.createElement("div", {
    className: `${classPrefix}-header`
  }, React.createElement("div", {
    className: `${classPrefix}-icon`
  }, icon || fallbackIcon), React.createElement("div", {
    className: `${classPrefix}-title`
  }, title), isNodeWithContent(description) ? React.createElement("div", {
    className: `${classPrefix}-description`
  }, description) : null, (details === null || details === void 0 ? void 0 : details.length) ? React.createElement("div", {
    className: `${classPrefix}-details`
  }, (collapse ? details.slice(0, 3) : details).map((detail, index) => {
    return React.createElement("div", {
      className: classNames(`${classPrefix}-detail`, detail.bold && `${classPrefix}-detail-bold`),
      key: index
    }, React.createElement("span", null, detail.label), React.createElement("span", null, detail.value));
  }), details.length > 3 && React.createElement("div", {
    onClick: () => setCollapse(prev => !prev)
  }, React.createElement("div", {
    className: classNames(`${classPrefix}-collapse`, !collapse && `${classPrefix}-collapse-active`)
  }))) : null, React.createElement("div", {
    className: `${classPrefix}-bgWrapper`
  }, React.createElement("div", {
    className: `${classPrefix}-bg`
  }))), React.createElement("div", {
    className: `${classPrefix}-content`
  }, props.children), (showPrimaryButton || showSecondaryButton) && React.createElement("div", {
    className: `${classPrefix}-footer`
  }, showSecondaryButton && React.createElement(Button, {
    block: true,
    color: 'default',
    fill: 'solid',
    size: 'large',
    onClick: onSecondaryButtonClick,
    className: `${classPrefix}-footer-btn`
  }, secondaryButtonText), showPrimaryButton && showSecondaryButton && React.createElement("div", {
    className: `${classPrefix}-footer-space`
  }), showPrimaryButton && React.createElement(Button, {
    block: true,
    color: 'primary',
    fill: 'solid',
    size: 'large',
    onClick: onPrimaryButtonClick,
    className: `${classPrefix}-footer-btn`
  }, primaryButtonText))));
};