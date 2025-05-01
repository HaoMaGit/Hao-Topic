"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_uni_popup_dialog2 = common_vendor.resolveComponent("uni-popup-dialog");
  const _easycom_uni_popup2 = common_vendor.resolveComponent("uni-popup");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_file_picker2 = common_vendor.resolveComponent("uni-file-picker");
  const _easycom_uv_button2 = common_vendor.resolveComponent("uv-button");
  (_easycom_uni_popup_dialog2 + _easycom_uni_popup2 + _easycom_uni_icons2 + _easycom_uni_file_picker2 + _easycom_uv_button2)();
}
const _easycom_uni_popup_dialog = () => "../../../uni_modules/uni-popup/components/uni-popup-dialog/uni-popup-dialog.js";
const _easycom_uni_popup = () => "../../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
const _easycom_uni_icons = () => "../../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_file_picker = () => "../../../uni_modules/uni-file-picker/components/uni-file-picker/uni-file-picker.js";
const _easycom_uv_button = () => "../../../uni_modules/uv-button/components/uv-button/uv-button.js";
if (!Math) {
  (_easycom_uni_popup_dialog + _easycom_uni_popup + _easycom_uni_icons + _easycom_uni_file_picker + _easycom_uv_button)();
}
const _sfc_main = {
  __name: "setting",
  setup(__props) {
    const imageStyles = common_vendor.ref({
      "width": 30,
      "height": 30,
      "border": false
      // 是否显示边框
    });
    const uploadSuccess = (e) => {
      common_vendor.index.__f__("log", "at pages/user/setting/setting.vue:14", e);
      common_vendor.index.__f__("log", "at pages/user/setting/setting.vue:15", "上传成功");
    };
    const nameDialog = common_vendor.ref();
    const goToChange = () => {
      common_vendor.index.navigateTo({
        url: "/pages/change/change"
      });
    };
    const alertDialog = common_vendor.ref();
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          type: "error",
          cancelText: "取消",
          confirmText: "清除",
          title: "清除数据",
          content: "确定要清除刷题记录以及所有的数据"
        }),
        b: common_vendor.sr(alertDialog, "ae3eef15-0", {
          "k": "alertDialog"
        }),
        c: common_vendor.p({
          type: "dialog"
        }),
        d: common_vendor.sr("inputClose", "ae3eef15-3,ae3eef15-2"),
        e: common_vendor.o(_ctx.dialogInputConfirm),
        f: common_vendor.p({
          mode: "input",
          title: "修改名称",
          value: "对话框预置提示内容!",
          placeholder: "AI用户"
        }),
        g: common_vendor.sr(nameDialog, "ae3eef15-2", {
          "k": "nameDialog"
        }),
        h: common_vendor.p({
          type: "dialog"
        }),
        i: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        j: common_vendor.o(uploadSuccess),
        k: common_vendor.p({
          limit: "1",
          ["del-icon"]: false,
          ["disable-preview"]: true,
          imageStyles: imageStyles.value,
          ["file-mediatype"]: "image"
        }),
        l: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        m: common_vendor.o(($event) => nameDialog.value.open()),
        n: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        o: common_vendor.o(goToChange),
        p: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        q: common_vendor.o(($event) => alertDialog.value.open()),
        r: common_vendor.p({
          plain: true
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ae3eef15"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/user/setting/setting.js.map
