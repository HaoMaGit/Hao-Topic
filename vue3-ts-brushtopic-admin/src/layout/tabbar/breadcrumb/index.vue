<script setup lang="ts">
import { useSettingStore } from '@/stores/modules/setting.js';
import { RightOutlined, MenuFoldOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue';
// 实例化设置仓库
const settingStore = useSettingStore();
import { useRoute } from 'vue-router';
// 实例化路由
const $route = useRoute();

// 折叠图标
const changeIcon = () => {
  settingStore.fold = !settingStore.fold
}
</script>
<template>
  <!-- 折叠图标 -->
  <div class="icon-box" @click="changeIcon">
    <MenuFoldOutlined v-if="!settingStore.fold" />
    <MenuUnfoldOutlined v-else />
  </div>
  <!-- 面包屑 -->
  <a-breadcrumb>
    <template #separator>
      <RightOutlined />
    </template>
    <!-- 面包屑的每一项 -->
    <a-breadcrumb-item v-for="(item, index) in $route.matched" :key="index" v-show="item.meta.title" :to="item.path">
      <span>{{ item.meta.title }}</span>
    </a-breadcrumb-item>
  </a-breadcrumb>
</template>
<style lang="scss" scoped>
.icon-box {
  margin: 0 12px 0 5px;
}
</style>
