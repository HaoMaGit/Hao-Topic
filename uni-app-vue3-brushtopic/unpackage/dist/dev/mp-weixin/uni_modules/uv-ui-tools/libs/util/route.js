"use strict";
const common_vendor = require("../../../../common/vendor.js");
const uni_modules_uvUiTools_libs_function_index = require("../function/index.js");
class Router {
  constructor() {
    this.config = {
      type: "navigateTo",
      url: "",
      delta: 1,
      // navigateBack页面后退时,回退的层数
      params: {},
      // 传递的参数
      animationType: "pop-in",
      // 窗口动画,只在APP有效
      animationDuration: 300,
      // 窗口动画持续时间,单位毫秒,只在APP有效
      intercept: false,
      // 是否需要拦截
      events: {}
      // 页面间通信接口，用于监听被打开页面发送到当前页面的数据。hbuilderx 2.8.9+ 开始支持。
    };
    this.route = this.route.bind(this);
  }
  // 判断url前面是否有"/"，如果没有则加上，否则无法跳转
  addRootPath(url) {
    return url[0] === "/" ? url : `/${url}`;
  }
  // 整合路由参数
  mixinParam(url, params) {
    url = url && this.addRootPath(url);
    let query = "";
    if (/.*\/.*\?.*=.*/.test(url)) {
      query = uni_modules_uvUiTools_libs_function_index.queryParams(params, false);
      return url += `&${query}`;
    }
    query = uni_modules_uvUiTools_libs_function_index.queryParams(params);
    return url += query;
  }
  // 对外的方法名称
  async route(options = {}, params = {}) {
    let mergeConfig = {};
    if (typeof options === "string") {
      mergeConfig.url = this.mixinParam(options, params);
      mergeConfig.type = "navigateTo";
    } else {
      mergeConfig = uni_modules_uvUiTools_libs_function_index.deepMerge(this.config, options);
      mergeConfig.url = this.mixinParam(options.url, options.params);
    }
    if (mergeConfig.url === uni_modules_uvUiTools_libs_function_index.page())
      return;
    if (params.intercept) {
      mergeConfig.intercept = params.intercept;
    }
    mergeConfig.params = params;
    mergeConfig = uni_modules_uvUiTools_libs_function_index.deepMerge(this.config, mergeConfig);
    if (typeof mergeConfig.intercept === "function") {
      const isNext = await new Promise((resolve, reject) => {
        mergeConfig.intercept(mergeConfig, resolve);
      });
      isNext && this.openPage(mergeConfig);
    } else {
      this.openPage(mergeConfig);
    }
  }
  // 执行路由跳转
  openPage(config) {
    const {
      url,
      type,
      delta,
      animationType,
      animationDuration,
      events
    } = config;
    if (config.type == "navigateTo" || config.type == "to") {
      common_vendor.index.navigateTo({
        url,
        animationType,
        animationDuration,
        events
      });
    }
    if (config.type == "redirectTo" || config.type == "redirect") {
      common_vendor.index.redirectTo({
        url
      });
    }
    if (config.type == "switchTab" || config.type == "tab") {
      common_vendor.index.switchTab({
        url
      });
    }
    if (config.type == "reLaunch" || config.type == "launch") {
      common_vendor.index.reLaunch({
        url
      });
    }
    if (config.type == "navigateBack" || config.type == "back") {
      common_vendor.index.navigateBack({
        delta
      });
    }
  }
}
const route = new Router().route;
exports.route = route;
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/uni_modules/uv-ui-tools/libs/util/route.js.map
