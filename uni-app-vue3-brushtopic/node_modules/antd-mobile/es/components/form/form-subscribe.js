import React, { memo, useContext } from 'react';
import { FieldContext, useWatch } from 'rc-field-form';
import { useUpdate } from 'ahooks';
import { useIsomorphicUpdateLayoutEffect } from '../../utils/use-isomorphic-update-layout-effect';
export const FormSubscribe = props => {
  const update = useUpdate();
  const form = useContext(FieldContext);
  const value = form.getFieldsValue(props.to);
  // Memo to avoid useless render
  const childNode = React.useMemo(() => props.children(value, form), [JSON.stringify(value), props.children]);
  return React.createElement(React.Fragment, null, childNode, props.to.map(namePath => React.createElement(Watcher, {
    key: namePath.toString(),
    form: form,
    namePath: namePath,
    onChange: update
  })));
};
export const Watcher = memo(props => {
  const value = useWatch(props.namePath, props.form);
  useIsomorphicUpdateLayoutEffect(() => {
    props.onChange();
  }, [value]);
  return null;
});