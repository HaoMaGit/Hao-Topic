"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_list_chat2 = common_vendor.resolveComponent("uni-list-chat");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  (_easycom_uni_icons2 + _easycom_uni_list_chat2 + _easycom_uni_list2)();
}
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_list_chat = () => "../../uni_modules/uni-list/components/uni-list-chat/uni-list-chat.js";
const _easycom_uni_list = () => "../../uni_modules/uni-list/components/uni-list/uni-list.js";
if (!Math) {
  (_easycom_uni_icons + _easycom_uni_list_chat + _easycom_uni_list)();
}
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const avatarList = common_vendor.ref([{
      url: common_assets.Java
    }]);
    const tapRanking = () => {
      common_vendor.index.navigateTo({
        url: "/pages/index/ranking/ranking"
      });
    };
    return (_ctx, _cache) => {
      return {
        a: common_assets._imports_0,
        b: common_vendor.o(tapRanking),
        c: common_vendor.p({
          type: "calendar",
          color: "#eca78c",
          size: "30"
        }),
        d: common_vendor.f(9, (item, k0, i0) => {
          return {
            a: "1cf27b2a-3-" + i0 + "," + ("1cf27b2a-2-" + i0),
            b: "1cf27b2a-2-" + i0 + ",1cf27b2a-1"
          };
        }),
        e: common_vendor.p({
          type: "star-filled",
          color: "#999",
          size: "24"
        }),
        f: common_vendor.p({
          title: "你认为Java的优势是什么？",
          ["avatar-list"]: avatarList.value,
          note: "Java JavaSe Mysql",
          ["badge-positon"]: "left"
        }),
        g: common_vendor.p({
          border: true
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1cf27b2a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
