"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  const _easycom_uni_drawer2 = common_vendor.resolveComponent("uni-drawer");
  const _easycom_uni_tag2 = common_vendor.resolveComponent("uni-tag");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_zero_markdown_view2 = common_vendor.resolveComponent("zero-markdown-view");
  (_easycom_uni_list_item2 + _easycom_uni_list2 + _easycom_uni_drawer2 + _easycom_uni_tag2 + _easycom_uni_icons2 + _easycom_zero_markdown_view2)();
}
const _easycom_uni_list_item = () => "../../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../../uni_modules/uni-list/components/uni-list/uni-list.js";
const _easycom_uni_drawer = () => "../../../uni_modules/uni-drawer/components/uni-drawer/uni-drawer.js";
const _easycom_uni_tag = () => "../../../uni_modules/uni-tag/components/uni-tag/uni-tag.js";
const _easycom_uni_icons = () => "../../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_zero_markdown_view = () => "../../../uni_modules/zero-markdown-view/components/zero-markdown-view/zero-markdown-view.js";
if (!Math) {
  (_easycom_uni_list_item + _easycom_uni_list + _easycom_uni_drawer + _easycom_uni_tag + _easycom_uni_icons + _easycom_zero_markdown_view)();
}
const _sfc_main = {
  __name: "topic",
  setup(__props) {
    common_vendor.onLoad((options) => {
      common_vendor.index.__f__("log", "at pages/category/topic/topic.vue:10", options.name);
      common_vendor.index.__f__("log", "at pages/category/topic/topic.vue:11", options.id);
      common_vendor.index.setNavigationBarTitle({
        title: options.name
      });
    });
    const isTabs = common_vendor.ref(true);
    const isShowAnswer = common_vendor.ref(false);
    const showAnswer = () => {
      isShowAnswer.value = true;
    };
    const markDownContent = common_vendor.ref(
      "String内部维护的是private final char byte数组， 不可变线程安全 好处 防止被恶意篡改 作为HashMap的key可以保证不可变性 可以实现字符串常量池， 在Java中， 创建字符串对象的方式 通过字符串常量进行创建 在字符串常量池判断是否存在， 如果存在就返回， 不存在就在字符串常量池创建后返回 通过new字符串对象进行创建 在字符串常量池中判断是否存在， 如果不存在就创建， 再判断堆中是否存在， 如果不存在就创建， 然后返回该对象， 总之要保证字符串常量池和堆中都有该对象 String、 StringBuilder、 StringBuffer的区别 String内部维护的是private final char"
    );
    const showRight = common_vendor.ref();
    const nextQuestion = () => {
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(50, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => _ctx.goToTopic(item)),
            b: "ab609117-2-" + i0 + ",ab609117-1"
          };
        }),
        b: common_vendor.p({
          showArrow: true,
          clickable: true
        }),
        c: common_vendor.sr(showRight, "ab609117-0", {
          "k": "showRight"
        }),
        d: common_vendor.p({
          mode: "right",
          width: "360"
        }),
        e: common_vendor.p({
          inverted: true,
          text: "Java基础"
        }),
        f: common_vendor.p({
          inverted: true,
          text: "Java"
        }),
        g: common_vendor.p({
          type: "star",
          color: "#999",
          size: "24"
        }),
        h: isTabs.value ? 1 : "",
        i: common_vendor.o(($event) => isTabs.value = true),
        j: !isTabs.value ? 1 : "",
        k: common_vendor.o(($event) => isTabs.value = false),
        l: !isShowAnswer.value
      }, !isShowAnswer.value ? {
        m: common_vendor.o(showAnswer)
      } : {
        n: common_vendor.p({
          markdown: markDownContent.value
        })
      }, {
        o: common_vendor.p({
          type: "list",
          color: "#21b5ff",
          size: "19"
        }),
        p: common_vendor.o(($event) => showRight.value.open()),
        q: common_vendor.p({
          type: "left",
          size: "30",
          color: "#999"
        }),
        r: common_vendor.p({
          type: "compose",
          size: "33",
          color: "#10aeff"
        }),
        s: common_vendor.p({
          type: "right",
          size: "30",
          color: "#10aeff"
        }),
        t: common_vendor.o(nextQuestion)
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ab609117"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/category/topic/topic.js.map
