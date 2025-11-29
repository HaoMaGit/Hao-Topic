<script setup lang="ts">
import BoxPlate from './BoxPlate.vue'

defineOptions({ name: 'BoxPlate', inheritAttrs: false });
defineProps({ title: String, value: String });
const handleClick = (title: string | undefined) => {
  console.log(title);
  // 传给父组件点击了标题
  emit('handleClickTitle', title);
}
const emit = defineEmits(['handleClickTitle']);
</script>

<template>
  <!-- 容器 -->
  <div class="box-plate">
    <!-- 容器头部 -->
    <div class="box-plate-header">
      <!-- 头部左侧 -->
      <div class="box-plate-space">
        <!-- 头部图标 -->
        <span class="box-plate-icon">
          <slot name="icon">
            <svg-icon local-icon="trend-charts"></svg-icon>
          </slot>
        </span>
        <!-- 头部标题 -->
        <div class="box-plate-title" @click="handleClick(title)">{{ title }}</div>
      </div>
      <!-- 头部右侧 -->
      <slot name="side">
        <div class="box-plate-side">{{ value }}</div>
      </slot>
    </div>
    <!-- 容器内部 -->
    <div class="box-plate-container">
      <slot></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.box-plate {
  display: flex;
  flex-direction: column;
  gap: 10px;
  height: 100%;

  .box-plate-header {
    position: relative;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    border-radius: 0 0 0 16px;
    background: url('@/assets/images/title-bg.png') no-repeat;

    &::after {
      content: '';
      position: absolute;
      left: 0;
      right: 0;
      height: 100%;
      z-index: -1;
      background: url('@/assets/images/title-texture.png') center bottom no-repeat;
    }
  }

  .box-plate-space {
    display: flex;
    align-items: center;
  }

  .box-plate-icon {
    font-size: 40px;
    line-height: 0;
  }

  .box-plate-title {
    cursor: pointer;
    background: #1677ff;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-size: 18px;
    font-weight: 600;
    font-style: oblique;
    letter-spacing: 2px;
  }

  .box-plate-side {
    color: rgba(153, 206, 255, 1);
    font-size: 14px;
  }

  .box-plate-container {
    flex: auto;
    padding: 12px 12px 0 12px;
    border-radius: 16px 10px 10px;
    background: linear-gradient(127.79deg, rgba(64, 99, 160, 0.3) 3.99%, rgba(23, 36, 58, 0.1) 66.01%);
  }
}
</style>
