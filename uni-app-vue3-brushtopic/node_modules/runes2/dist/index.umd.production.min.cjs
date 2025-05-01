!function(e, i) {
  "object" == typeof exports && "undefined" != typeof module ? i(exports) : "function" == typeof define && define.amd ? define([ "exports" ], i) : i((e = "undefined" != typeof globalThis ? globalThis : e || self).Runes2 = {});
}(this, (function(e) {
  "use strict";
  var i;
  e.EnumRunesCode = void 0, (i = e.EnumRunesCode || (e.EnumRunesCode = {}))[i.HIGH_SURROGATE_START = 55296] = "HIGH_SURROGATE_START", 
  i[i.HIGH_SURROGATE_END = 56319] = "HIGH_SURROGATE_END", i[i.LOW_SURROGATE_START = 56320] = "LOW_SURROGATE_START", 
  i[i.REGIONAL_INDICATOR_START = 127462] = "REGIONAL_INDICATOR_START", i[i.REGIONAL_INDICATOR_END = 127487] = "REGIONAL_INDICATOR_END", 
  i[i.FITZPATRICK_MODIFIER_START = 127995] = "FITZPATRICK_MODIFIER_START", i[i.FITZPATRICK_MODIFIER_END = 127999] = "FITZPATRICK_MODIFIER_END", 
  i[i.VARIATION_MODIFIER_START = 65024] = "VARIATION_MODIFIER_START", i[i.VARIATION_MODIFIER_END = 65039] = "VARIATION_MODIFIER_END", 
  i[i.DIACRITICAL_MARKS_START = 8400] = "DIACRITICAL_MARKS_START", i[i.DIACRITICAL_MARKS_END = 8447] = "DIACRITICAL_MARKS_END", 
  i[i.SUBDIVISION_INDICATOR_START = 127988] = "SUBDIVISION_INDICATOR_START", i[i.TAGS_START = 917504] = "TAGS_START", 
  i[i.TAGS_END = 917631] = "TAGS_END", i[i.ZWJ = 8205] = "ZWJ";
  const n = Object.freeze([ 0x0308, 0x0937, 0x093F, 0x0BA8, 0x0BBF, 0x0BCD, 0x0E31, 0x0E33, 0x0E40, 0x0E49, 0x1100, 0x1161, 0x11A8 ]);
  var t;
  function runes(e) {
    if ("string" != typeof e) throw new TypeError("string cannot be undefined or null");
    const i = [];
    let n = 0, t = 0;
    for (;n < e.length; ) t += nextUnits(n + t, e), isGrapheme(e[n + t]) && t++, isVariationSelector(e[n + t]) && t++, 
    isDiacriticalMark(e[n + t]) && t++, isZeroWidthJoiner(e[n + t]) ? t++ : (i.push(e.substring(n, n + t)), 
    n += t, t = 0);
    return i;
  }
  function nextUnits(e, i) {
    const n = i[e];
    if (!isFirstOfSurrogatePair(n) || e === i.length - 1) return 1;
    const t = n + i[e + 1];
    let r = i.substring(e + 2, e + 5);
    return isRegionalIndicator(t) && isRegionalIndicator(r) ? 4 : isSubdivisionFlag(t) && isSupplementarySpecialpurposePlane(r) ? i.slice(e).indexOf(String.fromCodePoint(917631)) + 2 : isFitzpatrickModifier(r) ? 4 : 2;
  }
  function isFirstOfSurrogatePair(e) {
    return e && betweenInclusive(e[0].charCodeAt(0), 55296, 56319);
  }
  function isRegionalIndicator(e) {
    return betweenInclusive(codePointFromSurrogatePair(e), 127462, 127487);
  }
  function isSubdivisionFlag(e) {
    return betweenInclusive(codePointFromSurrogatePair(e), 127988, 127988);
  }
  function isFitzpatrickModifier(e) {
    return betweenInclusive(codePointFromSurrogatePair(e), 127995, 127999);
  }
  function isVariationSelector(e) {
    return "string" == typeof e && betweenInclusive(e.charCodeAt(0), 65024, 65039);
  }
  function isDiacriticalMark(e) {
    return "string" == typeof e && betweenInclusive(e.charCodeAt(0), 8400, 8447);
  }
  function isSupplementarySpecialpurposePlane(e) {
    const i = e.codePointAt(0);
    return "string" == typeof e && "number" == typeof i && betweenInclusive(i, 917504, 917631);
  }
  function isGrapheme(e) {
    return "string" == typeof e && n.includes(e.charCodeAt(0));
  }
  function isZeroWidthJoiner(e) {
    return "string" == typeof e && 8205 === e.charCodeAt(0);
  }
  function codePointFromSurrogatePair(e) {
    return (e.charCodeAt(0) - 55296 << 10) + (e.charCodeAt(1) - 56320) + 0x10000;
  }
  function betweenInclusive(e, i, n) {
    return e >= i && e <= n;
  }
  function substring(e, i, n) {
    const t = runes(e);
    if (void 0 === i) return e;
    if (i >= t.length) return "";
    const r = t.length - i;
    let o = i + (void 0 === n ? r : n);
    return o > i + r && (o = void 0), t.slice(i, o).join("");
  }
  e.EnumCodeUnits = void 0, (t = e.EnumCodeUnits || (e.EnumCodeUnits = {}))[t.unit_1 = 1] = "unit_1", 
  t[t.unit_2 = 2] = "unit_2", t[t.unit_4 = 4] = "unit_4", Object.defineProperty(runes, "runes", {
    value: runes
  }), Object.defineProperty(runes, "default", {
    value: runes
  }), Object.defineProperty(runes, "__esModule", {
    value: !0
  }), Object.defineProperty(runes, "substr", {
    value: substring
  }), Object.defineProperty(runes, "substring", {
    value: substring
  }), Object.defineProperty(runes, "EnumRunesCode", {
    value: e.EnumRunesCode
  }), Object.defineProperty(runes, "EnumCodeUnits", {
    value: e.EnumCodeUnits
  }), Object.defineProperty(runes, "GRAPHEMES", {
    value: n
  }), e.GRAPHEMES = n, e.betweenInclusive = betweenInclusive, e.codePointFromSurrogatePair = codePointFromSurrogatePair, 
  e.default = runes, e.isDiacriticalMark = isDiacriticalMark, e.isFirstOfSurrogatePair = isFirstOfSurrogatePair, 
  e.isFitzpatrickModifier = isFitzpatrickModifier, e.isGrapheme = isGrapheme, e.isRegionalIndicator = isRegionalIndicator, 
  e.isSubdivisionFlag = isSubdivisionFlag, e.isSupplementarySpecialpurposePlane = isSupplementarySpecialpurposePlane, 
  e.isVariationSelector = isVariationSelector, e.isZeroWidthJoiner = isZeroWidthJoiner, 
  e.nextUnits = nextUnits, e.runes = runes, e.substr = substring, e.substring = substring, 
  Object.defineProperty(e, "__esModule", {
    value: !0
  });
}));
//# sourceMappingURL=index.umd.production.min.cjs.map
