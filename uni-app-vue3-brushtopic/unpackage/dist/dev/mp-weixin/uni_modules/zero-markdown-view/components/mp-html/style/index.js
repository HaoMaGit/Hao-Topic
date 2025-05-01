"use strict";
const uni_modules_zeroMarkdownView_components_mpHtml_style_parser = require("./parser.js");
function Style() {
  this.styles = [];
}
Style.prototype.onParse = function(node, vm) {
  if (node.name === "style" && node.children.length && node.children[0].type === "text") {
    this.styles = this.styles.concat(new uni_modules_zeroMarkdownView_components_mpHtml_style_parser.Parser().parse(node.children[0].text));
  } else if (node.name) {
    let matched = ["", "", "", ""];
    for (let i = 0, len = this.styles.length; i < len; i++) {
      const item = this.styles[i];
      let res = match(node, item.key || item.list[item.list.length - 1]);
      let j;
      if (res) {
        if (!item.key) {
          j = item.list.length - 2;
          for (let k = vm.stack.length; j >= 0 && k--; ) {
            if (item.list[j] === ">") {
              if (j < 1 || j > item.list.length - 2)
                break;
              if (match(vm.stack[k], item.list[j - 1])) {
                j -= 2;
              } else {
                j++;
              }
            } else if (match(vm.stack[k], item.list[j])) {
              j--;
            }
          }
          res = 4;
        }
        if (item.key || j < 0) {
          if (item.pseudo && node.children) {
            let text;
            item.style = item.style.replace(/content:([^;]+)/, (_, $1) => {
              text = $1.replace(/['"]/g, "").replace(/attr\((.+?)\)/, (_2, $12) => node.attrs[$12.trim()] || "").replace(/\\(\w{4})/, (_2, $12) => String.fromCharCode(parseInt($12, 16)));
              return "";
            });
            const pseudo = {
              name: "span",
              attrs: {
                style: item.style
              },
              children: [{
                type: "text",
                text
              }]
            };
            if (item.pseudo === "before") {
              node.children.unshift(pseudo);
            } else {
              node.children.push(pseudo);
            }
          } else {
            matched[res - 1] += item.style + (item.style[item.style.length - 1] === ";" ? "" : ";");
          }
        }
      }
    }
    matched = matched.join("");
    if (matched.length > 2) {
      node.attrs.style = matched + (node.attrs.style || "");
    }
  }
};
function match(node, keys) {
  function matchItem(key) {
    if (key[0] === "#") {
      if (node.attrs.id && node.attrs.id.trim() === key.substr(1))
        return 3;
    } else if (key[0] === ".") {
      key = key.substr(1);
      const selectors = (node.attrs.class || "").split(" ");
      for (let i = 0; i < selectors.length; i++) {
        if (selectors[i].trim() === key)
          return 2;
      }
    } else if (node.name === key) {
      return 1;
    }
    return 0;
  }
  if (keys instanceof Array) {
    let res = 0;
    for (let j = 0; j < keys.length; j++) {
      const tmp = matchItem(keys[j]);
      if (!tmp)
        return 0;
      if (tmp > res) {
        res = tmp;
      }
    }
    return res;
  }
  return matchItem(keys);
}
exports.Style = Style;
//# sourceMappingURL=../../../../../../.sourcemap/mp-weixin/uni_modules/zero-markdown-view/components/mp-html/style/index.js.map
