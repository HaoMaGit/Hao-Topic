export const create = () => {
  return [1, 0, 0, 1, 0, 0];
};
export const getTranslateX = m => {
  return m[4];
};
export const getTranslateY = m => {
  return m[5];
};
export const getScaleX = m => {
  return m[0];
};
export const getScaleY = m => {
  return m[3];
};
export const translate = (m, x, y) => {
  return multiply([1, 0, 0, 1, x, y], m);
};
export const scale = (m, scaleX, scaleY = scaleX) => {
  return multiply([scaleX, 0, 0, scaleY, 0, 0], m);
};
export const apply = (m, [ox, oy]) => {
  return [m[0] * ox + m[2] * oy + m[4], m[1] * ox + m[3] * oy + m[5]];
};
export const multiply = (m1, m2) => {
  return [m1[0] * m2[0] + m1[2] * m2[1], m1[1] * m2[0] + m1[3] * m2[1], m1[0] * m2[2] + m1[2] * m2[3], m1[1] * m2[2] + m1[3] * m2[3], m1[0] * m2[4] + m1[2] * m2[5] + m1[4], m1[1] * m2[4] + m1[3] * m2[5] + m1[5]];
};