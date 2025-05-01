<script setup lang="ts">
import Setting from './setting/index.vue'
import Breadcrumb from '@/layout/tabbar/breadcrumb/index.vue'
</script>
<template>
  <div class="tabbar">
    <!-- 左侧面包屑  -->
    <div class="tabbar-left">
      <Breadcrumb />
    </div>
    <!-- 右侧设置 -->
    <div class="tabbar-right">
      <Setting />
    </div>
  </div>
</template>
<style lang="scss" scoped>
.tabbar {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
  background-color: $base-tabbar-background !important;

  .tabbar-left {
    display: flex;
    align-items: center;
  }

  .tabbar-right {
    display: flex;
    align-items: center;
  }
}
</style>
