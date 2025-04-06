<script setup lang="ts">
import { useSettingStore } from '@/stores/modules/setting.js';
// 实例化设置仓库
const settingStore = useSettingStore();

</script>
<template>
  <div class="logo" :style="{ 'paddingLeft': settingStore.fold ? '25px' : '15px' }">
    <img src="../../assets/images/logo.png" alt="">
    <p v-if="!settingStore.fold" :class="{ 'text-hidden': settingStore.fold, 'text-preview': !settingStore.fold }">
      易题后台管理系统</p>
  </div>
</template>
<style lang="scss" scoped>
.logo {
  width: 100%;
  height: $base-menu-logo-height;
  color: #1677ff;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;

  img {
    width: 30px;
    height: 30px;
  }

  p {
    font-size: 15px;
    margin-left: 6px;
  }

  .text-hidden {
    opacity: 0;
  }

  .text-preview {
    padding-top: 15px;
    white-space: nowrap;
    overflow: hidden;
  }
}
</style>
