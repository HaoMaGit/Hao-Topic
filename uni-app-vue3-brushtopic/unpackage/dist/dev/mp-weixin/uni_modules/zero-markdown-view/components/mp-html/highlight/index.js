"use strict";
const uni_modules_zeroMarkdownView_components_mpHtml_highlight_prism_min = require("./prism.min.js");
const uni_modules_zeroMarkdownView_components_mpHtml_parser = require("../parser.js");
function Highlight(vm) {
  this.vm = vm;
}
Highlight.prototype.onParse = function(node, vm) {
  if (node.name === "pre") {
    if (vm.options.editable) {
      node.attrs.class = (node.attrs.class || "") + " hl-pre";
      return;
    }
    let i;
    for (i = node.children.length; i--; ) {
      if (node.children[i].name === "code")
        break;
    }
    if (i === -1)
      return;
    const code = node.children[i];
    let className = code.attrs.class + " " + node.attrs.class;
    i = className.indexOf("language-");
    if (i === -1) {
      i = className.indexOf("lang-");
      if (i === -1) {
        className = "language-text";
        i = 9;
      } else {
        i += 5;
      }
    } else {
      i += 9;
    }
    let j;
    for (j = i; j < className.length; j++) {
      if (className[j] === " ")
        break;
    }
    const lang = className.substring(i, j);
    if (code.children.length) {
      const text = this.vm.getText(code.children).replace(/&amp;/g, "&");
      if (!text)
        return;
      if (node.c) {
        node.c = void 0;
      }
      if (uni_modules_zeroMarkdownView_components_mpHtml_highlight_prism_min.Prism.languages[lang]) {
        code.children = new uni_modules_zeroMarkdownView_components_mpHtml_parser.Parser(this.vm).parse(
          // 加一层 pre 保留空白符
          "<pre>" + uni_modules_zeroMarkdownView_components_mpHtml_highlight_prism_min.Prism.highlight(text, uni_modules_zeroMarkdownView_components_mpHtml_highlight_prism_min.Prism.languages[lang], lang).replace(/token /g, "hl-") + "</pre>"
        )[0].children;
      }
      node.attrs.class = "hl-pre";
      code.attrs.class = "hl-code";
      code.attrs.style = "display:block;overflow: auto;";
      {
        node.children.push({
          name: "div",
          attrs: {
            class: "hl-language",
            style: "user-select:none;position:absolute;top:0;right:2px;font-size:10px;"
          },
          children: [{
            type: "text",
            text: lang
          }]
        });
      }
      {
        node.attrs.style += (node.attrs.style || "") + ";user-select:none;";
        node.attrs["data-content"] = text;
        node.children.push({
          name: "div",
          attrs: {
            class: "hl-copy",
            style: "user-select:none;position:absolute;top:0;right:3px;font-size:10px;"
          }
          // children: [{
          //   type: 'text',
          //   text: '复制'
          // }]
        });
        vm.expose();
      }
    }
  }
};
exports.Highlight = Highlight;
//# sourceMappingURL=../../../../../../.sourcemap/mp-weixin/uni_modules/zero-markdown-view/components/mp-html/highlight/index.js.map
