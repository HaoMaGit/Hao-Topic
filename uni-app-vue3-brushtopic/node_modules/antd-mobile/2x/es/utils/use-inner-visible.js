import { useState } from 'react';
import { useIsomorphicLayoutEffect } from 'ahooks';
export function useInnerVisible(outerVisible) {
  const [innerVisible, setInnerVisible] = useState(outerVisible);
  useIsomorphicLayoutEffect(() => {
    setInnerVisible(outerVisible);
  }, [outerVisible]);
  return innerVisible;
}