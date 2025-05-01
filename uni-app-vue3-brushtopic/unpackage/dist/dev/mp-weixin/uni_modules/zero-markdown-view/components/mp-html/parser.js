"use strict";
const common_vendor = require("../../../../common/vendor.js");
const config = {
  // 信任的标签（保持标签名不变）
  trustTags: makeMap("a,abbr,ad,audio,b,blockquote,br,code,col,colgroup,dd,del,dl,dt,div,em,fieldset,h1,h2,h3,h4,h5,h6,hr,i,img,ins,label,legend,li,ol,p,q,ruby,rt,source,span,strong,sub,sup,table,tbody,td,tfoot,th,thead,tr,title,ul,video"),
  // 块级标签（转为 div，其他的非信任标签转为 span）
  blockTags: makeMap("address,article,aside,body,caption,center,cite,footer,header,html,nav,pre,section"),
  // 行内标签
  inlineTags: makeMap("abbr,b,big,code,del,em,i,ins,label,q,small,span,strong,sub,sup"),
  // 要移除的标签
  ignoreTags: makeMap("area,base,canvas,embed,frame,head,iframe,input,link,map,meta,param,rp,script,source,style,textarea,title,track,wbr"),
  // 自闭合的标签
  voidTags: makeMap("area,base,br,col,circle,ellipse,embed,frame,hr,img,input,line,link,meta,param,path,polygon,rect,source,track,use,wbr"),
  // html 实体
  entities: {
    lt: "<",
    gt: ">",
    quot: '"',
    apos: "'",
    ensp: " ",
    emsp: " ",
    nbsp: " ",
    semi: ";",
    ndash: "–",
    mdash: "—",
    middot: "·",
    lsquo: "‘",
    rsquo: "’",
    ldquo: "“",
    rdquo: "”",
    bull: "•",
    hellip: "…",
    larr: "←",
    uarr: "↑",
    rarr: "→",
    darr: "↓"
  },
  // 默认的标签样式
  tagStyle: {
    address: "font-style:italic",
    big: "display:inline;font-size:1.2em",
    caption: "display:table-caption;text-align:center",
    center: "text-align:center",
    cite: "font-style:italic",
    dd: "margin-left:40px",
    mark: "background-color:yellow",
    pre: "font-family:monospace;white-space:pre",
    s: "text-decoration:line-through",
    small: "display:inline;font-size:0.8em",
    strike: "text-decoration:line-through",
    u: "text-decoration:underline"
  },
  // svg 大小写对照表
  svgDict: {
    animatetransform: "animateTransform",
    lineargradient: "linearGradient",
    viewbox: "viewBox",
    attributename: "attributeName",
    repeatcount: "repeatCount",
    repeatdur: "repeatDur"
  }
};
const tagSelector = {};
const {
  windowWidth,
  system
} = common_vendor.index.getSystemInfoSync();
const blankChar = makeMap(" ,\r,\n,	,\f");
let idIndex = 0;
function makeMap(str) {
  const map = /* @__PURE__ */ Object.create(null);
  const list = str.split(",");
  for (let i = list.length; i--; ) {
    map[list[i]] = true;
  }
  return map;
}
function decodeEntity(str, amp) {
  let i = str.indexOf("&");
  while (i !== -1) {
    const j = str.indexOf(";", i + 3);
    let code;
    if (j === -1)
      break;
    if (str[i + 1] === "#") {
      code = parseInt((str[i + 2] === "x" ? "0" : "") + str.substring(i + 2, j));
      if (!isNaN(code)) {
        str = str.substr(0, i) + String.fromCharCode(code) + str.substr(j + 1);
      }
    } else {
      code = str.substring(i + 1, j);
      if (config.entities[code] || code === "amp" && amp) {
        str = str.substr(0, i) + (config.entities[code] || "&") + str.substr(j + 1);
      }
    }
    i = str.indexOf("&", i + 1);
  }
  return str;
}
function mergeNodes(nodes) {
  let i = nodes.length - 1;
  for (let j = i; j >= -1; j--) {
    if (j === -1 || nodes[j].c || !nodes[j].name || nodes[j].name !== "div" && nodes[j].name !== "p" && nodes[j].name[0] !== "h" || (nodes[j].attrs.style || "").includes("inline")) {
      if (i - j >= 5) {
        nodes.splice(j + 1, i - j, {
          name: "div",
          attrs: {},
          children: nodes.slice(j + 1, i + 1)
        });
      }
      i = j - 1;
    }
  }
}
function Parser(vm) {
  this.options = vm || {};
  this.tagStyle = Object.assign({}, config.tagStyle, this.options.tagStyle);
  this.imgList = vm.imgList || [];
  this.imgList._unloadimgs = 0;
  this.plugins = vm.plugins || [];
  this.attrs = /* @__PURE__ */ Object.create(null);
  this.stack = [];
  this.nodes = [];
  this.pre = (this.options.containerStyle || "").includes("white-space") && this.options.containerStyle.includes("pre") ? 2 : 0;
}
Parser.prototype.parse = function(content) {
  for (let i = this.plugins.length; i--; ) {
    if (this.plugins[i].onUpdate) {
      content = this.plugins[i].onUpdate(content, config) || content;
    }
  }
  new Lexer(this).parse(content);
  while (this.stack.length) {
    this.popNode();
  }
  if (this.nodes.length > 50) {
    mergeNodes(this.nodes);
  }
  return this.nodes;
};
Parser.prototype.expose = function() {
  for (let i = this.stack.length; i--; ) {
    const item = this.stack[i];
    if (item.c || item.name === "a" || item.name === "video" || item.name === "audio")
      return;
    item.c = 1;
  }
};
Parser.prototype.hook = function(node) {
  for (let i = this.plugins.length; i--; ) {
    if (this.plugins[i].onParse && this.plugins[i].onParse(node, this) === false) {
      return false;
    }
  }
  return true;
};
Parser.prototype.getUrl = function(url) {
  const domain = this.options.domain;
  if (url[0] === "/") {
    if (url[1] === "/") {
      url = (domain ? domain.split("://")[0] : "http") + ":" + url;
    } else if (domain) {
      url = domain + url;
    }
  } else if (!url.includes("data:") && !url.includes("://")) {
    if (domain) {
      url = domain + "/" + url;
    }
  }
  return url;
};
Parser.prototype.parseStyle = function(node) {
  const attrs = node.attrs;
  const list = (this.tagStyle[node.name] || "").split(";").concat((attrs.style || "").split(";"));
  const styleObj = {};
  let tmp = "";
  if (attrs.id && !this.xml) {
    if (this.options.useAnchor) {
      this.expose();
    } else if (node.name !== "img" && node.name !== "a" && node.name !== "video" && node.name !== "audio") {
      attrs.id = void 0;
    }
  }
  if (attrs.width) {
    styleObj.width = parseFloat(attrs.width) + (attrs.width.includes("%") ? "%" : "px");
    attrs.width = void 0;
  }
  if (attrs.height) {
    styleObj.height = parseFloat(attrs.height) + (attrs.height.includes("%") ? "%" : "px");
    attrs.height = void 0;
  }
  for (let i = 0, len = list.length; i < len; i++) {
    const info = list[i].split(":");
    if (info.length < 2)
      continue;
    const key = info.shift().trim().toLowerCase();
    let value = info.join(":").trim();
    if (value[0] === "-" && value.lastIndexOf("-") > 0 || value.includes("safe")) {
      tmp += `;${key}:${value}`;
    } else if (!styleObj[key] || value.includes("import") || !styleObj[key].includes("import")) {
      if (value.includes("url")) {
        let j = value.indexOf("(") + 1;
        if (j) {
          while (value[j] === '"' || value[j] === "'" || blankChar[value[j]]) {
            j++;
          }
          value = value.substr(0, j) + this.getUrl(value.substr(j));
        }
      } else if (value.includes("rpx")) {
        value = value.replace(/[0-9.]+\s*rpx/g, ($) => parseFloat($) * windowWidth / 750 + "px");
      }
      styleObj[key] = value;
    }
  }
  node.attrs.style = tmp;
  return styleObj;
};
Parser.prototype.onTagName = function(name) {
  this.tagName = this.xml ? name : name.toLowerCase();
  if (this.tagName === "svg") {
    this.xml = (this.xml || 0) + 1;
    config.ignoreTags.style = void 0;
  }
};
Parser.prototype.onAttrName = function(name) {
  name = this.xml ? name : name.toLowerCase();
  if (name.substr(0, 5) === "data-") {
    if (name === "data-src" && !this.attrs.src) {
      this.attrName = "src";
    } else if (this.tagName === "img" || this.tagName === "a") {
      this.attrName = name;
    } else {
      this.attrName = void 0;
    }
  } else {
    this.attrName = name;
    this.attrs[name] = "T";
  }
};
Parser.prototype.onAttrVal = function(val) {
  const name = this.attrName || "";
  if (name === "style" || name === "href") {
    this.attrs[name] = decodeEntity(val, true);
  } else if (name.includes("src")) {
    this.attrs[name] = this.getUrl(decodeEntity(val, true));
  } else if (name) {
    this.attrs[name] = val;
  }
};
Parser.prototype.onOpenTag = function(selfClose) {
  const node = /* @__PURE__ */ Object.create(null);
  node.name = this.tagName;
  node.attrs = this.attrs;
  if (this.options.nodes.length) {
    node.type = "node";
  }
  this.attrs = /* @__PURE__ */ Object.create(null);
  const attrs = node.attrs;
  const parent = this.stack[this.stack.length - 1];
  const siblings = parent ? parent.children : this.nodes;
  const close = this.xml ? selfClose : config.voidTags[node.name];
  if (tagSelector[node.name]) {
    attrs.class = tagSelector[node.name] + (attrs.class ? " " + attrs.class : "");
  }
  if (node.name === "embed") {
    const src = attrs.src || "";
    if (src.includes(".mp4") || src.includes(".3gp") || src.includes(".m3u8") || (attrs.type || "").includes("video")) {
      node.name = "video";
    } else if (src.includes(".mp3") || src.includes(".wav") || src.includes(".aac") || src.includes(".m4a") || (attrs.type || "").includes("audio")) {
      node.name = "audio";
    }
    if (attrs.autostart) {
      attrs.autoplay = "T";
    }
    attrs.controls = "T";
  }
  if (node.name === "video" || node.name === "audio") {
    if (node.name === "video" && !attrs.id) {
      attrs.id = "v" + idIndex++;
    }
    if (!attrs.controls && !attrs.autoplay) {
      attrs.controls = "T";
    }
    node.src = [];
    if (attrs.src) {
      node.src.push(attrs.src);
      attrs.src = void 0;
    }
    this.expose();
  }
  if (close) {
    if (!this.hook(node) || config.ignoreTags[node.name]) {
      if (node.name === "base" && !this.options.domain) {
        this.options.domain = attrs.href;
      } else if (node.name === "source" && parent && (parent.name === "video" || parent.name === "audio") && attrs.src) {
        parent.src.push(attrs.src);
      }
      return;
    }
    const styleObj = this.parseStyle(node);
    if (node.name === "img") {
      if (attrs.src) {
        if (attrs.src.includes("webp")) {
          node.webp = "T";
        }
        if (attrs.src.includes("data:") && !attrs["original-src"]) {
          attrs.ignore = "T";
        }
        if (!attrs.ignore || node.webp || attrs.src.includes("cloud://")) {
          for (let i = this.stack.length; i--; ) {
            const item = this.stack[i];
            if (item.name === "a") {
              node.a = item.attrs;
            }
            if (item.name === "table" && !node.webp && !attrs.src.includes("cloud://")) {
              if (!styleObj.display || styleObj.display.includes("inline")) {
                node.t = "inline-block";
              } else {
                node.t = styleObj.display;
              }
              styleObj.display = void 0;
            }
            const style = item.attrs.style || "";
            if (style.includes("flex:") && !style.includes("flex:0") && !style.includes("flex: 0") && (!styleObj.width || parseInt(styleObj.width) > 100)) {
              styleObj.width = "100% !important";
              styleObj.height = "";
              for (let j = i + 1; j < this.stack.length; j++) {
                this.stack[j].attrs.style = (this.stack[j].attrs.style || "").replace("inline-", "");
              }
            } else if (style.includes("flex") && styleObj.width === "100%") {
              for (let j = i + 1; j < this.stack.length; j++) {
                const style2 = this.stack[j].attrs.style || "";
                if (!style2.includes(";width") && !style2.includes(" width") && style2.indexOf("width") !== 0) {
                  styleObj.width = "";
                  break;
                }
              }
            } else if (style.includes("inline-block")) {
              if (styleObj.width && styleObj.width[styleObj.width.length - 1] === "%") {
                item.attrs.style += ";max-width:" + styleObj.width;
                styleObj.width = "";
              } else {
                item.attrs.style += ";max-width:100%";
              }
            }
            item.c = 1;
          }
          attrs.i = this.imgList.length.toString();
          let src = attrs["original-src"] || attrs.src;
          if (this.imgList.includes(src)) {
            let i = src.indexOf("://");
            if (i !== -1) {
              i += 3;
              let newSrc = src.substr(0, i);
              for (; i < src.length; i++) {
                if (src[i] === "/")
                  break;
                newSrc += Math.random() > 0.5 ? src[i].toUpperCase() : src[i];
              }
              newSrc += src.substr(i);
              src = newSrc;
            }
          }
          this.imgList.push(src);
          if (!node.t) {
            this.imgList._unloadimgs += 1;
          }
        }
      }
      if (styleObj.display === "inline") {
        styleObj.display = "";
      }
      if (attrs.ignore) {
        styleObj["max-width"] = styleObj["max-width"] || "100%";
        attrs.style += ";-webkit-touch-callout:none";
      }
      if (parseInt(styleObj.width) > windowWidth) {
        styleObj.height = void 0;
      }
      if (!isNaN(parseInt(styleObj.width))) {
        node.w = "T";
      }
      if (!isNaN(parseInt(styleObj.height)) && (!styleObj.height.includes("%") || parent && (parent.attrs.style || "").includes("height"))) {
        node.h = "T";
      }
    } else if (node.name === "svg") {
      siblings.push(node);
      this.stack.push(node);
      this.popNode();
      return;
    }
    for (const key in styleObj) {
      if (styleObj[key]) {
        attrs.style += `;${key}:${styleObj[key].replace(" !important", "")}`;
      }
    }
    attrs.style = attrs.style.substr(1) || void 0;
    if (!attrs.style) {
      delete attrs.style;
    }
  } else {
    if ((node.name === "pre" || (attrs.style || "").includes("white-space") && attrs.style.includes("pre")) && this.pre !== 2) {
      this.pre = node.pre = 1;
    }
    node.children = [];
    this.stack.push(node);
  }
  siblings.push(node);
};
Parser.prototype.onCloseTag = function(name) {
  name = this.xml ? name : name.toLowerCase();
  let i;
  for (i = this.stack.length; i--; ) {
    if (this.stack[i].name === name)
      break;
  }
  if (i !== -1) {
    while (this.stack.length > i) {
      this.popNode();
    }
  } else if (name === "p" || name === "br") {
    const siblings = this.stack.length ? this.stack[this.stack.length - 1].children : this.nodes;
    siblings.push({
      name,
      attrs: {
        class: tagSelector[name] || "",
        style: this.tagStyle[name] || ""
      }
    });
  }
};
Parser.prototype.popNode = function() {
  const node = this.stack.pop();
  let attrs = node.attrs;
  const children = node.children;
  const parent = this.stack[this.stack.length - 1];
  const siblings = parent ? parent.children : this.nodes;
  if (!this.hook(node) || config.ignoreTags[node.name]) {
    if (node.name === "title" && children.length && children[0].type === "text" && this.options.setTitle) {
      common_vendor.index.setNavigationBarTitle({
        title: children[0].text
      });
    }
    siblings.pop();
    return;
  }
  if (node.pre && this.pre !== 2) {
    this.pre = node.pre = void 0;
    for (let i = this.stack.length; i--; ) {
      if (this.stack[i].pre) {
        this.pre = 1;
      }
    }
  }
  const styleObj = {};
  if (node.name === "svg") {
    if (this.xml > 1) {
      this.xml--;
      return;
    }
    let src = "";
    const style = attrs.style;
    attrs.style = "";
    attrs.xmlns = "http://www.w3.org/2000/svg";
    (function traversal(node2) {
      if (node2.type === "text") {
        src += node2.text;
        return;
      }
      const name = config.svgDict[node2.name] || node2.name;
      src += "<" + name;
      for (const item in node2.attrs) {
        const val = node2.attrs[item];
        if (val) {
          src += ` ${config.svgDict[item] || item}="${val}"`;
        }
      }
      if (!node2.children) {
        src += "/>";
      } else {
        src += ">";
        for (let i = 0; i < node2.children.length; i++) {
          traversal(node2.children[i]);
        }
        src += "</" + name + ">";
      }
    })(node);
    node.name = "img";
    node.attrs = {
      src: "data:image/svg+xml;utf8," + src.replace(/#/g, "%23"),
      style,
      ignore: "T"
    };
    node.children = void 0;
    this.xml = false;
    config.ignoreTags.style = true;
    return;
  }
  if (attrs.align) {
    if (node.name === "table") {
      if (attrs.align === "center") {
        styleObj["margin-inline-start"] = styleObj["margin-inline-end"] = "auto";
      } else {
        styleObj.float = attrs.align;
      }
    } else {
      styleObj["text-align"] = attrs.align;
    }
    attrs.align = void 0;
  }
  if (attrs.dir) {
    styleObj.direction = attrs.dir;
    attrs.dir = void 0;
  }
  if (node.name === "font") {
    if (attrs.color) {
      styleObj.color = attrs.color;
      attrs.color = void 0;
    }
    if (attrs.face) {
      styleObj["font-family"] = attrs.face;
      attrs.face = void 0;
    }
    if (attrs.size) {
      let size = parseInt(attrs.size);
      if (!isNaN(size)) {
        if (size < 1) {
          size = 1;
        } else if (size > 7) {
          size = 7;
        }
        styleObj["font-size"] = ["x-small", "small", "medium", "large", "x-large", "xx-large", "xxx-large"][size - 1];
      }
      attrs.size = void 0;
    }
  }
  if ((attrs.class || "").includes("align-center")) {
    styleObj["text-align"] = "center";
  }
  Object.assign(styleObj, this.parseStyle(node));
  if (node.name !== "table" && parseInt(styleObj.width) > windowWidth) {
    styleObj["max-width"] = "100%";
    styleObj["box-sizing"] = "border-box";
  }
  if (config.blockTags[node.name]) {
    node.name = "div";
  } else if (!config.trustTags[node.name] && !this.xml) {
    node.name = "span";
  }
  if (node.name === "a" || node.name === "ad") {
    this.expose();
  } else if (node.name === "video") {
    if ((styleObj.height || "").includes("auto")) {
      styleObj.height = void 0;
    }
  } else if ((node.name === "ul" || node.name === "ol") && node.c) {
    const types = {
      a: "lower-alpha",
      A: "upper-alpha",
      i: "lower-roman",
      I: "upper-roman"
    };
    if (types[attrs.type]) {
      attrs.style += ";list-style-type:" + types[attrs.type];
      attrs.type = void 0;
    }
    for (let i = children.length; i--; ) {
      if (children[i].name === "li") {
        children[i].c = 1;
      }
    }
  } else if (node.name === "table") {
    let padding = parseFloat(attrs.cellpadding);
    let spacing = parseFloat(attrs.cellspacing);
    const border = parseFloat(attrs.border);
    const bordercolor = styleObj["border-color"];
    const borderstyle = styleObj["border-style"];
    if (node.c) {
      if (isNaN(padding)) {
        padding = 2;
      }
      if (isNaN(spacing)) {
        spacing = 2;
      }
    }
    if (border) {
      attrs.style += `;border:${border}px ${borderstyle || "solid"} ${bordercolor || "gray"}`;
    }
    if (node.flag && node.c) {
      styleObj.display = "grid";
      if (spacing) {
        styleObj["grid-gap"] = spacing + "px";
        styleObj.padding = spacing + "px";
      } else if (border) {
        attrs.style += ";border-left:0;border-top:0";
      }
      const width = [];
      const trList = [];
      const cells = [];
      const map = {};
      (function traversal(nodes) {
        for (let i = 0; i < nodes.length; i++) {
          if (nodes[i].name === "tr") {
            trList.push(nodes[i]);
          } else {
            traversal(nodes[i].children || []);
          }
        }
      })(children);
      for (let row = 1; row <= trList.length; row++) {
        let col = 1;
        for (let j = 0; j < trList[row - 1].children.length; j++) {
          const td = trList[row - 1].children[j];
          if (td.name === "td" || td.name === "th") {
            while (map[row + "." + col]) {
              col++;
            }
            let style = td.attrs.style || "";
            let start = style.indexOf("width") ? style.indexOf(";width") : 0;
            if (start !== -1) {
              let end = style.indexOf(";", start + 6);
              if (end === -1) {
                end = style.length;
              }
              if (!td.attrs.colspan) {
                width[col] = style.substring(start ? start + 7 : 6, end);
              }
              style = style.substr(0, start) + style.substr(end);
            }
            style += ";display:flex";
            start = style.indexOf("vertical-align");
            if (start !== -1) {
              const val = style.substr(start + 15, 10);
              if (val.includes("middle")) {
                style += ";align-items:center";
              } else if (val.includes("bottom")) {
                style += ";align-items:flex-end";
              }
            } else {
              style += ";align-items:center";
            }
            start = style.indexOf("text-align");
            if (start !== -1) {
              const val = style.substr(start + 11, 10);
              if (val.includes("center")) {
                style += ";justify-content: center";
              } else if (val.includes("right")) {
                style += ";justify-content: right";
              }
            }
            style = (border ? `;border:${border}px ${borderstyle || "solid"} ${bordercolor || "gray"}` + (spacing ? "" : ";border-right:0;border-bottom:0") : "") + (padding ? `;padding:${padding}px` : "") + ";" + style;
            if (td.attrs.colspan) {
              style += `;grid-column-start:${col};grid-column-end:${col + parseInt(td.attrs.colspan)}`;
              if (!td.attrs.rowspan) {
                style += `;grid-row-start:${row};grid-row-end:${row + 1}`;
              }
              col += parseInt(td.attrs.colspan) - 1;
            }
            if (td.attrs.rowspan) {
              style += `;grid-row-start:${row};grid-row-end:${row + parseInt(td.attrs.rowspan)}`;
              if (!td.attrs.colspan) {
                style += `;grid-column-start:${col};grid-column-end:${col + 1}`;
              }
              for (let rowspan = 1; rowspan < td.attrs.rowspan; rowspan++) {
                for (let colspan = 0; colspan < (td.attrs.colspan || 1); colspan++) {
                  map[row + rowspan + "." + (col - colspan)] = 1;
                }
              }
            }
            if (style) {
              td.attrs.style = style;
            }
            cells.push(td);
            col++;
          }
        }
        if (row === 1) {
          let temp = "";
          for (let i = 1; i < col; i++) {
            temp += (width[i] ? width[i] : "auto") + " ";
          }
          styleObj["grid-template-columns"] = temp;
        }
      }
      node.children = cells;
    } else {
      if (node.c) {
        styleObj.display = "table";
      }
      if (!isNaN(spacing)) {
        styleObj["border-spacing"] = spacing + "px";
      }
      if (border || padding) {
        (function traversal(nodes) {
          for (let i = 0; i < nodes.length; i++) {
            const td = nodes[i];
            if (td.name === "th" || td.name === "td") {
              if (border) {
                td.attrs.style = `border:${border}px ${borderstyle || "solid"} ${bordercolor || "gray"};${td.attrs.style || ""}`;
              }
              if (padding) {
                td.attrs.style = `padding:${padding}px;${td.attrs.style || ""}`;
              }
            } else if (td.children) {
              traversal(td.children);
            }
          }
        })(children);
      }
    }
    if (this.options.scrollTable && !(attrs.style || "").includes("inline")) {
      const table = Object.assign({}, node);
      node.name = "div";
      node.attrs = {
        style: "overflow:auto"
      };
      node.children = [table];
      attrs = table.attrs;
    }
  } else if ((node.name === "td" || node.name === "th") && (attrs.colspan || attrs.rowspan)) {
    for (let i = this.stack.length; i--; ) {
      if (this.stack[i].name === "table") {
        this.stack[i].flag = 1;
        break;
      }
    }
  } else if (node.name === "ruby") {
    node.name = "span";
    for (let i = 0; i < children.length - 1; i++) {
      if (children[i].type === "text" && children[i + 1].name === "rt") {
        children[i] = {
          name: "div",
          attrs: {
            style: "display:inline-block;text-align:center"
          },
          children: [{
            name: "div",
            attrs: {
              style: "font-size:50%;" + (children[i + 1].attrs.style || "")
            },
            children: children[i + 1].children
          }, children[i]]
        };
        children.splice(i + 1, 1);
      }
    }
  } else if (node.c) {
    (function traversal(node2) {
      node2.c = 2;
      for (let i = node2.children.length; i--; ) {
        const child = node2.children[i];
        if (child.name && (config.inlineTags[child.name] || (child.attrs.style || "").includes("inline") && child.children) && !child.c) {
          traversal(child);
        }
        if (!child.c || child.name === "table") {
          node2.c = 1;
        }
      }
    })(node);
  }
  if ((styleObj.display || "").includes("flex") && !node.c) {
    for (let i = children.length; i--; ) {
      const item = children[i];
      if (item.f) {
        item.attrs.style = (item.attrs.style || "") + item.f;
        item.f = void 0;
      }
    }
  }
  const flex = parent && ((parent.attrs.style || "").includes("flex") || (parent.attrs.style || "").includes("grid")) && !(node.c && common_vendor.wx$1.getNFCAdapter);
  if (flex) {
    node.f = ";max-width:100%";
  }
  if (children.length >= 50 && node.c && !(styleObj.display || "").includes("flex")) {
    mergeNodes(children);
  }
  for (const key in styleObj) {
    if (styleObj[key]) {
      const val = `;${key}:${styleObj[key].replace(" !important", "")}`;
      if (flex && (key.includes("flex") && key !== "flex-direction" || key === "align-self" || key.includes("grid") || styleObj[key][0] === "-" || key.includes("width") && val.includes("%"))) {
        node.f += val;
        if (key === "width") {
          attrs.style += ";width:100%";
        }
      } else {
        attrs.style += val;
      }
    }
  }
  attrs.style = attrs.style.substr(1) || void 0;
  for (const key in attrs) {
    if (!attrs[key]) {
      delete attrs[key];
    }
  }
};
Parser.prototype.onText = function(text) {
  if (!this.pre) {
    let trim = "";
    let flag;
    for (let i = 0, len = text.length; i < len; i++) {
      if (!blankChar[text[i]]) {
        trim += text[i];
      } else {
        if (trim[trim.length - 1] !== " ") {
          trim += " ";
        }
        if (text[i] === "\n" && !flag) {
          flag = true;
        }
      }
    }
    if (trim === " ") {
      if (flag)
        return;
      else {
        const parent = this.stack[this.stack.length - 1];
        if (parent && parent.name[0] === "t")
          return;
      }
    }
    text = trim;
  }
  const node = /* @__PURE__ */ Object.create(null);
  node.type = "text";
  node.text = decodeEntity(text);
  if (this.hook(node)) {
    if (this.options.selectable === "force" && system.includes("iOS") && !common_vendor.index.canIUse("rich-text.user-select")) {
      this.expose();
    }
    const siblings = this.stack.length ? this.stack[this.stack.length - 1].children : this.nodes;
    siblings.push(node);
  }
};
function Lexer(handler) {
  this.handler = handler;
}
Lexer.prototype.parse = function(content) {
  this.content = content || "";
  this.i = 0;
  this.start = 0;
  this.state = this.text;
  for (let len = this.content.length; this.i !== -1 && this.i < len; ) {
    this.state();
  }
};
Lexer.prototype.checkClose = function(method) {
  const selfClose = this.content[this.i] === "/";
  if (this.content[this.i] === ">" || selfClose && this.content[this.i + 1] === ">") {
    if (method) {
      this.handler[method](this.content.substring(this.start, this.i));
    }
    this.i += selfClose ? 2 : 1;
    this.start = this.i;
    this.handler.onOpenTag(selfClose);
    if (this.handler.tagName === "script") {
      this.i = this.content.indexOf("</", this.i);
      if (this.i !== -1) {
        this.i += 2;
        this.start = this.i;
      }
      this.state = this.endTag;
    } else {
      this.state = this.text;
    }
    return true;
  }
  return false;
};
Lexer.prototype.text = function() {
  this.i = this.content.indexOf("<", this.i);
  if (this.i === -1) {
    if (this.start < this.content.length) {
      this.handler.onText(this.content.substring(this.start, this.content.length));
    }
    return;
  }
  const c = this.content[this.i + 1];
  if (c >= "a" && c <= "z" || c >= "A" && c <= "Z") {
    if (this.start !== this.i) {
      this.handler.onText(this.content.substring(this.start, this.i));
    }
    this.start = ++this.i;
    this.state = this.tagName;
  } else if (c === "/" || c === "!" || c === "?") {
    if (this.start !== this.i) {
      this.handler.onText(this.content.substring(this.start, this.i));
    }
    const next = this.content[this.i + 2];
    if (c === "/" && (next >= "a" && next <= "z" || next >= "A" && next <= "Z")) {
      this.i += 2;
      this.start = this.i;
      this.state = this.endTag;
      return;
    }
    let end = "-->";
    if (c !== "!" || this.content[this.i + 2] !== "-" || this.content[this.i + 3] !== "-") {
      end = ">";
    }
    this.i = this.content.indexOf(end, this.i);
    if (this.i !== -1) {
      this.i += end.length;
      this.start = this.i;
    }
  } else {
    this.i++;
  }
};
Lexer.prototype.tagName = function() {
  if (blankChar[this.content[this.i]]) {
    this.handler.onTagName(this.content.substring(this.start, this.i));
    while (blankChar[this.content[++this.i]])
      ;
    if (this.i < this.content.length && !this.checkClose()) {
      this.start = this.i;
      this.state = this.attrName;
    }
  } else if (!this.checkClose("onTagName")) {
    this.i++;
  }
};
Lexer.prototype.attrName = function() {
  let c = this.content[this.i];
  if (blankChar[c] || c === "=") {
    this.handler.onAttrName(this.content.substring(this.start, this.i));
    let needVal = c === "=";
    const len = this.content.length;
    while (++this.i < len) {
      c = this.content[this.i];
      if (!blankChar[c]) {
        if (this.checkClose())
          return;
        if (needVal) {
          this.start = this.i;
          this.state = this.attrVal;
          return;
        }
        if (this.content[this.i] === "=") {
          needVal = true;
        } else {
          this.start = this.i;
          this.state = this.attrName;
          return;
        }
      }
    }
  } else if (!this.checkClose("onAttrName")) {
    this.i++;
  }
};
Lexer.prototype.attrVal = function() {
  const c = this.content[this.i];
  const len = this.content.length;
  if (c === '"' || c === "'") {
    this.start = ++this.i;
    this.i = this.content.indexOf(c, this.i);
    if (this.i === -1)
      return;
    this.handler.onAttrVal(this.content.substring(this.start, this.i));
  } else {
    for (; this.i < len; this.i++) {
      if (blankChar[this.content[this.i]]) {
        this.handler.onAttrVal(this.content.substring(this.start, this.i));
        break;
      } else if (this.checkClose("onAttrVal"))
        return;
    }
  }
  while (blankChar[this.content[++this.i]])
    ;
  if (this.i < len && !this.checkClose()) {
    this.start = this.i;
    this.state = this.attrName;
  }
};
Lexer.prototype.endTag = function() {
  const c = this.content[this.i];
  if (blankChar[c] || c === ">" || c === "/") {
    this.handler.onCloseTag(this.content.substring(this.start, this.i));
    if (c !== ">") {
      this.i = this.content.indexOf(">", this.i);
      if (this.i === -1)
        return;
    }
    this.start = ++this.i;
    this.state = this.text;
  } else {
    this.i++;
  }
};
exports.Parser = Parser;
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/zero-markdown-view/components/mp-html/parser.js.map
