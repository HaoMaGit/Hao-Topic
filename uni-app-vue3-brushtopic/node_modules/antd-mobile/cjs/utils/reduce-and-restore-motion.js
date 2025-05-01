"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.isMotionReduced = isMotionReduced;
exports.reduceMotion = reduceMotion;
exports.restoreMotion = restoreMotion;
exports.useMotionReduced = useMotionReduced;
var _web = require("@react-spring/web");
var _shim = require("use-sync-external-store/shim");
let reduced = false;
const subscribers = new Set();
function notify() {
  subscribers.forEach(subscriber => {
    subscriber();
  });
}
function reduceMotion() {
  reduced = true;
  notify();
  _web.Globals.assign({
    skipAnimation: true
  });
}
function restoreMotion() {
  reduced = false;
  notify();
  _web.Globals.assign({
    skipAnimation: false
  });
}
function isMotionReduced() {
  return reduced;
}
function subscribe(onStoreChange) {
  subscribers.add(onStoreChange);
  return () => {
    subscribers.delete(onStoreChange);
  };
}
function useMotionReduced() {
  return (0, _shim.useSyncExternalStore)(subscribe, isMotionReduced, isMotionReduced);
}