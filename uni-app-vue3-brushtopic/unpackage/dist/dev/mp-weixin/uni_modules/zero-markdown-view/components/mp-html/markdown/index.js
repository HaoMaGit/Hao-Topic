"use strict";
const uni_modules_zeroMarkdownView_components_mpHtml_markdown_marked_min = require("./marked.min.js");
let index = 0;
function Markdown(vm) {
  this.vm = vm;
  vm._ids = {};
}
Markdown.prototype.onUpdate = function(content) {
  if (this.vm.markdown) {
    return uni_modules_zeroMarkdownView_components_mpHtml_markdown_marked_min.marked(content);
  }
};
Markdown.prototype.onParse = function(node, vm) {
  if (vm.options.markdown) {
    if (vm.options.useAnchor && node.attrs && /[\u4e00-\u9fa5]/.test(node.attrs.id)) {
      const id = "t" + index++;
      this.vm._ids[node.attrs.id] = id;
      node.attrs.id = id;
    }
    if (node.name === "p" || node.name === "table" || node.name === "tr" || node.name === "th" || node.name === "td" || node.name === "blockquote" || node.name === "pre" || node.name === "code") {
      node.attrs.class = `md-${node.name} ${node.attrs.class || ""}`;
    }
  }
};
exports.Markdown = Markdown;
//# sourceMappingURL=../../../../../../.sourcemap/mp-weixin/uni_modules/zero-markdown-view/components/mp-html/markdown/index.js.map
