"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const uni_modules_uvUiTools_libs_function_index = require("../../uni_modules/uv-ui-tools/libs/function/index.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uv_modal2 = common_vendor.resolveComponent("uv-modal");
  const _easycom_uni_popup2 = common_vendor.resolveComponent("uni-popup");
  const _easycom_uni_popup_dialog2 = common_vendor.resolveComponent("uni-popup-dialog");
  const _easycom_uni_file_picker2 = common_vendor.resolveComponent("uni-file-picker");
  const _component_uni_section = common_vendor.resolveComponent("uni-section");
  (_easycom_uni_icons2 + _easycom_uv_modal2 + _easycom_uni_popup2 + _easycom_uni_popup_dialog2 + _easycom_uni_file_picker2 + _component_uni_section)();
}
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uv_modal = () => "../../uni_modules/uv-modal/components/uv-modal/uv-modal.js";
const _easycom_uni_popup = () => "../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
const _easycom_uni_popup_dialog = () => "../../uni_modules/uni-popup/components/uni-popup-dialog/uni-popup-dialog.js";
const _easycom_uni_file_picker = () => "../../uni_modules/uni-file-picker/components/uni-file-picker/uni-file-picker.js";
if (!Math) {
  (_easycom_uni_icons + _easycom_uv_modal + _easycom_uni_popup + _easycom_uni_popup_dialog + _easycom_uni_file_picker)();
}
const _sfc_main = {
  __name: "user",
  setup(__props) {
    const memberModal = common_vendor.ref();
    const unlockMember = () => {
      memberModal.value.open();
    };
    const goToPay = () => {
      memberModal.value.close();
      common_vendor.index.showLoading({
        title: "支付成功"
      });
      setTimeout(function() {
        common_vendor.index.hideLoading();
      }, 200);
    };
    const contactUsPopup = common_vendor.ref();
    const contactUs = () => {
      contactUsPopup.value.open();
    };
    const feedbackPopup = common_vendor.ref();
    const dialogInputConfirm = () => {
      uni_modules_uvUiTools_libs_function_index.toast("提交成功");
    };
    const avatarPreview = common_vendor.ref([{
      url: "https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/shuijiao-small.jpg",
      extname: "png",
      name: "shuijiao.png"
    }]);
    const imageStyles = common_vendor.ref({
      width: 110,
      height: 110,
      border: {
        radius: "50%"
      }
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          type: "vip",
          size: "38",
          color: "#f3d5c0"
        }),
        b: common_vendor.o(unlockMember),
        c: common_vendor.sr(memberModal, "0f7520f0-0", {
          "k": "memberModal"
        }),
        d: common_vendor.o(goToPay),
        e: common_vendor.p({
          title: "会员服务",
          confirmText: "去支付"
        }),
        f: common_assets._imports_0$2,
        g: common_vendor.sr(contactUsPopup, "0f7520f0-2", {
          "k": "contactUsPopup"
        }),
        h: common_vendor.p({
          ["background-color"]: "#fff"
        }),
        i: common_vendor.sr("inputClose", "0f7520f0-4,0f7520f0-3"),
        j: common_vendor.o(dialogInputConfirm),
        k: common_vendor.p({
          mode: "input",
          title: "意见反馈",
          value: "对话框预置提示内容!",
          placeholder: "请输入反馈内容"
        }),
        l: common_vendor.sr(feedbackPopup, "0f7520f0-3", {
          "k": "feedbackPopup"
        }),
        m: common_vendor.p({
          type: "dialog"
        }),
        n: common_vendor.p({
          readonly: true,
          value: avatarPreview.value,
          imageStyles: imageStyles.value,
          ["file-mediatype"]: "image"
        }),
        o: common_vendor.p({
          title: "自定义图片大小",
          type: "line"
        }),
        p: common_vendor.p({
          type: "vip-filled",
          size: "28",
          color: "#00a9e0"
        }),
        q: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        r: common_vendor.o(unlockMember),
        s: common_vendor.p({
          type: "star-filled",
          size: "28",
          color: "#00a9e0"
        }),
        t: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        v: common_vendor.p({
          type: "eye-filled",
          size: "28",
          color: "#00a9e0"
        }),
        w: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        x: common_vendor.p({
          type: "eye-filled",
          size: "28",
          color: "#00a9e0"
        }),
        y: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        z: common_vendor.p({
          type: "gear-filled",
          size: "28",
          color: "#00a9e0"
        }),
        A: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        B: common_vendor.p({
          type: "staff-filled",
          size: "28",
          color: "#00a9e0"
        }),
        C: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        D: common_vendor.o(contactUs),
        E: common_vendor.p({
          type: "paperplane",
          size: "28",
          color: "#00a9e0"
        }),
        F: common_vendor.p({
          type: "right",
          size: "22",
          color: "#a6a6a6"
        }),
        G: common_vendor.o(($event) => feedbackPopup.value.open())
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-0f7520f0"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/user/user.js.map
