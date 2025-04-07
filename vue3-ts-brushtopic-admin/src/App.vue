<script setup lang="ts">
import { RouterView } from 'vue-router'
import zhCN from 'ant-design-vue/es/locale/zh_CN';
</script>

<template>
  <a-config-provider :locale="zhCN">
    <!-- 一级路由出口 -->
    <RouterView />
  </a-config-provider>
</template>

<style scoped></style>
