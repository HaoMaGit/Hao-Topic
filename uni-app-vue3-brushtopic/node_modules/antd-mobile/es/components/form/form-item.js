import { __rest } from "tslib";
import { QuestionCircleOutline } from 'antd-mobile-icons';
import classNames from 'classnames';
import { Field } from 'rc-field-form';
import FieldContext from 'rc-field-form/lib/FieldContext';
import React, { useCallback, useContext, useRef, useState } from 'react';
import { devWarning } from '../../utils/dev-log';
import { withNativeProps } from '../../utils/native-props';
import { undefinedFallback } from '../../utils/undefined-fallback';
import { mergeProps } from '../../utils/with-default-props';
import { useConfig } from '../config-provider';
import List from '../list';
import Popover from '../popover';
import { FormContext, NoStyleItemContext } from './context';
import { isSafeSetRefComponent, toArray } from './utils';
const NAME_SPLIT = '__SPLIT__';
const classPrefix = `adm-form-item`;
const MemoInput = React.memo(({
  children
}) => children, (prev, next) => prev.value === next.value && prev.update === next.update);
const FormItemLayout = props => {
  var _a;
  const {
    locale,
    form: componentConfig = {}
  } = useConfig();
  const {
    style,
    extra,
    label,
    help,
    helpIcon,
    required,
    children,
    htmlFor,
    hidden,
    arrow,
    arrowIcon,
    childElementPosition = 'normal'
  } = mergeProps(componentConfig, props);
  const context = useContext(FormContext);
  const hasFeedback = props.hasFeedback !== undefined ? props.hasFeedback : context.hasFeedback;
  const layout = props.layout || context.layout;
  const disabled = (_a = props.disabled) !== null && _a !== void 0 ? _a : context.disabled;
  const requiredMark = (() => {
    const {
      requiredMarkStyle
    } = context;
    switch (requiredMarkStyle) {
      case 'asterisk':
        return required && React.createElement("span", {
          className: `${classPrefix}-required-asterisk`
        }, "*");
      case 'text-required':
        return required && React.createElement("span", {
          className: `${classPrefix}-required-text`
        }, "(", locale.Form.required, ")");
      case 'text-optional':
        return !required && React.createElement("span", {
          className: `${classPrefix}-required-text`
        }, "(", locale.Form.optional, ")");
      case 'none':
        return null;
      default:
        return null;
    }
  })();
  const labelElement = !!label && React.createElement("label", {
    className: `${classPrefix}-label`,
    htmlFor: htmlFor
  }, label, requiredMark, help && React.createElement(Popover, {
    content: help,
    mode: 'dark',
    trigger: 'click'
  }, React.createElement("span", {
    className: `${classPrefix}-label-help`,
    onClick: e => {
      e.stopPropagation();
      e.preventDefault();
    }
  }, helpIcon || React.createElement(QuestionCircleOutline, null))));
  const description = (!!props.description || hasFeedback) && React.createElement(React.Fragment, null, props.description, hasFeedback && React.createElement(React.Fragment, null, props.errors.map((error, index) => React.createElement("div", {
    key: `error-${index}`,
    className: `${classPrefix}-feedback-error`
  }, error)), props.warnings.map((warning, index) => React.createElement("div", {
    key: `warning-${index}`,
    className: `${classPrefix}-feedback-warning`
  }, warning))));
  return withNativeProps(props, React.createElement(List.Item, {
    style: style,
    title: layout === 'vertical' && labelElement,
    prefix: layout === 'horizontal' && labelElement,
    extra: extra,
    description: description,
    className: classNames(classPrefix, `${classPrefix}-${layout}`, {
      [`${classPrefix}-hidden`]: hidden,
      [`${classPrefix}-has-error`]: props.errors.length
    }),
    disabled: disabled,
    onClick: props.onClick,
    clickable: props.clickable,
    arrowIcon: arrowIcon || arrow
  }, React.createElement("div", {
    className: classNames(`${classPrefix}-child`, `${classPrefix}-child-position-${childElementPosition}`)
  }, React.createElement("div", {
    className: classNames(`${classPrefix}-child-inner`)
  }, children))));
};
export const FormItem = props => {
  const {
      // 样式相关
      style,
      // FormItem 相关
      label,
      help,
      helpIcon,
      extra,
      hasFeedback,
      name,
      required,
      noStyle,
      hidden,
      layout,
      childElementPosition,
      description,
      // Field 相关
      disabled,
      rules,
      children,
      messageVariables,
      trigger = 'onChange',
      validateTrigger = trigger,
      onClick,
      shouldUpdate,
      dependencies,
      clickable,
      arrow,
      arrowIcon
    } = props,
    fieldProps = __rest(props, ["style", "label", "help", "helpIcon", "extra", "hasFeedback", "name", "required", "noStyle", "hidden", "layout", "childElementPosition", "description", "disabled", "rules", "children", "messageVariables", "trigger", "validateTrigger", "onClick", "shouldUpdate", "dependencies", "clickable", "arrow", "arrowIcon"]);
  const {
    name: formName
  } = useContext(FormContext);
  const {
    validateTrigger: contextValidateTrigger
  } = useContext(FieldContext);
  const mergedValidateTrigger = undefinedFallback(validateTrigger, contextValidateTrigger, trigger);
  const widgetRef = useRef(null);
  const updateRef = useRef(0);
  updateRef.current += 1;
  const [subMetas, setSubMetas] = useState({});
  const onSubMetaChange = useCallback((subMeta, namePath) => {
    setSubMetas(prevSubMetas => {
      const nextSubMetas = Object.assign({}, prevSubMetas);
      const nameKey = namePath.join(NAME_SPLIT);
      if (subMeta.destroy) {
        delete nextSubMetas[nameKey];
      } else {
        nextSubMetas[nameKey] = subMeta;
      }
      return nextSubMetas;
    });
  }, [setSubMetas]);
  function renderLayout(baseChildren, fieldId, meta, isRequired) {
    var _a, _b;
    if (noStyle && !hidden) {
      return baseChildren;
    }
    const curErrors = (_a = meta === null || meta === void 0 ? void 0 : meta.errors) !== null && _a !== void 0 ? _a : [];
    const errors = Object.keys(subMetas).reduce((subErrors, key) => {
      var _a, _b;
      const errors = (_b = (_a = subMetas[key]) === null || _a === void 0 ? void 0 : _a.errors) !== null && _b !== void 0 ? _b : [];
      if (errors.length) {
        subErrors = [...subErrors, ...errors];
      }
      return subErrors;
    }, curErrors);
    const curWarnings = (_b = meta === null || meta === void 0 ? void 0 : meta.warnings) !== null && _b !== void 0 ? _b : [];
    const warnings = Object.keys(subMetas).reduce((subWarnings, key) => {
      var _a, _b;
      const warnings = (_b = (_a = subMetas[key]) === null || _a === void 0 ? void 0 : _a.warnings) !== null && _b !== void 0 ? _b : [];
      if (warnings.length) {
        subWarnings = [...subWarnings, ...warnings];
      }
      return subWarnings;
    }, curWarnings);
    return withNativeProps(props, React.createElement(FormItemLayout, {
      style: style,
      label: label,
      extra: extra,
      help: help,
      helpIcon: helpIcon,
      description: description,
      required: isRequired,
      disabled: disabled,
      hasFeedback: hasFeedback,
      htmlFor: fieldId,
      errors: errors,
      warnings: warnings,
      onClick: onClick && (e => onClick(e, widgetRef)),
      hidden: hidden,
      layout: layout,
      childElementPosition: childElementPosition,
      clickable: clickable,
      arrow: arrow,
      arrowIcon: arrowIcon
    }, React.createElement(NoStyleItemContext.Provider, {
      value: onSubMetaChange
    }, baseChildren)));
  }
  const isRenderProps = typeof children === 'function';
  if (!name && !isRenderProps && !props.dependencies) {
    return renderLayout(children);
  }
  let Variables = {};
  Variables.label = typeof label === 'string' ? label : '';
  if (messageVariables) {
    Variables = Object.assign(Object.assign({}, Variables), messageVariables);
  }
  const notifyParentMetaChange = useContext(NoStyleItemContext);
  const onMetaChange = meta => {
    if (noStyle && notifyParentMetaChange) {
      const namePath = meta.name;
      notifyParentMetaChange(meta, namePath);
    }
  };
  return React.createElement(Field, Object.assign({}, fieldProps, {
    name: name,
    shouldUpdate: shouldUpdate,
    dependencies: dependencies,
    rules: rules,
    trigger: trigger,
    validateTrigger: mergedValidateTrigger,
    onMetaChange: onMetaChange,
    messageVariables: Variables
  }), (control, meta, context) => {
    let childNode = null;
    const isRequired = required !== undefined ? required : rules && rules.some(rule => !!(rule && typeof rule === 'object' && rule.required));
    const nameList = toArray(name).length && meta ? meta.name : [];
    const fieldId = (nameList.length > 0 && formName ? [formName, ...nameList] : nameList).join('_');
    if (shouldUpdate && dependencies) {
      devWarning('Form.Item', "`shouldUpdate` and `dependencies` shouldn't be used together.");
    }
    if (isRenderProps) {
      if ((shouldUpdate || dependencies) && !name) {
        childNode = children(context);
      } else {
        if (!(shouldUpdate || dependencies)) {
          devWarning('Form.Item', '`children` of render props only work with `shouldUpdate` or `dependencies`.');
        }
        if (name) {
          devWarning('Form.Item', "Do not use `name` with `children` of render props since it's not a field.");
        }
      }
      // not render props
    } else if (dependencies && !name) {
      devWarning('Form.Item', 'Must set `name` or use render props when `dependencies` is set.');
    } else if (React.isValidElement(children)) {
      if (children.props.defaultValue) {
        devWarning('Form.Item', '`defaultValue` will not work on controlled Field. You should use `initialValues` of Form instead.');
      }
      const childProps = Object.assign(Object.assign({}, children.props), control);
      if (isSafeSetRefComponent(children)) {
        childProps.ref = instance => {
          const originRef = children.ref;
          if (originRef) {
            if (typeof originRef === 'function') {
              originRef(instance);
            }
            if ('current' in originRef) {
              originRef.current = instance;
            }
          }
          widgetRef.current = instance;
        };
      }
      if (!childProps.id) {
        childProps.id = fieldId;
      }
      // We should keep user origin event handler
      const triggers = new Set([...toArray(trigger), ...toArray(mergedValidateTrigger)]);
      triggers.forEach(eventName => {
        childProps[eventName] = (...args) => {
          var _a, _b, _c;
          (_a = control[eventName]) === null || _a === void 0 ? void 0 : _a.call(control, ...args);
          (_c = (_b = children.props)[eventName]) === null || _c === void 0 ? void 0 : _c.call(_b, ...args);
        };
      });
      childNode = React.createElement(MemoInput, {
        value: control[props.valuePropName || 'value'],
        update: updateRef.current
      }, React.cloneElement(children, childProps));
    } else {
      if (name) {
        devWarning('Form.Item', '`name` is only used for validate React element. If you are using Form.Item as layout display, please remove `name` instead.');
      }
      childNode = children;
    }
    return renderLayout(childNode, fieldId, meta, isRequired);
  });
};