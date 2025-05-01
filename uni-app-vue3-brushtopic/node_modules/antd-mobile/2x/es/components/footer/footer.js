import React from 'react';
import classNames from 'classnames';
import { withNativeProps } from '../../utils/native-props';
import { mergeProps } from '../../utils/with-default-props';
import Divider from '../divider';
const classPrefix = `adm-footer`;
const defaultProps = {
  label: '',
  links: [],
  content: '',
  chips: []
};
export const Footer = p => {
  const props = mergeProps(defaultProps, p);
  const {
    label,
    links,
    content,
    chips,
    onChipClick,
    onLinkClick
  } = props;
  const clickChipItem = (item, index) => {
    if ((chips === null || chips === void 0 ? void 0 : chips.length) && item.type === 'link') {
      onChipClick === null || onChipClick === void 0 ? void 0 : onChipClick(item, index);
    }
  };
  const clickLinkItem = (item, index, e) => {
    if (onLinkClick) {
      e.preventDefault();
      onLinkClick(item, index);
    }
  };
  return withNativeProps(props, React.createElement("div", {
    className: classNames(classPrefix)
  }, label && React.createElement("div", {
    className: `${classPrefix}-label`
  }, React.createElement(Divider, null, label)), !!(links === null || links === void 0 ? void 0 : links.length) && React.createElement("div", {
    className: `${classPrefix}-links`
  }, links.map((link, index) => React.createElement(React.Fragment, {
    key: index
  }, React.createElement("a", {
    href: link.href,
    rel: 'noopener noreferrer',
    onClick: event => clickLinkItem(link, index, event)
  }, link.text), index !== links.length - 1 && React.createElement(Divider, {
    direction: 'vertical'
  })))), content && React.createElement("div", {
    className: `${classPrefix}-content`
  }, content), chips && chips.length > 0 && React.createElement("div", {
    className: `${classPrefix}-chips`
  }, chips.map((chip, index) => React.createElement("div", {
    key: index,
    onClick: () => clickChipItem(chip, index),
    className: classNames(`${classPrefix}-chip`, {
      [`${classPrefix}-chip-link`]: chip.type === 'link'
    })
  }, chip.text)))));
};