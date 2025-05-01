"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ErrorBlock = void 0;
var _createErrorBlock = require("./create-error-block");
var _images = require("./images");
const imageRecord = {
  'default': _images.defaultImage,
  'disconnected': _images.disconnectedImage,
  'empty': _images.emptyImage,
  'busy': _images.busyImage
};
const ErrorBlock = (0, _createErrorBlock.createErrorBlock)(imageRecord);
exports.ErrorBlock = ErrorBlock;