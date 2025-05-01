"use strict";
const uni_modules_uvUiTools_libs_function_throttle = require("../../../uv-ui-tools/libs/function/throttle.js");
const uni_modules_uvUiTools_libs_mixin_mpMixin = require("../../../uv-ui-tools/libs/mixin/mpMixin.js");
const uni_modules_uvUiTools_libs_mixin_mixin = require("../../../uv-ui-tools/libs/mixin/mixin.js");
const uni_modules_uvUiTools_libs_mixin_button = require("../../../uv-ui-tools/libs/mixin/button.js");
const uni_modules_uvUiTools_libs_mixin_openType = require("../../../uv-ui-tools/libs/mixin/openType.js");
const uni_modules_uvButton_components_uvButton_props = require("./props.js");
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  name: "uv-button",
  mixins: [uni_modules_uvUiTools_libs_mixin_mpMixin.mpMixin, uni_modules_uvUiTools_libs_mixin_mixin.mixin, uni_modules_uvUiTools_libs_mixin_button.button, uni_modules_uvUiTools_libs_mixin_openType.openType, uni_modules_uvButton_components_uvButton_props.props],
  emits: ["click"],
  data() {
    return {};
  },
  computed: {
    // 生成bem风格的类名
    bemClass() {
      if (!this.color) {
        return this.bem(
          "button",
          ["type", "shape", "size"],
          ["disabled", "plain", "hairline"]
        );
      } else {
        return this.bem(
          "button",
          ["shape", "size"],
          ["disabled", "plain", "hairline"]
        );
      }
    },
    loadingColor() {
      if (this.plain) {
        return this.color ? this.color : "#3c9cff";
      }
      if (this.type === "info") {
        return "#c9c9c9";
      }
      return "rgb(200, 200, 200)";
    },
    iconColorCom() {
      if (this.iconColor)
        return this.iconColor;
      if (this.plain) {
        return this.color ? this.color : this.type;
      } else {
        return this.type === "info" ? "#000000" : "#ffffff";
      }
    },
    baseColor() {
      let style = {};
      if (this.color) {
        style.color = this.plain ? this.color : "white";
        if (!this.plain) {
          style["background-color"] = this.color;
        }
        if (this.color.indexOf("gradient") !== -1) {
          style.borderTopWidth = 0;
          style.borderRightWidth = 0;
          style.borderBottomWidth = 0;
          style.borderLeftWidth = 0;
          if (!this.plain) {
            style.backgroundImage = this.color;
          }
        } else {
          style.borderColor = this.color;
          style.borderWidth = "1px";
          style.borderStyle = "solid";
        }
      }
      return style;
    },
    // nvue版本按钮的字体不会继承父组件的颜色，需要对每一个text组件进行单独的设置
    nvueTextStyle() {
      let style = {};
      if (this.type === "info") {
        style.color = "#323233";
      }
      if (this.color) {
        style.color = this.plain ? this.color : "white";
      }
      style.fontSize = this.textSize + "px";
      return style;
    },
    // 字体大小
    textSize() {
      let fontSize = 14, { size } = this;
      if (size === "large")
        fontSize = 16;
      if (size === "normal")
        fontSize = 14;
      if (size === "small")
        fontSize = 12;
      if (size === "mini")
        fontSize = 10;
      return fontSize;
    },
    // 设置图标大小
    getIconSize() {
      const size = this.iconSize ? this.iconSize : this.textSize * 1.35;
      return this.$uv.addUnit(size);
    },
    // 设置外层盒子的宽度，其他样式不需要
    btnWrapperStyle() {
      const style = {};
      const customStyle = this.$uv.addStyle(this.customStyle);
      if (customStyle.width)
        style.width = customStyle.width;
      return style;
    }
  },
  methods: {
    clickHandler() {
      if (!this.disabled && !this.loading) {
        uni_modules_uvUiTools_libs_function_throttle.throttle(() => {
          this.$emit("click");
        }, this.throttleTime);
      }
    }
  }
};
if (!Array) {
  const _easycom_uv_loading_icon2 = common_vendor.resolveComponent("uv-loading-icon");
  const _easycom_uv_icon2 = common_vendor.resolveComponent("uv-icon");
  (_easycom_uv_loading_icon2 + _easycom_uv_icon2)();
}
const _easycom_uv_loading_icon = () => "../../../uv-loading-icon/components/uv-loading-icon/uv-loading-icon.js";
const _easycom_uv_icon = () => "../../../uv-icon/components/uv-icon/uv-icon.js";
if (!Math) {
  (_easycom_uv_loading_icon + _easycom_uv_icon)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: _ctx.disabled || _ctx.loading
  }, _ctx.disabled || _ctx.loading ? {} : {}, {
    b: _ctx.loading
  }, _ctx.loading ? {
    c: common_vendor.p({
      mode: _ctx.loadingMode,
      size: _ctx.loadingSize * 1.15,
      color: $options.loadingColor
    }),
    d: common_vendor.t(_ctx.loadingText || _ctx.text),
    e: common_vendor.s({
      fontSize: $options.textSize + "px"
    }),
    f: common_vendor.s(_ctx.$uv.addStyle(_ctx.customTextStyle))
  } : common_vendor.e({
    g: _ctx.icon
  }, _ctx.icon ? {
    h: common_vendor.p({
      name: _ctx.icon,
      color: $options.iconColorCom,
      size: $options.getIconSize,
      customStyle: {
        marginRight: "2px"
      }
    })
  } : {}, {
    i: common_vendor.t(_ctx.text),
    j: common_vendor.s({
      fontSize: $options.textSize + "px"
    }),
    k: common_vendor.s(_ctx.$uv.addStyle(_ctx.customTextStyle))
  }), {
    l: Number(_ctx.hoverStartTime),
    m: Number(_ctx.hoverStayTime),
    n: _ctx.formType,
    o: _ctx.openType,
    p: _ctx.appParameter,
    q: _ctx.hoverStopPropagation,
    r: _ctx.sendMessageTitle,
    s: _ctx.sendMessagePath,
    t: _ctx.lang,
    v: _ctx.dataName,
    w: _ctx.sessionFrom,
    x: _ctx.sendMessageImg,
    y: _ctx.showMessageCard,
    z: common_vendor.o((...args) => _ctx.onGetPhoneNumber && _ctx.onGetPhoneNumber(...args)),
    A: common_vendor.o((...args) => _ctx.onGetUserInfo && _ctx.onGetUserInfo(...args)),
    B: common_vendor.o((...args) => _ctx.onError && _ctx.onError(...args)),
    C: common_vendor.o((...args) => _ctx.onOpenSetting && _ctx.onOpenSetting(...args)),
    D: common_vendor.o((...args) => _ctx.onLaunchApp && _ctx.onLaunchApp(...args)),
    E: common_vendor.o((...args) => _ctx.onContact && _ctx.onContact(...args)),
    F: common_vendor.o((...args) => _ctx.onChooseavatar && _ctx.onChooseavatar(...args)),
    G: common_vendor.o((...args) => _ctx.onAgreeprivacyauthorization && _ctx.onAgreeprivacyauthorization(...args)),
    H: common_vendor.o((...args) => _ctx.onAddgroupapp && _ctx.onAddgroupapp(...args)),
    I: common_vendor.o((...args) => _ctx.onChooseaddress && _ctx.onChooseaddress(...args)),
    J: common_vendor.o((...args) => _ctx.onSubscribe && _ctx.onSubscribe(...args)),
    K: common_vendor.o((...args) => _ctx.onLogin && _ctx.onLogin(...args)),
    L: common_vendor.o((...args) => _ctx.onIm && _ctx.onIm(...args)),
    M: common_vendor.s($options.baseColor),
    N: common_vendor.s(_ctx.$uv.addStyle(_ctx.customStyle)),
    O: common_vendor.o((...args) => $options.clickHandler && $options.clickHandler(...args)),
    P: common_vendor.n($options.bemClass),
    Q: common_vendor.s($options.btnWrapperStyle)
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-ae8e42c7"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uv-button/components/uv-button/uv-button.js.map
