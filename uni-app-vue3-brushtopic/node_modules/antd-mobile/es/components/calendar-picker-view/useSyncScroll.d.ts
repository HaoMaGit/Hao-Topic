import type { Dayjs } from 'dayjs';
export default function useSyncScroll(current: Dayjs, visible: boolean, bodyRef: React.RefObject<HTMLDivElement>): (date: Dayjs) => void;
