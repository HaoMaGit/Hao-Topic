// 移植自 field-form https://github.com/react-component/field-form/blob/master/src/utils/validateUtil.ts#L21
export function replaceMessage(template, kv) {
  return template.replace(/\$\{\w+\}/g, str => {
    const key = str.slice(2, -1);
    return kv[key];
  });
}