import { DownFill } from 'antd-mobile-icons';
import classNames from 'classnames';
import React from 'react';
import { withNativeProps } from '../../utils/native-props';
import { useShouldRender } from '../../utils/should-render';
import { mergeProp, mergeProps } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
import { IconContext } from './context';
const classPrefix = `adm-dropdown-item`;
const Item = props => {
  const {
    dropdown: componentConfig = {}
  } = useConfig();
  const mergedProps = mergeProps(componentConfig, props);
  const {
    active,
    highlight,
    onClick,
    title
  } = mergedProps;
  const cls = classNames(classPrefix, {
    [`${classPrefix}-active`]: active,
    [`${classPrefix}-highlight`]: highlight !== null && highlight !== void 0 ? highlight : active
  });
  const contextArrowIcon = React.useContext(IconContext);
  const mergedArrowIcon = mergeProp(React.createElement(DownFill, null), contextArrowIcon, mergedProps.arrow, mergedProps.arrowIcon);
  return withNativeProps(props, React.createElement("div", {
    className: cls,
    onClick: onClick
  }, React.createElement("div", {
    className: `${classPrefix}-title`
  }, React.createElement("span", {
    className: `${classPrefix}-title-text`
  }, title), React.createElement("span", {
    className: classNames(`${classPrefix}-title-arrow`, {
      [`${classPrefix}-title-arrow-active`]: active
    })
  }, mergedArrowIcon))));
};
export default Item;
export const ItemChildrenWrap = props => {
  const {
    active = false
  } = props;
  const shouldRender = useShouldRender(active, props.forceRender, props.destroyOnClose);
  const cls = classNames(`${classPrefix}-content`, {
    [`${classPrefix}-content-hidden`]: !active
  });
  return shouldRender ? React.createElement("div", {
    className: cls,
    onClick: props.onClick
  }, props.children) : null;
};