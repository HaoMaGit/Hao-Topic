import { useMemoizedFn } from 'ahooks';
import { useEffect } from 'react';
export function observe(element, options, callback) {
  if (element && typeof MutationObserver !== 'undefined') {
    let observer = new MutationObserver(() => {
      callback();
    });
    observer.observe(element, options);
    // Return cleanup function
    return () => {
      if (observer) {
        observer.disconnect();
        observer = null;
      }
    };
  }
  return () => {};
}
export function useMutationEffect(effect, targetRef, options) {
  const fn = useMemoizedFn(effect);
  useEffect(() => {
    const cleanup = observe(targetRef.current, options, fn);
    return cleanup;
  }, [targetRef]);
}