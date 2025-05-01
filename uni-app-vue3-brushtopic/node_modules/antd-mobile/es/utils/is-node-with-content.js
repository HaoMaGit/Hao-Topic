/**
 * Check if the `node` is visible Node (not null, undefined, or false)
 */
export function isNodeWithContent(node) {
  return node !== undefined && node !== null && node !== false;
}