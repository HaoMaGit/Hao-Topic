"use strict";
const uni_modules_uvUiTools_libs_mixin_mpMixin = require("../../../uv-ui-tools/libs/mixin/mpMixin.js");
const uni_modules_uvUiTools_libs_mixin_mixin = require("../../../uv-ui-tools/libs/mixin/mixin.js");
const uni_modules_uvModal_components_uvModal_props = require("./props.js");
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  name: "uv-modal",
  mixins: [uni_modules_uvUiTools_libs_mixin_mpMixin.mpMixin, uni_modules_uvUiTools_libs_mixin_mixin.mixin, uni_modules_uvModal_components_uvModal_props.props],
  data() {
    return {
      loading: false
    };
  },
  computed: {
    nvueStyle() {
      const style = {};
      return style;
    }
  },
  methods: {
    open() {
      this.$refs.modalPopup.open();
      if (this.loading)
        this.loading = false;
    },
    close() {
      this.$refs.modalPopup.close();
    },
    popupChange(e) {
      if (!e.show)
        this.$emit("close");
    },
    // 点击确定按钮
    confirmHandler() {
      if (!this.loading) {
        this.$emit("confirm");
      }
      if (this.asyncClose) {
        this.loading = true;
      } else {
        this.close();
      }
    },
    // 点击取消按钮
    cancelHandler() {
      this.$emit("cancel");
      this.close();
    },
    closeLoading() {
      this.$nextTick(() => {
        this.loading = false;
      });
    }
  }
};
if (!Array) {
  const _easycom_uv_line2 = common_vendor.resolveComponent("uv-line");
  const _easycom_uv_loading_icon2 = common_vendor.resolveComponent("uv-loading-icon");
  const _easycom_uv_popup2 = common_vendor.resolveComponent("uv-popup");
  (_easycom_uv_line2 + _easycom_uv_loading_icon2 + _easycom_uv_popup2)();
}
const _easycom_uv_line = () => "../../../uv-line/components/uv-line/uv-line.js";
const _easycom_uv_loading_icon = () => "../../../uv-loading-icon/components/uv-loading-icon/uv-loading-icon.js";
const _easycom_uv_popup = () => "../../../uv-popup/components/uv-popup/uv-popup.js";
if (!Math) {
  (_easycom_uv_line + _easycom_uv_loading_icon + _easycom_uv_popup)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: _ctx.title
  }, _ctx.title ? {
    b: common_vendor.t(_ctx.title)
  } : {}, {
    c: common_vendor.t(_ctx.content),
    d: common_vendor.s({
      textAlign: _ctx.align
    }),
    e: common_vendor.s($options.nvueStyle),
    f: common_vendor.s(_ctx.$uv.addStyle(_ctx.textStyle)),
    g: `${_ctx.title ? 12 : 25}px`,
    h: _ctx.showConfirmButton || _ctx.showCancelButton
  }, _ctx.showConfirmButton || _ctx.showCancelButton ? common_vendor.e({
    i: _ctx.showCancelButton
  }, _ctx.showCancelButton ? {
    j: common_vendor.t(_ctx.cancelText),
    k: _ctx.cancelColor,
    l: common_vendor.n(_ctx.showCancelButton && !_ctx.showConfirmButton && "uv-modal__button-group__wrapper--only-cancel"),
    m: common_vendor.o((...args) => $options.cancelHandler && $options.cancelHandler(...args))
  } : {}, {
    n: _ctx.showConfirmButton && _ctx.showCancelButton
  }, _ctx.showConfirmButton && _ctx.showCancelButton ? {
    o: common_vendor.p({
      direction: "column"
    })
  } : {}, {
    p: _ctx.showConfirmButton
  }, _ctx.showConfirmButton ? common_vendor.e({
    q: $data.loading
  }, $data.loading ? {} : {
    r: common_vendor.t(_ctx.confirmText),
    s: _ctx.confirmColor
  }, {
    t: common_vendor.n(!_ctx.showCancelButton && _ctx.showConfirmButton && "uv-modal__button-group__wrapper--only-confirm"),
    v: common_vendor.o((...args) => $options.confirmHandler && $options.confirmHandler(...args))
  }) : {}, {
    w: _ctx.buttonReverse ? "row-reverse" : "row"
  }) : {}, {
    x: _ctx.$uv.addUnit(_ctx.width),
    y: common_vendor.sr("modalPopup", "4b4aa5ec-0"),
    z: common_vendor.o($options.popupChange),
    A: common_vendor.p({
      mode: "center",
      zoom: _ctx.zoom,
      zIndex: _ctx.zIndex,
      customStyle: {
        borderRadius: "6px",
        overflow: "hidden",
        marginTop: `-${_ctx.$uv.addUnit(_ctx.negativeTop)}`
      },
      closeOnClickOverlay: _ctx.closeOnClickOverlay,
      safeAreaInsetBottom: false,
      duration: 400
    })
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-4b4aa5ec"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uv-modal/components/uv-modal/uv-modal.js.map
