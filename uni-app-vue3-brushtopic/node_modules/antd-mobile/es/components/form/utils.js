import { isMemo, isFragment } from 'react-is';
export function toArray(candidate) {
  if (candidate === undefined || candidate === false) return [];
  return Array.isArray(candidate) ? candidate : [candidate];
}
// eslint-disable-next-line @typescript-eslint/ban-types
function shouldConstruct(Component) {
  const prototype = Component.prototype;
  return !!(prototype && prototype.isReactComponent);
}
// https://github.com/facebook/react/blob/ce13860281f833de8a3296b7a3dad9caced102e9/packages/react-reconciler/src/ReactFiber.new.js#L225
function isSimpleFunctionComponent(type) {
  return typeof type === 'function' && !shouldConstruct(type) && type.defaultProps === undefined;
}
export function isSafeSetRefComponent(component) {
  if (isFragment(component)) return false;
  if (isMemo(component)) return isSafeSetRefComponent(component.type);
  return !isSimpleFunctionComponent(component.type);
}