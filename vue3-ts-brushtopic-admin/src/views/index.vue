<script setup lang="ts">
import { useUserStore } from '@/stores/modules/user';
import AdminIndex from './adminIndex.vue';
import CommonIndex from './commonIndex.vue';
const userStore = useUserStore()
// 获取当前用户身份
const identity = userStore.userInfo.identity
console.log('身份', identity);

</script>
<template>
  <div class="index-body">
    <!-- 管理员首页 -->
    <template v-if="identity === 2">
      <AdminIndex />
    </template>
    <!-- 通用的首页 -->
    <template v-else>
      <CommonIndex />
    </template>
  </div>
</template>
<style lang="scss" scoped></style>
