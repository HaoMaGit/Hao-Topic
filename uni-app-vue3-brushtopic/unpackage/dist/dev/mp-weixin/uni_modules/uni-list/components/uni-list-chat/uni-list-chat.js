"use strict";
const common_vendor = require("../../../../common/vendor.js");
const avatarWidth = 45;
const _sfc_main = {
  name: "UniListChat",
  emits: ["click"],
  props: {
    title: {
      type: String,
      default: ""
    },
    note: {
      type: String,
      default: ""
    },
    clickable: {
      type: Boolean,
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
    badgeText: {
      type: [String, Number],
      default: ""
    },
    badgePositon: {
      type: String,
      default: "right"
    },
    time: {
      type: String,
      default: ""
    },
    avatarCircle: {
      type: Boolean,
      default: false
    },
    avatar: {
      type: String,
      default: ""
    },
    avatarList: {
      type: Array,
      default() {
        return [];
      }
    }
  },
  // inject: ['list'],
  computed: {
    isDraft() {
      return this.note.slice(0, 14) == "[uni-im-draft]";
    },
    isSingle() {
      if (this.badgeText === "dot") {
        return "uni-badge--dot";
      } else {
        const badgeText = this.badgeText.toString();
        if (badgeText.length > 1) {
          return "uni-badge--complex";
        } else {
          return "uni-badge--single";
        }
      }
    },
    computedAvatar() {
      if (this.avatarList.length > 4) {
        this.imageWidth = avatarWidth * 0.31;
        return "avatarItem--3";
      } else if (this.avatarList.length > 1) {
        this.imageWidth = avatarWidth * 0.47;
        return "avatarItem--2";
      } else {
        this.imageWidth = avatarWidth;
        return "avatarItem--1";
      }
    }
  },
  watch: {
    avatar: {
      handler(avatar) {
        if (avatar.substr(0, 8) == "cloud://") {
          common_vendor.er.getTempFileURL({
            fileList: [avatar]
          }).then((res) => {
            let fileList = res.fileList || res.result.fileList;
            this.avatarUrl = fileList[0].tempFileURL;
          });
        } else {
          this.avatarUrl = avatar;
        }
      },
      immediate: true
    }
  },
  data() {
    return {
      isFirstChild: false,
      border: true,
      // avatarList: 3,
      imageWidth: 50,
      avatarUrl: ""
    };
  },
  mounted() {
    this.list = this.getForm();
    if (this.list) {
      if (!this.list.firstChildAppend) {
        this.list.firstChildAppend = true;
        this.isFirstChild = true;
      }
      this.border = this.list.border;
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
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.border ? 1 : "",
    b: $data.isFirstChild ? 1 : "",
    c: $props.avatarCircle || $props.avatarList.length === 0
  }, $props.avatarCircle || $props.avatarList.length === 0 ? {
    d: $props.avatarCircle ? 1 : "",
    e: $data.avatarUrl,
    f: $props.avatarCircle ? 1 : ""
  } : {
    g: common_vendor.f($props.avatarList, (item, index, i0) => {
      return {
        a: item.url,
        b: index
      };
    }),
    h: $data.imageWidth + "px",
    i: $data.imageWidth + "px",
    j: common_vendor.n($options.computedAvatar),
    k: $data.imageWidth + "px",
    l: $data.imageWidth + "px"
  }, {
    m: $props.badgeText && $props.badgePositon === "left"
  }, $props.badgeText && $props.badgePositon === "left" ? {
    n: common_vendor.t($props.badgeText === "dot" ? "" : $props.badgeText),
    o: common_vendor.n($options.isSingle)
  } : {}, {
    p: common_vendor.t($props.title),
    q: $options.isDraft
  }, $options.isDraft ? {} : {}, {
    r: common_vendor.t($options.isDraft ? $props.note.slice(14) : $props.note),
    s: common_vendor.t($props.time),
    t: $props.badgeText && $props.badgePositon === "right"
  }, $props.badgeText && $props.badgePositon === "right" ? {
    v: common_vendor.t($props.badgeText === "dot" ? "" : $props.badgeText),
    w: common_vendor.n($options.isSingle),
    x: common_vendor.n($props.badgePositon === "right" ? "uni-list-chat--right" : "")
  } : {}, {
    y: !$props.clickable && !$props.link ? "" : "uni-list-chat--hover",
    z: common_vendor.o((...args) => $options.onClick && $options.onClick(...args))
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uni-list/components/uni-list-chat/uni-list-chat.js.map
