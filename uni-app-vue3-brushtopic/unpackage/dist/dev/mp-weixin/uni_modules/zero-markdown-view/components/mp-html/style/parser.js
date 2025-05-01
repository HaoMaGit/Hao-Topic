"use strict";
const blank = {
  " ": true,
  "\n": true,
  "	": true,
  "\r": true,
  "\f": true
};
function Parser() {
  this.styles = [];
  this.selectors = [];
}
Parser.prototype.parse = function(content) {
  new Lexer(this).parse(content);
  return this.styles;
};
Parser.prototype.onSelector = function(name) {
  if (name.includes("[") || name.includes("*") || name.includes("@"))
    return;
  const selector = {};
  if (name.includes(":")) {
    const info = name.split(":");
    const pseudo = info.pop();
    if (pseudo === "before" || pseudo === "after") {
      selector.pseudo = pseudo;
      name = info[0];
    } else
      return;
  }
  function splitItem(str) {
    const arr = [];
    let i, start;
    for (i = 1, start = 0; i < str.length; i++) {
      if (str[i] === "." || str[i] === "#") {
        arr.push(str.substring(start, i));
        start = i;
      }
    }
    if (!arr.length) {
      return str;
    } else {
      arr.push(str.substring(start, i));
      return arr;
    }
  }
  if (name.includes(" ")) {
    selector.list = [];
    const list = name.split(" ");
    for (let i = 0; i < list.length; i++) {
      if (list[i].length) {
        const arr = list[i].split(">");
        for (let j = 0; j < arr.length; j++) {
          selector.list.push(splitItem(arr[j]));
          if (j < arr.length - 1) {
            selector.list.push(">");
          }
        }
      }
    }
  } else {
    selector.key = splitItem(name);
  }
  this.selectors.push(selector);
};
Parser.prototype.onContent = function(content) {
  for (let i = 0; i < this.selectors.length; i++) {
    this.selectors[i].style = content;
  }
  this.styles = this.styles.concat(this.selectors);
  this.selectors = [];
};
function Lexer(handler) {
  this.selector = "";
  this.style = "";
  this.handler = handler;
}
Lexer.prototype.parse = function(content) {
  this.i = 0;
  this.content = content;
  this.state = this.blank;
  for (let len = content.length; this.i < len; this.i++) {
    this.state(content[this.i]);
  }
};
Lexer.prototype.comment = function() {
  this.i = this.content.indexOf("*/", this.i) + 1;
  if (!this.i) {
    this.i = this.content.length;
  }
};
Lexer.prototype.blank = function(c) {
  if (!blank[c]) {
    if (c === "/" && this.content[this.i + 1] === "*") {
      this.comment();
      return;
    }
    this.selector += c;
    this.state = this.name;
  }
};
Lexer.prototype.name = function(c) {
  if (c === "/" && this.content[this.i + 1] === "*") {
    this.comment();
    return;
  }
  if (c === "{" || c === "," || c === ";") {
    this.handler.onSelector(this.selector.trimEnd());
    this.selector = "";
    if (c !== "{") {
      while (blank[this.content[++this.i]])
        ;
    }
    if (this.content[this.i] === "{") {
      this.floor = 1;
      this.state = this.val;
    } else {
      this.selector += this.content[this.i];
    }
  } else if (blank[c]) {
    this.selector += " ";
  } else {
    this.selector += c;
  }
};
Lexer.prototype.val = function(c) {
  if (c === "/" && this.content[this.i + 1] === "*") {
    this.comment();
    return;
  }
  if (c === "{") {
    this.floor++;
  } else if (c === "}") {
    this.floor--;
    if (!this.floor) {
      this.handler.onContent(this.style);
      this.style = "";
      this.state = this.blank;
      return;
    }
  }
  this.style += c;
};
exports.Parser = Parser;
//# sourceMappingURL=../../../../../../.sourcemap/mp-weixin/uni_modules/zero-markdown-view/components/mp-html/style/parser.js.map
