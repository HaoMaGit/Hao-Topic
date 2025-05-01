import { useIsomorphicLayoutEffect, useMemoizedFn } from 'ahooks';
export function useResizeEffect(effect, targetRef) {
  const fn = useMemoizedFn(effect);
  useIsomorphicLayoutEffect(() => {
    const target = targetRef.current;
    if (!target) return;
    if (window.ResizeObserver) {
      let animationFrame;
      const observer = new ResizeObserver(() => {
        animationFrame = window.requestAnimationFrame(() => fn(target));
      });
      observer.observe(target);
      return () => {
        window.cancelAnimationFrame(animationFrame);
        observer.disconnect();
      };
    } else {
      fn(target);
    }
  }, [targetRef]);
}