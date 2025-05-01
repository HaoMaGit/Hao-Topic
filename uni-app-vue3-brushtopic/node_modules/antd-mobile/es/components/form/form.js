import { __rest } from "tslib";
import React, { forwardRef, useMemo } from 'react';
import classNames from 'classnames';
import List from '../list';
import RcForm from 'rc-field-form';
import { defaultFormContext, FormContext } from './context';
import { mergeProps } from '../../utils/with-default-props';
import { Header } from './header';
import { useConfig } from '../config-provider';
import merge from 'deepmerge';
import { FormArray } from './form-array';
import { traverseReactNode } from '../../utils/traverse-react-node';
const classPrefix = 'adm-form';
const defaultProps = defaultFormContext;
export const Form = forwardRef((p, ref) => {
  const props = mergeProps(defaultProps, p);
  const {
      className,
      style,
      hasFeedback,
      children,
      layout,
      footer,
      mode,
      disabled,
      requiredMarkStyle
    } = props,
    formProps = __rest(props, ["className", "style", "hasFeedback", "children", "layout", "footer", "mode", "disabled", "requiredMarkStyle"]);
  const {
    locale
  } = useConfig();
  const validateMessages = useMemo(() => merge(locale.Form.defaultValidateMessages, formProps.validateMessages || {}), [locale.Form.defaultValidateMessages, formProps.validateMessages]);
  const lists = [];
  let currentHeader = null;
  let items = [];
  let count = 0;
  function collect() {
    if (items.length === 0) return;
    count += 1;
    lists.push(React.createElement(List, {
      header: currentHeader,
      key: count,
      mode: mode
    }, items));
    items = [];
  }
  traverseReactNode(props.children, child => {
    if (React.isValidElement(child)) {
      if (child.type === Header) {
        collect();
        currentHeader = child.props.children;
        return;
      }
      if (child.type === FormArray) {
        collect();
        lists.push(child);
        return;
      }
    }
    items.push(child);
  });
  collect();
  return React.createElement(RcForm, Object.assign({
    className: classNames(classPrefix, className),
    style: style,
    ref: ref
  }, formProps, {
    validateMessages: validateMessages
  }), React.createElement(FormContext.Provider, {
    value: {
      name: formProps.name,
      hasFeedback,
      layout,
      requiredMarkStyle,
      disabled
    }
  }, lists), footer && React.createElement("div", {
    className: `${classPrefix}-footer`
  }, footer));
});