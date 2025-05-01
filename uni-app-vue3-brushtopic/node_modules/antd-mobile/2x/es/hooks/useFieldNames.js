import { useMemo } from 'react';
export const useFieldNames = (fieldNames = {}) => {
  const fields = useMemo(() => {
    const {
      label = 'label',
      value = 'value',
      disabled = 'disabled',
      children = 'children'
    } = fieldNames;
    return [label, value, children, disabled];
  }, [JSON.stringify(fieldNames)]);
  return fields;
};