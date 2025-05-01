"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
require("./result-page.css");
var _resultPage2 = require("./result-page");
var _attachPropertiesToComponent = require("../../utils/attach-properties-to-component");
var _resultPageCard = require("./result-page-card");
var _default = (0, _attachPropertiesToComponent.attachPropertiesToComponent)(_resultPage2.ResultPage, {
  Card: _resultPageCard.ResultPageCard
});
exports.default = _default;