<script setup lang="ts">
import { h, ref } from 'vue';
import { MailOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons-vue';
import type { MenuProps } from 'ant-design-vue';
const current = ref<string[]>(['mail']);
const items = ref<MenuProps['items']>([
  {
    key: 'mail',
    icon: () => h(MailOutlined),
    label: 'Navigation One',
    title: 'Navigation One',
  },
  {
    key: 'app',
    icon: () => h(AppstoreOutlined),
    label: 'Navigation Two',
    title: 'Navigation Two',
  },
  {
    key: 'sub1',
    icon: () => h(SettingOutlined),
    label: 'Navigation Three - Submenu',
    title: 'Navigation Three - Submenu',
    children: [
      {
        type: 'group',
        label: 'Item 1',
        children: [
          {
            label: 'Option 1',
            key: 'setting:1',
          },
          {
            label: 'Option 2',
            key: 'setting:2',
          },
        ],
      },
      {
        type: 'group',
        label: 'Item 2',
        children: [
          {
            label: 'Option 3',
            key: 'setting:3',
          },
          {
            label: 'Option 4',
            key: 'setting:4',
          },
        ],
      },
    ],
  },
  {
    key: 'alipay',
    label: h('a', { href: 'https://antdv.com', target: '_blank' }, 'Navigation Four - Link'),
    title: 'Navigation Four - Link',
  },
]);
</script>
<template>
  <div class="">
    <h1 style="color: #16f;">首页</h1>
    
    <a-menu v-model:selectedKeys="current"  :items="items" />
  </div>
</template>
<style lang="scss" scoped></style>
