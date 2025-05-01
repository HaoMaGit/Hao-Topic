"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  __name: "ranking",
  setup(__props) {
    const screen = common_vendor.ref(true);
    const list = common_vendor.ref([{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}]);
    common_vendor.ref(0);
    return (_ctx, _cache) => {
      return {
        a: screen.value ? 1 : "",
        b: common_vendor.o(($event) => screen.value = true),
        c: !screen.value ? 1 : "",
        d: common_vendor.o(($event) => screen.value = false),
        e: common_vendor.f(list.value, (item, key, i0) => {
          return {
            a: common_vendor.t(key + 4),
            b: common_vendor.t(key),
            c: key
          };
        }),
        f: common_vendor.o((...args) => _ctx.upper && _ctx.upper(...args)),
        g: common_vendor.o((...args) => _ctx.lower && _ctx.lower(...args)),
        h: common_vendor.o((...args) => _ctx.scroll && _ctx.scroll(...args))
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-7d24afc7"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/index/ranking/ranking.js.map
