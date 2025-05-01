"use strict";
const common_vendor = require("../../../../common/vendor.js");
const uni_modules_zeroMarkdownView_components_mpHtml_parser = require("./parser.js");
const uni_modules_zeroMarkdownView_components_mpHtml_markdown_index = require("./markdown/index.js");
const uni_modules_zeroMarkdownView_components_mpHtml_highlight_index = require("./highlight/index.js");
const uni_modules_zeroMarkdownView_components_mpHtml_style_index = require("./style/index.js");
const node = () => "./node/node.js";
const plugins = [uni_modules_zeroMarkdownView_components_mpHtml_markdown_index.Markdown, uni_modules_zeroMarkdownView_components_mpHtml_highlight_index.Highlight, uni_modules_zeroMarkdownView_components_mpHtml_style_index.Style];
const _sfc_main = {
  name: "mp-html",
  data() {
    return {
      nodes: []
    };
  },
  props: {
    markdown: Boolean,
    containerStyle: {
      type: String,
      default: ""
    },
    content: {
      type: String,
      default: ""
    },
    copyLink: {
      type: [Boolean, String],
      default: true
    },
    domain: String,
    errorImg: {
      type: String,
      default: ""
    },
    lazyLoad: {
      type: [Boolean, String],
      default: false
    },
    loadingImg: {
      type: String,
      default: ""
    },
    pauseVideo: {
      type: [Boolean, String],
      default: true
    },
    previewImg: {
      type: [Boolean, String],
      default: true
    },
    scrollTable: [Boolean, String],
    selectable: [Boolean, String],
    setTitle: {
      type: [Boolean, String],
      default: true
    },
    showImgMenu: {
      type: [Boolean, String],
      default: true
    },
    tagStyle: Object,
    useAnchor: [Boolean, Number]
  },
  emits: ["load", "ready", "imgtap", "linktap", "play", "error"],
  components: {
    node
  },
  watch: {
    content(content) {
      this.setContent(content);
    }
  },
  created() {
    this.plugins = [];
    for (let i = plugins.length; i--; ) {
      this.plugins.push(new plugins[i](this));
    }
  },
  mounted() {
    if (this.content && !this.nodes.length) {
      this.setContent(this.content);
    }
  },
  beforeDestroy() {
    this._hook("onDetached");
  },
  methods: {
    /**
     * @description 将锚点跳转的范围限定在一个 scroll-view 内
     * @param {Object} page scroll-view 所在页面的示例
     * @param {String} selector scroll-view 的选择器
     * @param {String} scrollTop scroll-view scroll-top 属性绑定的变量名
     */
    in(page, selector, scrollTop) {
      if (page && selector && scrollTop) {
        this._in = {
          page,
          selector,
          scrollTop
        };
      }
    },
    /**
     * @description 锚点跳转
     * @param {String} id 要跳转的锚点 id
     * @param {Number} offset 跳转位置的偏移量
     * @returns {Promise}
     */
    navigateTo(id, offset) {
      id = this._ids[decodeURI(id)] || id;
      return new Promise((resolve, reject) => {
        if (!this.useAnchor) {
          reject(Error("Anchor is disabled"));
          return;
        }
        offset = offset || parseInt(this.useAnchor) || 0;
        let deep = " ";
        deep = ">>>";
        const selector = common_vendor.index.createSelectorQuery().in(this._in ? this._in.page : this).select((this._in ? this._in.selector : "._root") + (id ? `${deep}#${id}` : "")).boundingClientRect();
        if (this._in) {
          selector.select(this._in.selector).scrollOffset().select(this._in.selector).boundingClientRect();
        } else {
          selector.selectViewport().scrollOffset();
        }
        selector.exec((res) => {
          if (!res[0]) {
            reject(Error("Label not found"));
            return;
          }
          const scrollTop = res[1].scrollTop + res[0].top - (res[2] ? res[2].top : 0) + offset;
          if (this._in) {
            this._in.page[this._in.scrollTop] = scrollTop;
          } else {
            common_vendor.index.pageScrollTo({
              scrollTop,
              duration: 300
            });
          }
          resolve();
        });
      });
    },
    /**
     * @description 获取文本内容
     * @return {String}
     */
    getText(nodes) {
      let text = "";
      (function traversal(nodes2) {
        for (let i = 0; i < nodes2.length; i++) {
          const node2 = nodes2[i];
          if (node2.type === "text") {
            text += node2.text.replace(/&amp;/g, "&");
          } else if (node2.name === "br") {
            text += "\n";
          } else {
            const isBlock = node2.name === "p" || node2.name === "div" || node2.name === "tr" || node2.name === "li" || node2.name[0] === "h" && node2.name[1] > "0" && node2.name[1] < "7";
            if (isBlock && text && text[text.length - 1] !== "\n") {
              text += "\n";
            }
            if (node2.children) {
              traversal(node2.children);
            }
            if (isBlock && text[text.length - 1] !== "\n") {
              text += "\n";
            } else if (node2.name === "td" || node2.name === "th") {
              text += "	";
            }
          }
        }
      })(nodes || this.nodes);
      return text;
    },
    /**
     * @description 获取内容大小和位置
     * @return {Promise}
     */
    getRect() {
      return new Promise((resolve, reject) => {
        common_vendor.index.createSelectorQuery().in(this).select("#_root").boundingClientRect().exec((res) => res[0] ? resolve(res[0]) : reject(Error("Root label not found")));
      });
    },
    /**
     * @description 暂停播放媒体
     */
    pauseMedia() {
      for (let i = (this._videos || []).length; i--; ) {
        this._videos[i].pause();
      }
    },
    /**
     * @description 设置媒体播放速率
     * @param {Number} rate 播放速率
     */
    setPlaybackRate(rate) {
      this.playbackRate = rate;
      for (let i = (this._videos || []).length; i--; ) {
        this._videos[i].playbackRate(rate);
      }
    },
    /**
     * @description 设置内容
     * @param {String} content html 内容
     * @param {Boolean} append 是否在尾部追加
     */
    setContent(content, append) {
      if (!append || !this.imgList) {
        this.imgList = [];
      }
      const nodes = new uni_modules_zeroMarkdownView_components_mpHtml_parser.Parser(this).parse(content);
      this.$set(this, "nodes", append ? (this.nodes || []).concat(nodes) : nodes);
      this._videos = [];
      this.$nextTick(() => {
        this._hook("onLoad");
        this.$emit("load");
      });
      if (this.lazyLoad || this.imgList._unloadimgs < this.imgList.length / 2) {
        let height = 0;
        const callback = (rect) => {
          if (!rect || !rect.height)
            rect = {};
          if (rect.height === height) {
            this.$emit("ready", rect);
          } else {
            height = rect.height;
            setTimeout(() => {
              this.getRect().then(callback).catch(callback);
            }, 350);
          }
        };
        this.getRect().then(callback).catch(callback);
      } else {
        if (!this.imgList._unloadimgs) {
          this.getRect().then((rect) => {
            this.$emit("ready", rect);
          }).catch(() => {
            this.$emit("ready", {});
          });
        }
      }
    },
    /**
     * @description 调用插件钩子函数
     */
    _hook(name) {
      for (let i = plugins.length; i--; ) {
        if (this.plugins[i][name]) {
          this.plugins[i][name]();
        }
      }
    }
  }
};
if (!Array) {
  const _component_node = common_vendor.resolveComponent("node");
  _component_node();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: !$data.nodes[0]
  }, !$data.nodes[0] ? {} : {
    b: common_vendor.p({
      childs: $data.nodes,
      opts: [$props.lazyLoad, $props.loadingImg, $props.errorImg, $props.showImgMenu, $props.selectable],
      name: "span"
    })
  }, {
    c: common_vendor.n(($props.selectable ? "_select " : "") + "_root"),
    d: common_vendor.s($props.containerStyle)
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/zero-markdown-view/components/mp-html/mp-html.js.map
