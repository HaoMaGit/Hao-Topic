import { useEvent } from 'rc-util';
import { useEffect, useRef } from 'react';
export default function useSyncScroll(current, visible, bodyRef) {
  const rafRef = useRef();
  const clean = () => {
    if (rafRef.current) {
      cancelAnimationFrame(rafRef.current);
    }
  };
  const scrollTo = useEvent(date => {
    clean();
    rafRef.current = requestAnimationFrame(() => {
      if (bodyRef.current) {
        const yearMonth = date.format('YYYY-M');
        const target = bodyRef.current.querySelector(`[data-year-month="${yearMonth}"]`);
        if (target) {
          // Scroll to the top of view
          target.scrollIntoView({
            block: 'start',
            inline: 'nearest'
          });
        }
      }
    });
  });
  useEffect(() => {
    if (visible && current) {
      scrollTo(current);
      return clean;
    }
  }, [current, visible]);
  return scrollTo;
}