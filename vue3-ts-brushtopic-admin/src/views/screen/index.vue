<script setup lang="ts">
import { onMounted } from 'vue';
import LeftTopPieChart from './components/LeftTopPieChart.vue'
import RightTopPieChart from './components/RightTopPieChart.vue';
import VScaleScreen from 'v-scale-screen'
import ScreenHeader from './components/ScreenHeader.vue';
import LeftCenterBarChart from './components/LeftCenterBarChart.vue';
import RightCenterBarChart from './components/RightCenterBarChart.vue';
onMounted(() => {

})
</script>
<template>
  <v-scale-screen width="1920" height="1080">
    <!-- 屏幕 -->
    <div id="screen-app">
      <!-- 屏幕容器 -->
      <div class="data-view">
        <!-- 头部 -->
        <div class="main-header">
          <ScreenHeader />
        </div>
        <div class="main-container">
          <!-- 左侧 -->
          <div class="left-chart-container">
            <!-- 左上（展示不同等级用户的分布情况饼图） -->
            <LeftTopPieChart />
            <!-- 左中（刷题量top10柱状图） -->
            <LeftCenterBarChart />
            <!-- 左下 -->
          </div>
          <!-- 中部 -->
          <div class="center-chart-container"></div>
          <!--右侧 -->
          <div class="right-chart-container">
            <!-- 右上（用户使用率系统使用率系统空闲率饼图） -->
            <RightTopPieChart />
            <!-- 右中（活跃用户top10柱状图） -->
            <RightCenterBarChart />
            <!-- 右下 -->
          </div>
        </div>
      </div>
    </div>
  </v-scale-screen>
</template>
<style lang="scss" scoped>
#screen-app {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
  background: url("@/assets/images/bgk.png") no-repeat;
  background-size: 100% 100%;
  pointer-events: none;
  color: rgba(153, 206, 255, 1);
  background-color: #010103;
  font-size: 14px;
}

.data-view {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: url("@/assets/images/bg-border.png") no-repeat;
  background-size: 100% 100%;

  .main-container {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    flex: auto;
    padding: 0 24px;
  }

  .left-chart-container,
  .right-chart-container {
    width: 500px;
    height: 100%;
    flex-shrink: 0;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 16px;
    pointer-events: auto;
  }

  .center-chart-container {
    flex: auto;
    pointer-events: none;
  }
}
</style>
