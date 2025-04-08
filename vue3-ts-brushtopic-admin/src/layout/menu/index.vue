<script setup lang="ts">
import { useSettingStore } from '@/stores/modules/setting.ts'
import { useUserStore } from '@/stores/modules/user.ts'
// 引入系统设置
const settingStore = useSettingStore()
// 引入用户信息
const userStore = useUserStore()
import { ref, h, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import type { MenuProps } from 'ant-design-vue';
import * as AntIcons from '@ant-design/icons-vue';
// 获取路由路径的
const $route = useRoute()
// 操作路由实例
const $router = useRouter()
defineProps(['menuList'])
//点击菜单的回调
const handleClick: MenuProps['onClick'] = menuInfo => {
  console.log(menuInfo);
  $router.push(String(menuInfo.key))
};
// 当前选中的路由
const selectedKeys = ref([$route.path])

// 菜单
const menuAllList = ref<any[]>([])

onMounted(() => {
  // 转换图标字符串为图标组件
  userStore.userInfo.menuList.forEach(item => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const icon = item.icon ? () => h(AntIcons[item.icon]) : null;
    let children = null;
    if (!item.children || item.children.length === 0) {
      children = null;
    } else {
      children = item.children.map(child => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        const icon = child.icon ? () => h(AntIcons[child.icon]) : null;
        return {
          key: child.key,
          label: child.label,
          icon: icon,
        };
      });
    }
    menuAllList.value.push({
      key: item.key,
      label: item.label,
      icon: icon,
      children: children,
    });
  });
});
</script>
<template>
  <a-menu mode="inline" class="custom-menu" :items="menuAllList" v-model:selectedKeys="selectedKeys"
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
