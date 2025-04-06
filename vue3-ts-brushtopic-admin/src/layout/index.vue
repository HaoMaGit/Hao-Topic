<script setup lang="ts">
import { useSettingStore } from '@/stores/modules/setting.ts'
import { useUserStore } from '@/stores/modules/user.ts'
// 引入系统设置
const settingStore = useSettingStore()
// 引入用户信息
const userStore = useUserStore()
// 引入Logo
import Logo from './logo/index.vue'
// 菜单项
import Menu from './menu/index.vue'
// 顶部导航
import Tabbar from './tabbar/index.vue'
</script>
<template>
  <div class="layout_container">
    <!-- 左侧菜单 -->
    <div class="layout_slider" :class="{ fold: settingStore.fold ? true : false }">
      <Logo />
      <!-- 展示菜单 -->
      <!-- 滚动组件 -->
      <div class="scrollbar">
        <!-- 菜单组件 -->
        <!-- 根据路由动态生成菜单 -->
        <Menu :menuList="userStore.userInfo.menuList" />
      </div>
    </div>
    <!-- 顶部导航 -->
    <div class="layout_tabbar" :class="{ fold: settingStore.fold ? true : false }">
      <!-- layout组件的顶部导航tabbar -->
      <Tabbar />
    </div>
    <!-- 内容展示区域 -->
    <div class="layout_main" :class="{ fold: settingStore.fold ? true : false }">
      <a-card style="height: 100%;">
        <!-- 二级菜单 -->
        <router-view></router-view>
      </a-card>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.layout_container {
  width: 100%;
  height: 100vh;

  .layout_slider {
    color: white;
    width: $base-menu-width;
    height: 100vh;
    background: $base-menu-background;
    transition: all 0.3s;

    &.fold {
      width: $base-menu-min-width;
    }

    .scrollbar {
      width: 100%;
      height: calc(100vh - $base-menu-logo-height);
    }
  }

  .layout_tabbar {
    position: fixed;
    width: calc(100% - $base-menu-width);
    height: $base-tabbar-height;
    top: 0px;
    left: $base-menu-width;
    transition: all 0.3s;
    border-bottom: 1px solid #dcdfe6;

    &.fold {
      width: calc(100vw - $base-menu-min-width);
      left: $base-menu-min-width;
    }
  }

  .layout_main {
    position: absolute;
    width: calc(100% - $base-menu-width);
    height: calc(100vh - $base-tabbar-height);
    left: $base-menu-width;
    top: $base-tabbar-height;
    padding: 12px;
    overflow: auto;
    transition: all 0.3s;
    background-color: $base-content-background;

    &.fold {
      width: calc(100vw - $base-menu-min-width);
      left: $base-menu-min-width;
    }
  }
}
</style>
