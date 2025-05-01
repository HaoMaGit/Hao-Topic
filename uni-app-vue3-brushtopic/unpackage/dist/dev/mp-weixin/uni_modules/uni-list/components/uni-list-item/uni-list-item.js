"use strict";
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  name: "UniListItem",
  emits: ["click", "switchChange"],
  props: {
    direction: {
      type: String,
      default: "row"
    },
    title: {
      type: String,
      default: ""
    },
    note: {
      type: String,
      default: ""
    },
    ellipsis: {
      type: [Number, String],
      default: 0
    },
    disabled: {
      type: [Boolean, String],
      default: false
    },
    clickable: {
      type: Boolean,
      default: false
    },
    showArrow: {
      type: [Boolean, String],
      default: false
    },
    link: {
      type: [Boolean, String],
      default: false
    },
    to: {
      type: String,
      default: ""
    },
    showBadge: {
      type: [Boolean, String],
      default: false
    },
    showSwitch: {
      type: [Boolean, String],
      default: false
    },
    switchChecked: {
      type: [Boolean, String],
      default: false
    },
    badgeText: {
      type: String,
      default: ""
    },
    badgeType: {
      type: String,
      default: "success"
    },
    badgeStyle: {
      type: Object,
      default() {
        return {};
      }
    },
    rightText: {
      type: String,
      default: ""
    },
    thumb: {
      type: String,
      default: ""
    },
    thumbSize: {
      type: String,
      default: "base"
    },
    showExtraIcon: {
      type: [Boolean, String],
      default: false
    },
    extraIcon: {
      type: Object,
      default() {
        return {
          type: "",
          color: "#000000",
          size: 20,
          customPrefix: ""
        };
      }
    },
    border: {
      type: Boolean,
      default: true
    },
    customStyle: {
      type: Object,
      default() {
        return {
          padding: "",
          backgroundColor: "#FFFFFF"
        };
      }
    },
    keepScrollPosition: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    "customStyle.padding": {
      handler(padding) {
        if (typeof padding == "number") {
          padding += "";
        }
        let paddingArr = padding.split(" ");
        if (paddingArr.length === 1) {
          const allPadding = paddingArr[0];
          this.padding = {
            "top": allPadding,
            "right": allPadding,
            "bottom": allPadding,
            "left": allPadding
          };
        } else if (paddingArr.length === 2) {
          const [verticalPadding, horizontalPadding] = paddingArr;
          this.padding = {
            "top": verticalPadding,
            "right": horizontalPadding,
            "bottom": verticalPadding,
            "left": horizontalPadding
          };
        } else if (paddingArr.length === 4) {
          const [topPadding, rightPadding, bottomPadding, leftPadding] = paddingArr;
          this.padding = {
            "top": topPadding,
            "right": rightPadding,
            "bottom": bottomPadding,
            "left": leftPadding
          };
        }
      },
      immediate: true
    }
  },
  // inject: ['list'],
  data() {
    return {
      isFirstChild: false,
      padding: {
        top: "",
        right: "",
        bottom: "",
        left: ""
      }
    };
  },
  mounted() {
    this.list = this.getForm();
    if (this.list) {
      if (!this.list.firstChildAppend) {
        this.list.firstChildAppend = true;
        this.isFirstChild = true;
      }
    }
  },
  methods: {
    /**
     * 获取父元素实例
     */
    getForm(name = "uniList") {
      let parent = this.$parent;
      let parentName = parent.$options.name;
      while (parentName !== name) {
        parent = parent.$parent;
        if (!parent)
          return false;
        parentName = parent.$options.name;
      }
      return parent;
    },
    onClick() {
      if (this.to !== "") {
        this.openPage();
        return;
      }
      if (this.clickable || this.link) {
        this.$emit("click", {
          data: {}
        });
      }
    },
    onSwitchChange(e) {
      this.$emit("switchChange", e.detail);
    },
    openPage() {
      if (["navigateTo", "redirectTo", "reLaunch", "switchTab"].indexOf(this.link) !== -1) {
        this.pageApi(this.link);
      } else {
        this.pageApi("navigateTo");
      }
    },
    pageApi(api) {
      let callback = {
        url: this.to,
        success: (res) => {
          this.$emit("click", {
            data: res
          });
        },
        fail: (err) => {
          this.$emit("click", {
            data: err
          });
        }
      };
      switch (api) {
        case "navigateTo":
          common_vendor.index.navigateTo(callback);
          break;
        case "redirectTo":
          common_vendor.index.redirectTo(callback);
          break;
        case "reLaunch":
          common_vendor.index.reLaunch(callback);
          break;
        case "switchTab":
          common_vendor.index.switchTab(callback);
          break;
        default:
          common_vendor.index.navigateTo(callback);
      }
    }
  }
};
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_badge2 = common_vendor.resolveComponent("uni-badge");
  (_easycom_uni_icons2 + _easycom_uni_badge2)();
}
const _easycom_uni_icons = () => "../../../uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_badge = () => "../../../uni-badge/components/uni-badge/uni-badge.js";
if (!Math) {
  (_easycom_uni_icons + _easycom_uni_badge)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: !$data.isFirstChild
  }, !$data.isFirstChild ? {
    b: $props.border ? 1 : ""
  } : {}, {
    c: $props.thumb
  }, $props.thumb ? {
    d: $props.thumb,
    e: common_vendor.n("uni-list--" + $props.thumbSize)
  } : $props.showExtraIcon ? {
    g: common_vendor.p({
      customPrefix: $props.extraIcon.customPrefix,
      color: $props.extraIcon.color,
      size: $props.extraIcon.size,
      type: $props.extraIcon.type
    })
  } : {}, {
    f: $props.showExtraIcon,
    h: $props.title
  }, $props.title ? {
    i: common_vendor.t($props.title),
    j: common_vendor.n($props.ellipsis !== 0 && $props.ellipsis <= 2 ? "uni-ellipsis-" + $props.ellipsis : "")
  } : {}, {
    k: $props.note
  }, $props.note ? {
    l: common_vendor.t($props.note)
  } : {}, {
    m: $props.thumb || $props.showExtraIcon || $props.showBadge || $props.showSwitch ? 1 : "",
    n: $props.rightText || $props.showBadge || $props.showSwitch
  }, $props.rightText || $props.showBadge || $props.showSwitch ? common_vendor.e({
    o: $props.rightText
  }, $props.rightText ? {
    p: common_vendor.t($props.rightText)
  } : {}, {
    q: $props.showBadge
  }, $props.showBadge ? {
    r: common_vendor.p({
      type: $props.badgeType,
      text: $props.badgeText,
      ["custom-style"]: $props.badgeStyle
    })
  } : {}, {
    s: $props.showSwitch
  }, $props.showSwitch ? {
    t: $props.disabled,
    v: $props.switchChecked,
    w: common_vendor.o((...args) => $options.onSwitchChange && $options.onSwitchChange(...args))
  } : {}, {
    x: $props.direction === "column" ? 1 : ""
  }) : {}, {
    y: $props.showArrow || $props.link ? 1 : "",
    z: $props.direction === "column" ? 1 : "",
    A: $data.padding.top,
    B: $data.padding.left,
    C: $data.padding.right,
    D: $data.padding.bottom,
    E: $props.showArrow || $props.link
  }, $props.showArrow || $props.link ? {
    F: common_vendor.p({
      size: 16,
      color: "#bbb",
      type: "arrowright"
    })
  } : {}, {
    G: $props.disabled ? 1 : "",
    H: $props.customStyle.backgroundColor,
    I: !$props.clickable && !$props.link || $props.disabled || $props.showSwitch ? "" : "uni-list-item--hover",
    J: common_vendor.o((...args) => $options.onClick && $options.onClick(...args))
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uni-list/components/uni-list-item/uni-list-item.js.map
