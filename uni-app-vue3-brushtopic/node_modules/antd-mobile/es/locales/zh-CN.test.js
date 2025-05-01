import { base } from './base';
import zhCN from './zh-CN';
test('zh-CN', () => {
  function compareKeys(a, b) {
    return !Object.keys(a).some(key => {
      if (typeof b[key] === 'string') {
        return false;
      } else if (b[key]) {
        return !compareKeys(a[key], b[key]);
      } else {
        return true;
      }
    });
  }
  expect(compareKeys(base, zhCN)).toBeTruthy();
});