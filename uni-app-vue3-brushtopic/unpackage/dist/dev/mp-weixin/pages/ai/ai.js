"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const uni_modules_uvUiTools_libs_function_index = require("../../uni_modules/uv-ui-tools/libs/function/index.js");
if (!Array) {
  const _easycom_uni_list_chat2 = common_vendor.resolveComponent("uni-list-chat");
  const _easycom_uv_input2 = common_vendor.resolveComponent("uv-input");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  (_easycom_uni_list_chat2 + _easycom_uv_input2 + _easycom_uni_icons2)();
}
const _easycom_uni_list_chat = () => "../../uni_modules/uni-list/components/uni-list-chat/uni-list-chat.js";
const _easycom_uv_input = () => "../../uni_modules/uv-input/components/uv-input/uv-input.js";
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  (_easycom_uni_list_chat + _easycom_uv_input + _easycom_uni_icons)();
}
const _sfc_main = {
  __name: "ai",
  setup(__props) {
    const aiValue = common_vendor.ref("");
    const isSend = common_vendor.ref(false);
    const dialogue = () => {
      if (aiValue.value.trim("") === "") {
        uni_modules_uvUiTools_libs_function_index.toast("请输入您的问题");
      } else {
        uni_modules_uvUiTools_libs_function_index.toast("发送成功");
        isSend.value = true;
      }
    };
    const cancelDialogue = () => {
      isSend.value = false;
    };
    const aiSetting = common_vendor.ref(true);
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: aiSetting.value ? 1 : "",
        b: common_vendor.o(($event) => aiSetting.value = true),
        c: !aiSetting.value ? 1 : "",
        d: common_vendor.o(($event) => aiSetting.value = false),
        e: aiSetting.value
      }, aiSetting.value ? {
        f: common_vendor.f(10, (item, k0, i0) => {
          return {};
        }),
        g: common_vendor.o((...args) => _ctx.upper && _ctx.upper(...args))
      } : {
        h: common_vendor.f(15, (item, index, i0) => {
          return {
            a: "fdb58938-0-" + i0
          };
        }),
        i: common_vendor.p({
          clickable: true,
          title: "测试标题撒旦发射点发射点111111",
          avatar: common_vendor.unref(common_assets._imports_0$1),
          note: "您收到一条新的消息您收到一条新的消息您收到一条新的消息您收到一条新的消息您收到一条新的消息",
          time: "2020-02-02 12:24",
          ["badge-positon"]: "left"
        }),
        j: common_vendor.o((...args) => _ctx.upper && _ctx.upper(...args))
      }, {
        k: aiSetting.value
      }, aiSetting.value ? common_vendor.e({
        l: common_assets._imports_0$1,
        m: !isSend.value
      }, !isSend.value ? {
        n: common_vendor.o(($event) => dialogue()),
        o: common_assets._imports_1
      } : {
        p: common_vendor.o(($event) => cancelDialogue()),
        q: common_assets._imports_2
      }, {
        r: common_vendor.o(($event) => aiValue.value = $event),
        s: common_vendor.p({
          placeholder: "对话",
          modelValue: aiValue.value
        })
      }) : {
        t: common_assets._imports_0$1,
        v: common_vendor.p({
          type: "contact",
          color: "#00a0e9",
          size: "38"
        }),
        w: common_vendor.o(($event) => aiValue.value = $event),
        x: common_vendor.p({
          placeholder: "搜索我的历史",
          modelValue: aiValue.value
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-fdb58938"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/ai/ai.js.map
