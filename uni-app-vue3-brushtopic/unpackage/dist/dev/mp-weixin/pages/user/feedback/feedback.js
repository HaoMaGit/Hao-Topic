"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_card2 = common_vendor.resolveComponent("uni-card");
  const _component_uni_section = common_vendor.resolveComponent("uni-section");
  (_easycom_uni_icons2 + _easycom_uni_card2 + _component_uni_section)();
}
const _easycom_uni_icons = () => "../../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_card = () => "../../../uni_modules/uni-card/components/uni-card/uni-card.js";
if (!Math) {
  (_easycom_uni_icons + _easycom_uni_card)();
}
const _sfc_main = {
  __name: "feedback",
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(10, (i, k0, i0) => {
          return {
            a: "e9b0a919-2-" + i0 + "," + ("e9b0a919-1-" + i0),
            b: "e9b0a919-3-" + i0 + "," + ("e9b0a919-1-" + i0),
            c: "e9b0a919-4-" + i0 + "," + ("e9b0a919-1-" + i0),
            d: "e9b0a919-1-" + i0 + "," + ("e9b0a919-0-" + i0),
            e: "e9b0a919-0-" + i0
          };
        }),
        b: common_vendor.p({
          type: "info",
          size: "18",
          color: "#999"
        }),
        c: common_vendor.o(($event) => _ctx.actionsClick("分享")),
        d: common_vendor.p({
          type: "close",
          size: "18",
          color: "#999"
        }),
        e: common_vendor.o(($event) => _ctx.actionsClick("点赞")),
        f: common_vendor.p({
          type: "chatbubble",
          size: "18",
          color: "#999"
        }),
        g: common_vendor.o(($event) => _ctx.actionsClick("评论")),
        h: common_vendor.p({
          title: "这是一个基础卡片示例，此示例展示了一个标题加标题额外信息的标准卡片。",
          extra: "2024:02:10 22:22"
        }),
        i: common_vendor.p({
          title: "卡片标题+额外信息",
          type: "line"
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e9b0a919"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/user/feedback/feedback.js.map
