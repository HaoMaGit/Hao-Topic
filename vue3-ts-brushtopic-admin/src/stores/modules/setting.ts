import {
  defineStore
} from "pinia";
import {
  ref
} from 'vue'
export const useSettingStore = defineStore('setting', () => {
  // 左侧菜单是否折叠
  const fold = ref(false)
  // 主题颜色
  const themeColor = ref('#1677ff')
  // 是否为暗黑模式
  const isDark = ref(false)
  // 紧凑模式
  const isCompact = ref(false)
  // 判断本地是否存储了主题配置
  const theme = localStorage.getItem("settingTheme")
  // 解析
  if (theme) {
    // 有配置
    const jsonTheme = JSON.parse(theme)
    // 将本地配置粗才能到仓库中
    fold.value = jsonTheme.fold
    themeColor.value = jsonTheme.themeColor
    isDark.value = jsonTheme.isDark
    isCompact.value = jsonTheme.isCompact
  } else {
    // 没有配置
  }
  return {
    fold,
    themeColor,
    isDark,
    isCompact
  }
})