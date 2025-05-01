import dayjs from 'dayjs';
export function convertValueToRange(selectionMode, value) {
  if (selectionMode === undefined || value === null) return null;
  if (Array.isArray(value)) {
    return value;
  }
  return [value, value];
}
export function convertPageToDayjs(page) {
  return dayjs().year(page.year).month(page.month - 1).date(1);
}