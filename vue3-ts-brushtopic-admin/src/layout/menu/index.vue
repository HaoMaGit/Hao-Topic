<script setup lang="ts">
import { useSettingStore } from '@/stores/modules/setting.ts'
import { useUserStore } from '@/stores/modules/user.ts'
// 引入系统设置
const settingStore = useSettingStore()
// 引入用户信息
const userStore = useUserStore()
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import type { MenuProps } from 'ant-design-vue';
// 获取路由路径的
const $route = useRoute()
// 操作路由实例
const $router = useRouter()
defineProps(['menuList'])
//点击菜单的回调
const handleClick: MenuProps['onClick'] = menuInfo => {
  console.log(menuInfo)
  $router.push(String(menuInfo.key))
};
// 当前选中的路由
const selectedKeys = ref([$route.path])

</script>
<template>
  <a-menu class="custom-menu" :items="userStore.userInfo.menuList" v-model:selectedKeys="selectedKeys"
    @click="handleClick" :inline-collapsed="settingStore.fold" :theme="settingStore.isDark ? 'dark' : 'light'">
  </a-menu>
</template>
<style lang="scss" scoped>
.custom-menu {
  ::v-deep(.ant-menu-item-selected) {
    color: $base-menu-active-color !important;
    background-color: $base-menu-active-background !important;
  }
}
</style>
