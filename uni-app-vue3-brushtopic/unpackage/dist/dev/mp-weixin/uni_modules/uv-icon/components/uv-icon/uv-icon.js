"use strict";
const uni_modules_uvUiTools_libs_mixin_mpMixin = require("../../../uv-ui-tools/libs/mixin/mpMixin.js");
const uni_modules_uvUiTools_libs_mixin_mixin = require("../../../uv-ui-tools/libs/mixin/mixin.js");
const uni_modules_uvIcon_components_uvIcon_icons = require("./icons.js");
const uni_modules_uvIcon_components_uvIcon_props = require("./props.js");
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  name: "uv-icon",
  emits: ["click"],
  mixins: [uni_modules_uvUiTools_libs_mixin_mpMixin.mpMixin, uni_modules_uvUiTools_libs_mixin_mixin.mixin, uni_modules_uvIcon_components_uvIcon_props.props],
  data() {
    return {
      colorType: [
        "primary",
        "success",
        "info",
        "error",
        "warning"
      ]
    };
  },
  computed: {
    uClasses() {
      let classes = [];
      classes.push(this.customPrefix);
      classes.push(this.customPrefix + "-" + this.name);
      if (this.color && this.colorType.includes(this.color))
        classes.push("uv-icon__icon--" + this.color);
      return classes;
    },
    iconStyle() {
      let style = {};
      style = {
        fontSize: this.$uv.addUnit(this.size),
        lineHeight: this.$uv.addUnit(this.size),
        fontWeight: this.bold ? "bold" : "normal",
        // 某些特殊情况需要设置一个到顶部的距离，才能更好的垂直居中
        top: this.$uv.addUnit(this.top)
      };
      if (this.color && !this.colorType.includes(this.color))
        style.color = this.color;
      return style;
    },
    // 判断传入的name属性，是否图片路径，只要带有"/"均认为是图片形式
    isImg() {
      const isBase64 = this.name.indexOf("data:") > -1 && this.name.indexOf("base64") > -1;
      return this.name.indexOf("/") !== -1 || isBase64;
    },
    imgStyle() {
      let style = {};
      style.width = this.width ? this.$uv.addUnit(this.width) : this.$uv.addUnit(this.size);
      style.height = this.height ? this.$uv.addUnit(this.height) : this.$uv.addUnit(this.size);
      return style;
    },
    // 通过图标名，查找对应的图标
    icon() {
      const code = uni_modules_uvIcon_components_uvIcon_icons.icons["uvicon-" + this.name];
      return code ? unescape(`%u${code}`) : ["uvicon"].indexOf(this.customPrefix) > -1 ? this.name : "";
    }
  },
  methods: {
    clickHandler(e) {
      this.$emit("click", this.index);
      this.stop && this.preventEvent(e);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $options.isImg
  }, $options.isImg ? {
    b: _ctx.name,
    c: _ctx.imgMode,
    d: common_vendor.s($options.imgStyle),
    e: common_vendor.s(_ctx.$uv.addStyle(_ctx.customStyle))
  } : {
    f: common_vendor.t($options.icon),
    g: common_vendor.n($options.uClasses),
    h: common_vendor.s($options.iconStyle),
    i: common_vendor.s(_ctx.$uv.addStyle(_ctx.customStyle)),
    j: _ctx.hoverClass
  }, {
    k: _ctx.label !== ""
  }, _ctx.label !== "" ? {
    l: common_vendor.t(_ctx.label),
    m: _ctx.labelColor,
    n: _ctx.$uv.addUnit(_ctx.labelSize),
    o: _ctx.labelPos == "right" ? _ctx.$uv.addUnit(_ctx.space) : 0,
    p: _ctx.labelPos == "bottom" ? _ctx.$uv.addUnit(_ctx.space) : 0,
    q: _ctx.labelPos == "left" ? _ctx.$uv.addUnit(_ctx.space) : 0,
    r: _ctx.labelPos == "top" ? _ctx.$uv.addUnit(_ctx.space) : 0
  } : {}, {
    s: common_vendor.o((...args) => $options.clickHandler && $options.clickHandler(...args)),
    t: common_vendor.n("uv-icon--" + _ctx.labelPos)
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-b7a6dd5d"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uv-icon/components/uv-icon/uv-icon.js.map
