"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
if (!Array) {
  const _easycom_uv_input2 = common_vendor.resolveComponent("uv-input");
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  (_easycom_uv_input2 + _easycom_uni_list_item2 + _easycom_uni_list2)();
}
const _easycom_uv_input = () => "../../uni_modules/uv-input/components/uv-input/uv-input.js";
const _easycom_uni_list_item = () => "../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../uni_modules/uni-list/components/uni-list/uni-list.js";
if (!Math) {
  (_easycom_uv_input + _easycom_uni_list_item + _easycom_uni_list)();
}
const _sfc_main = {
  __name: "category",
  setup(__props) {
    const activeIndex = common_vendor.ref(0);
    const handlerMenu = (index) => {
      common_vendor.index.showLoading({
        title: "加载中"
      });
      activeIndex.value = index;
      setTimeout(function() {
        common_vendor.index.hideLoading();
      }, 2e3);
    };
    common_vendor.ref();
    const goToSubject = (item) => {
      common_vendor.index.__f__("log", "at pages/category/category.vue:20", 1);
      common_vendor.index.navigateTo({
        url: `/pages/category/subject/subject?id=1&name=Java专题练习`
      });
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          placeholder: "前置图标",
          prefixIcon: "search",
          prefixIconStyle: "font-size: 22px;color: #909399"
        }),
        b: common_vendor.f(10, (item, index, i0) => {
          return {
            a: activeIndex.value === index ? 1 : "",
            b: index,
            c: common_vendor.o(($event) => handlerMenu(index), index)
          };
        }),
        c: common_vendor.o((...args) => _ctx.scroll && _ctx.scroll(...args)),
        d: common_vendor.f(15, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => goToSubject()),
            b: "8145b772-2-" + i0 + ",8145b772-1"
          };
        }),
        e: common_vendor.unref(common_assets.Java),
        f: common_vendor.p({
          showArrow: true,
          clickable: true
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8145b772"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/category/category.js.map
