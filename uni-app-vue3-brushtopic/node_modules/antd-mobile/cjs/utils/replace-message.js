"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.replaceMessage = replaceMessage;
// 移植自 field-form https://github.com/react-component/field-form/blob/master/src/utils/validateUtil.ts#L21
function replaceMessage(template, kv) {
  return template.replace(/\$\{\w+\}/g, str => {
    const key = str.slice(2, -1);
    return kv[key];
  });
}