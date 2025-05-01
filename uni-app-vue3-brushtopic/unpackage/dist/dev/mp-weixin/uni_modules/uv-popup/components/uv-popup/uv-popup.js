"use strict";
var _a, _b;
const common_vendor = require("../../../../common/vendor.js");
const uni_modules_uvUiTools_libs_mixin_mpMixin = require("../../../uv-ui-tools/libs/mixin/mpMixin.js");
const uni_modules_uvUiTools_libs_mixin_mixin = require("../../../uv-ui-tools/libs/mixin/mixin.js");
const _sfc_main = {
  name: "uv-popup",
  components: {},
  mixins: [uni_modules_uvUiTools_libs_mixin_mpMixin.mpMixin, uni_modules_uvUiTools_libs_mixin_mixin.mixin],
  emits: ["change", "maskClick"],
  props: {
    // 弹出层类型，可选值，top: 顶部弹出层；bottom：底部弹出层；center：全屏弹出层
    // message: 消息提示 ; dialog : 对话框
    mode: {
      type: String,
      default: "center"
    },
    // 动画时长，单位ms
    duration: {
      type: [String, Number],
      default: 300
    },
    // 层级
    zIndex: {
      type: [String, Number],
      default: 10075
    },
    bgColor: {
      type: String,
      default: "#ffffff"
    },
    safeArea: {
      type: Boolean,
      default: true
    },
    // 是否显示遮罩
    overlay: {
      type: Boolean,
      default: true
    },
    // 点击遮罩是否关闭弹窗
    closeOnClickOverlay: {
      type: Boolean,
      default: true
    },
    // 遮罩的透明度，0-1之间
    overlayOpacity: {
      type: [Number, String],
      default: 0.4
    },
    // 自定义遮罩的样式
    overlayStyle: {
      type: [Object, String],
      default: ""
    },
    // 是否为iPhoneX留出底部安全距离
    safeAreaInsetBottom: {
      type: Boolean,
      default: true
    },
    // 是否留出顶部安全距离（状态栏高度）
    safeAreaInsetTop: {
      type: Boolean,
      default: false
    },
    // 是否显示关闭图标
    closeable: {
      type: Boolean,
      default: false
    },
    // 自定义关闭图标位置，top-left为左上角，top-right为右上角，bottom-left为左下角，bottom-right为右下角
    closeIconPos: {
      type: String,
      default: "top-right"
    },
    // mode=center，也即中部弹出时，是否使用缩放模式
    zoom: {
      type: Boolean,
      default: true
    },
    round: {
      type: [Number, String],
      default: 0
    },
    ...(_b = (_a = common_vendor.index.$uv) == null ? void 0 : _a.props) == null ? void 0 : _b.popup
  },
  watch: {
    /**
     * 监听type类型
     */
    type: {
      handler: function(type) {
        if (!this.config[type])
          return;
        this[this.config[type]](true);
      },
      immediate: true
    },
    isDesktop: {
      handler: function(newVal) {
        if (!this.config[newVal])
          return;
        this[this.config[this.mode]](true);
      },
      immediate: true
    },
    // H5 下禁止底部滚动
    showPopup(show) {
    }
  },
  data() {
    return {
      ani: [],
      showPopup: false,
      showTrans: false,
      popupWidth: 0,
      popupHeight: 0,
      config: {
        top: "top",
        bottom: "bottom",
        center: "center",
        left: "left",
        right: "right",
        message: "top",
        dialog: "center",
        share: "bottom"
      },
      transitionStyle: {
        position: "fixed",
        left: 0,
        right: 0
      },
      maskShow: true,
      mkclick: true,
      popupClass: this.isDesktop ? "fixforpc-top" : "top",
      direction: ""
    };
  },
  computed: {
    isDesktop() {
      return this.popupWidth >= 500 && this.popupHeight >= 500;
    },
    bg() {
      if (this.bgColor === "" || this.bgColor === "none" || this.$uv.getPx(this.round) > 0) {
        return "transparent";
      }
      return this.bgColor;
    },
    contentStyle() {
      const style = {};
      if (this.bgColor) {
        style.backgroundColor = this.bg;
      }
      if (this.round) {
        const value = this.$uv.addUnit(this.round);
        const mode = this.direction ? this.direction : this.mode;
        style.backgroundColor = this.bgColor;
        if (mode === "top") {
          style.borderBottomLeftRadius = value;
          style.borderBottomRightRadius = value;
        } else if (mode === "bottom") {
          style.borderTopLeftRadius = value;
          style.borderTopRightRadius = value;
        } else if (mode === "center") {
          style.borderRadius = value;
        }
      }
      return this.$uv.deepMerge(style, this.$uv.addStyle(this.customStyle));
    }
  },
  // TODO vue3
  unmounted() {
    this.setH5Visible();
  },
  created() {
    this.messageChild = null;
    this.clearPropagation = false;
  },
  methods: {
    setH5Visible() {
    },
    /**
     * 公用方法，不显示遮罩层
     */
    closeMask() {
      this.maskShow = false;
    },
    // TODO nvue 取消冒泡
    clear(e) {
      e.stopPropagation();
      this.clearPropagation = true;
    },
    open(direction) {
      if (this.showPopup) {
        return;
      }
      let innerType = ["top", "center", "bottom", "left", "right", "message", "dialog", "share"];
      if (!(direction && innerType.indexOf(direction) !== -1)) {
        direction = this.mode;
      } else {
        this.direction = direction;
      }
      if (!this.config[direction]) {
        return this.$uv.error(`缺少类型：${direction}`);
      }
      this[this.config[direction]]();
      this.$emit("change", {
        show: true,
        type: direction
      });
    },
    close(type) {
      this.showTrans = false;
      this.$emit("change", {
        show: false,
        type: this.mode
      });
      clearTimeout(this.timer);
      this.timer = setTimeout(() => {
        this.showPopup = false;
      }, 300);
    },
    // TODO 处理冒泡事件，头条的冒泡事件有问题 ，先这样兼容
    touchstart() {
      this.clearPropagation = false;
    },
    onTap() {
      if (this.clearPropagation) {
        this.clearPropagation = false;
        return;
      }
      this.$emit("maskClick");
      if (!this.closeOnClickOverlay)
        return;
      this.close();
    },
    /**
     * 顶部弹出样式处理
     */
    top(type) {
      this.popupClass = this.isDesktop ? "fixforpc-top" : "top";
      this.ani = ["slide-top"];
      this.transitionStyle = {
        position: "fixed",
        zIndex: this.zIndex,
        left: 0,
        right: 0,
        backgroundColor: this.bg
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
      this.$nextTick(() => {
        if (this.messageChild && this.mode === "message") {
          this.messageChild.timerClose();
        }
      });
    },
    /**
     * 底部弹出样式处理
     */
    bottom(type) {
      this.popupClass = "bottom";
      this.ani = ["slide-bottom"];
      this.transitionStyle = {
        position: "fixed",
        zIndex: this.zIndex,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: this.bg
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
    },
    /**
     * 中间弹出样式处理
     */
    center(type) {
      this.popupClass = "center";
      this.ani = this.zoom ? ["zoom-in", "fade"] : ["fade"];
      this.transitionStyle = {
        position: "fixed",
        zIndex: this.zIndex,
        display: "flex",
        flexDirection: "column",
        bottom: 0,
        left: 0,
        right: 0,
        top: 0,
        justifyContent: "center",
        alignItems: "center"
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
    },
    left(type) {
      this.popupClass = "left";
      this.ani = ["slide-left"];
      this.transitionStyle = {
        position: "fixed",
        zIndex: this.zIndex,
        left: 0,
        bottom: 0,
        top: 0,
        backgroundColor: this.bg,
        display: "flex",
        flexDirection: "column"
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
    },
    right(type) {
      this.popupClass = "right";
      this.ani = ["slide-right"];
      this.transitionStyle = {
        position: "fixed",
        zIndex: this.zIndex,
        bottom: 0,
        right: 0,
        top: 0,
        backgroundColor: this.bg,
        display: "flex",
        flexDirection: "column"
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
    }
  }
};
if (!Array) {
  const _easycom_uv_overlay2 = common_vendor.resolveComponent("uv-overlay");
  const _easycom_uv_status_bar2 = common_vendor.resolveComponent("uv-status-bar");
  const _easycom_uv_safe_bottom2 = common_vendor.resolveComponent("uv-safe-bottom");
  const _easycom_uv_icon2 = common_vendor.resolveComponent("uv-icon");
  const _easycom_uv_transition2 = common_vendor.resolveComponent("uv-transition");
  (_easycom_uv_overlay2 + _easycom_uv_status_bar2 + _easycom_uv_safe_bottom2 + _easycom_uv_icon2 + _easycom_uv_transition2)();
}
const _easycom_uv_overlay = () => "../../../uv-overlay/components/uv-overlay/uv-overlay.js";
const _easycom_uv_status_bar = () => "../../../uv-status-bar/components/uv-status-bar/uv-status-bar.js";
const _easycom_uv_safe_bottom = () => "../../../uv-safe-bottom/components/uv-safe-bottom/uv-safe-bottom.js";
const _easycom_uv_icon = () => "../../../uv-icon/components/uv-icon/uv-icon.js";
const _easycom_uv_transition = () => "../../../uv-transition/components/uv-transition/uv-transition.js";
if (!Math) {
  (_easycom_uv_overlay + _easycom_uv_status_bar + _easycom_uv_safe_bottom + _easycom_uv_icon + _easycom_uv_transition)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.showPopup
  }, $data.showPopup ? common_vendor.e({
    b: $data.maskShow && $props.overlay
  }, $data.maskShow && $props.overlay ? {
    c: common_vendor.o($options.onTap),
    d: common_vendor.p({
      show: $data.showTrans,
      duration: $props.duration,
      ["custom-style"]: $props.overlayStyle,
      opacity: $props.overlayOpacity,
      zIndex: $props.zIndex
    })
  } : {}, {
    e: $props.safeAreaInsetTop
  }, $props.safeAreaInsetTop ? {} : {}, {
    f: $props.safeAreaInsetBottom
  }, $props.safeAreaInsetBottom ? {} : {}, {
    g: $props.closeable
  }, $props.closeable ? {
    h: common_vendor.p({
      name: "close",
      color: "#909399",
      size: "18",
      bold: true
    }),
    i: common_vendor.o((...args) => $options.close && $options.close(...args)),
    j: common_vendor.n("uv-popup__content__close--" + $props.closeIconPos)
  } : {}, {
    k: common_vendor.s($options.contentStyle),
    l: common_vendor.n($data.popupClass),
    m: common_vendor.o((...args) => $options.clear && $options.clear(...args)),
    n: common_vendor.o($options.onTap),
    o: common_vendor.p({
      mode: $data.ani,
      name: "content",
      ["custom-style"]: $data.transitionStyle,
      duration: $props.duration,
      show: $data.showTrans
    }),
    p: common_vendor.o((...args) => $options.touchstart && $options.touchstart(...args)),
    q: common_vendor.n($data.popupClass),
    r: common_vendor.n($options.isDesktop ? "fixforpc-z-index" : ""),
    s: common_vendor.s({
      zIndex: $props.zIndex
    })
  }) : {});
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-01a3ad6e"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uv-popup/components/uv-popup/uv-popup.js.map
