"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const uni_modules_uvUiTools_libs_function_index = require("../../uni_modules/uv-ui-tools/libs/function/index.js");
if (!Array) {
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  const _easycom_uni_popup2 = common_vendor.resolveComponent("uni-popup");
  const _easycom_uv_input2 = common_vendor.resolveComponent("uv-input");
  (_easycom_uni_list_item2 + _easycom_uni_list2 + _easycom_uni_popup2 + _easycom_uv_input2)();
}
const _easycom_uni_list_item = () => "../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../uni_modules/uni-list/components/uni-list/uni-list.js";
const _easycom_uni_popup = () => "../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
const _easycom_uv_input = () => "../../uni_modules/uv-input/components/uv-input/uv-input.js";
if (!Math) {
  (_easycom_uni_list_item + _easycom_uni_list + _easycom_uni_popup + _easycom_uv_input)();
}
const _sfc_main = {
  __name: "login",
  setup(__props) {
    const loginWay = common_vendor.ref(0);
    const phoneValue = common_vendor.ref("");
    const qqValue = common_vendor.ref("");
    const userValue = common_vendor.ref("");
    common_vendor.ref("");
    const totalSecond = common_vendor.ref(60);
    const second = common_vendor.ref(60);
    const forgetPopup = common_vendor.ref();
    const codeValue = common_vendor.ref("");
    let timer = null;
    const getCode = () => {
      if (!timer && second.value === totalSecond.value) {
        common_vendor.index.__f__("log", "at pages/login/login.vue:25", "开始倒计时");
        timer = setInterval(() => {
          second.value--;
          if (second.value <= 0) {
            clearInterval(timer);
            timer = null;
            second.value = totalSecond.value;
          }
        }, 1e3);
        uni_modules_uvUiTools_libs_function_index.toast("验证码已发送");
      }
    };
    common_vendor.onUnmounted(() => {
      clearInterval(timer);
    });
    return (_ctx, _cache) => {
      var _a;
      return common_vendor.e({
        a: common_vendor.p({
          title: "手机号验证",
          clickable: true
        }),
        b: common_vendor.p({
          title: "邮箱验证",
          clickable: true
        }),
        c: common_vendor.o(($event) => forgetPopup.value.close()),
        d: common_vendor.p({
          title: "取消",
          clickable: true
        }),
        e: common_vendor.sr(forgetPopup, "e4e4508d-0", {
          "k": "forgetPopup"
        }),
        f: common_vendor.p({
          ["mask-click"]: false,
          ["background-color"]: "#fff",
          type: "bottom",
          ["border-radius"]: "10px 10px 0 0"
        }),
        g: common_vendor.t(loginWay.value === 1 ? "手机快捷登录" : loginWay.value === 2 ? "QQ邮箱登录" : "欢迎登录"),
        h: common_vendor.t(loginWay.value === 1 ? "手机号" : loginWay.value === 2 ? "邮箱" : "账号"),
        i: loginWay.value === 0
      }, loginWay.value === 0 ? {
        j: common_vendor.o(($event) => userValue.value = $event),
        k: common_vendor.p({
          placeholder: "请输入手机号或者邮箱",
          modelValue: userValue.value
        })
      } : {}, {
        l: loginWay.value === 1
      }, loginWay.value === 1 ? {
        m: common_vendor.o(($event) => phoneValue.value = $event),
        n: common_vendor.p({
          placeholder: "请输入手机号",
          modelValue: phoneValue.value
        })
      } : {}, {
        o: loginWay.value === 2
      }, loginWay.value === 2 ? {
        p: common_vendor.o(($event) => qqValue.value = $event),
        q: common_vendor.p({
          placeholder: "请输入邮箱",
          modelValue: qqValue.value
        })
      } : {}, {
        r: common_vendor.t(loginWay.value !== 1 ? "密码" : "验证码"),
        s: loginWay.value !== 1
      }, loginWay.value !== 1 ? {
        t: common_vendor.o(($event) => phoneValue.value = $event),
        v: common_vendor.p({
          placeholder: "请输入密码",
          modelValue: phoneValue.value
        })
      } : {}, {
        w: loginWay.value === 1
      }, loginWay.value === 1 ? {
        x: common_vendor.t(second.value === totalSecond.value ? "获取验证码" : `重新获取${second.value}秒`),
        y: ((_a = phoneValue.value) == null ? void 0 : _a.length) === 11 ? 1 : "",
        z: common_vendor.o(($event) => getCode()),
        A: common_vendor.o(($event) => codeValue.value = $event),
        B: common_vendor.p({
          placeholder: "请输入短信验证码",
          maxlength: "6",
          modelValue: codeValue.value
        })
      } : {}, {
        C: common_vendor.o(($event) => forgetPopup.value.open()),
        D: loginWay.value !== 0
      }, loginWay.value !== 0 ? {
        E: common_vendor.o(($event) => loginWay.value = 0)
      } : {}, {
        F: common_assets._imports_0$3,
        G: common_vendor.o(($event) => loginWay.value = 1),
        H: common_assets._imports_1$1,
        I: common_vendor.o(($event) => loginWay.value = 2)
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e4e4508d"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/login.js.map
