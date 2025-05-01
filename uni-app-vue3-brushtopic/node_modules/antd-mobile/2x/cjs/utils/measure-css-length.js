"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.measureCSSLength = measureCSSLength;
var _isDev = require("./is-dev");
var _devLog = require("./dev-log");
function measureCSSLength(raw) {
  if (raw === null || raw === undefined || raw === '') {
    if (_isDev.isDev) {
      (0, _devLog.devError)('Global', 'Something went wrong when calculating CSS length. Please report an issue at https://github.com/ant-design/ant-design-mobile/issues/new/choose');
    }
    return 0;
  }
  const withUnit = raw.trim();
  if (withUnit.endsWith('px')) {
    return parseFloat(withUnit);
  } else if (withUnit.endsWith('rem')) {
    return parseFloat(withUnit) * parseFloat(window.getComputedStyle(document.documentElement).fontSize);
  } else if (withUnit.endsWith('vw')) {
    return parseFloat(withUnit) * window.innerWidth / 100;
  } else {
    if (_isDev.isDev) {
      (0, _devLog.devError)('Global', `You are using a not supported CSS unit in \`${raw}\`. Only \`px\` \`rem\` and \`vw\` are supported.`);
    }
    return 0;
  }
}