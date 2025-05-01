import { useMemo } from 'react';
import isEqual from 'react-fast-compare';
import memoize from 'nano-memoize';
export function useCascaderValueExtend(options, fieldNames) {
  const {
    valueName,
    childrenName
  } = fieldNames;
  const generateItems = useMemo(() => {
    return memoize(val => {
      const ret = [];
      let currentOptions = options;
      for (const v of val) {
        const target = currentOptions.find(option => option[valueName] === v);
        if (!target) {
          break;
        }
        ret.push(target);
        if (!target[childrenName]) break;
        currentOptions = target[childrenName];
      }
      return ret;
    }, {
      equals: isEqual
    });
  }, [options]);
  const generateIsLeaf = useMemo(() => {
    return memoize(val => {
      const children = val.reduce((currentOptions, v) => {
        var _a;
        return ((_a = currentOptions.find(option => option[valueName] === v)) === null || _a === void 0 ? void 0 : _a[childrenName]) || [];
      }, options);
      return children.length === 0;
    }, {
      equals: isEqual
    });
  }, [options]);
  function generateValueExtend(val) {
    return {
      get items() {
        return generateItems(val);
      },
      get isLeaf() {
        return generateIsLeaf(val);
      }
    };
  }
  return generateValueExtend;
}