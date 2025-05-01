"use strict";
const uni_modules_uvUiTools_libs_mixin_mpMixin = require("../../../uv-ui-tools/libs/mixin/mpMixin.js");
const uni_modules_uvUiTools_libs_mixin_mixin = require("../../../uv-ui-tools/libs/mixin/mixin.js");
const uni_modules_uvStatusBar_components_uvStatusBar_props = require("./props.js");
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  name: "uv-status-bar",
  mixins: [uni_modules_uvUiTools_libs_mixin_mpMixin.mpMixin, uni_modules_uvUiTools_libs_mixin_mixin.mixin, uni_modules_uvStatusBar_components_uvStatusBar_props.props],
  data() {
    return {};
  },
  computed: {
    style() {
      const style = {};
      style.height = this.$uv.addUnit(this.$uv.sys().statusBarHeight, "px");
      if (this.bgColor) {
        if (this.bgColor.indexOf("gradient") > -1) {
          style.backgroundImage = this.bgColor;
        } else {
          style.background = this.bgColor;
        }
      }
      return this.$uv.deepMerge(style, this.$uv.addStyle(this.customStyle));
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.s($options.style)
  };
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-f5bd6f5a"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uv-status-bar/components/uv-status-bar/uv-status-bar.js.map
