"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_list_chat2 = common_vendor.resolveComponent("uni-list-chat");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  (_easycom_uni_icons2 + _easycom_uni_list_chat2 + _easycom_uni_list2)();
}
const _easycom_uni_icons = () => "../../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_list_chat = () => "../../../uni_modules/uni-list/components/uni-list-chat/uni-list-chat.js";
const _easycom_uni_list = () => "../../../uni_modules/uni-list/components/uni-list/uni-list.js";
if (!Math) {
  (_easycom_uni_icons + _easycom_uni_list_chat + _easycom_uni_list)();
}
const _sfc_main = {
  __name: "list",
  setup(__props) {
    const navBarTitle = common_vendor.ref("");
    common_vendor.onLoad((options) => {
      const type = options.type !== void 0 ? options.type : "0";
      if (type === "0") {
        navBarTitle.value = "我的收藏";
      } else if (type === "1") {
        navBarTitle.value = "我的记录";
      } else {
        navBarTitle.value = "默认标题";
      }
      common_vendor.index.setNavigationBarTitle({
        title: navBarTitle.value
      });
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(9, (item, k0, i0) => {
          return common_vendor.e(navBarTitle.value === "我的收藏" ? {
            a: "93f6d85e-2-" + i0 + "," + ("93f6d85e-1-" + i0),
            b: common_vendor.p({
              type: "star-filled",
              color: "#fe8d59",
              size: "24"
            })
          } : {}, {
            c: "93f6d85e-1-" + i0 + ",93f6d85e-0"
          });
        }),
        b: navBarTitle.value === "我的收藏",
        c: common_vendor.p({
          avatar: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-dc-site/460d46d0-4fcc-11eb-8ff1-d5dcf8779628.png",
          title: "你认为Java的优势是什么？",
          note: "Java JavaSe Mysql",
          ["badge-positon"]: "left"
        }),
        d: common_vendor.p({
          border: true
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-93f6d85e"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/user/list/list.js.map
