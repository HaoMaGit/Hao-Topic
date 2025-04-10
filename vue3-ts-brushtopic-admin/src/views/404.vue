<script setup lang="ts">
import { useRouter } from 'vue-router';
const $router = useRouter()

</script>
<template>
  <div class="no-body">
    <!-- 左侧背景图 -->
    <div class="no-left">

    </div>
    <!-- 右侧提示 -->
    <div class="no-right">
      <h1>404错误！</h1>
      <h2>还没有这个网页！</h2>
      <a-button class="btn" type="primary" @click="$router.push('/login')">返回首页</a-button>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.no-body {
  width: 100%;
  height: 100vh;
  display: flex;
  background-color: #f6f7fb;

  .no-left {
    margin-top: 100px;
    margin-left: 250px;
    width: 550px;
    height: 400px;
    background-image: url('../assets/images/404.png');
    background-repeat: no-repeat; // 防止图像重复
    background-position: center; // 水平和垂直居中
    background-size: cover; // 图像自动缩放以适应容器，同时保持原有比例
  }

  .no-right {
    margin: 200px 0 0 60px;

    h1 {
      font-size: 32px;
      line-height: 40px;
      color: #1482f0;
      margin-bottom: 20px;
      font-weight: bold;
    }

    h2 {
      font-size: 16px;
      color: black;
      font-weight: bold;
    }

    .btn {
      margin-top: 20px;
    }
  }
}
</style>
