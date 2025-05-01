"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.devError = devError;
exports.devPrint = devPrint;
exports.devWarning = devWarning;
var _isDev = require("./is-dev");
function devWarning(component, message) {
  if (_isDev.isDev) {
    console.warn(`[antd-mobile: ${component}] ${message}`);
  }
}
function devError(component, message) {
  if (_isDev.isDev) {
    console.error(`[antd-mobile: ${component}] ${message}`);
  }
}
let infoBox;
function devPrint(...message) {
  if (_isDev.isDev) {
    if (!infoBox) {
      infoBox = document.createElement('textarea');
      document.body.append(infoBox);
      infoBox.style.position = 'fixed';
      infoBox.style.top = '0';
      infoBox.style.left = '0';
      infoBox.style.width = '100vw';
      infoBox.style.height = '100vh';
      infoBox.style.zIndex = '999999';
      infoBox.style.fontSize = '12px';
      // infoBox.style.opacity = '0.85'
      infoBox.style.pointerEvents = 'none';
      infoBox.style.background = 'transparent';
      infoBox.style.textShadow = '-1px -1px 0 #FFF, -1px 1px 0 #FFF, 1px -1px 0 #FFF, 1px 1px 0 #FFF';
    }
    infoBox.value = `${infoBox.value}\n${message.join(' ')}`.trim();
    infoBox.scrollTop = infoBox.scrollHeight;
  }
}