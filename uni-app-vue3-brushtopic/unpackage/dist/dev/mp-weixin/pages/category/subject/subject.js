"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
if (!Array) {
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  (_easycom_uni_list_item2 + _easycom_uni_list2)();
}
const _easycom_uni_list_item = () => "../../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../../uni_modules/uni-list/components/uni-list/uni-list.js";
if (!Math) {
  (_easycom_uni_list_item + _easycom_uni_list)();
}
const _sfc_main = {
  __name: "subject",
  setup(__props) {
    common_vendor.onLoad((options) => {
      common_vendor.index.__f__("log", "at pages/category/subject/subject.vue:11", options.name);
      common_vendor.index.__f__("log", "at pages/category/subject/subject.vue:12", options.id);
      common_vendor.index.setNavigationBarTitle({
        title: options.name
      });
    });
    const goToTopic = (item) => {
      common_vendor.index.navigateTo({
        url: `/pages/category/topic/topic?id=1&name=Java专题练习`
      });
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.unref(common_assets.Java),
        b: common_vendor.f(30, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => goToTopic()),
            b: "2b623d09-1-" + i0 + ",2b623d09-0"
          };
        }),
        c: common_vendor.p({
          showArrow: true,
          clickable: true
        }),
        d: common_vendor.o((...args) => _ctx.upper && _ctx.upper(...args)),
        e: common_vendor.o((...args) => _ctx.lower && _ctx.lower(...args)),
        f: common_vendor.o((...args) => _ctx.scroll && _ctx.scroll(...args))
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-2b623d09"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/category/subject/subject.js.map
