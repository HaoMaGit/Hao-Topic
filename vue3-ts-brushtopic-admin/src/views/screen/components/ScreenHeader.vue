<script setup lang="ts">
import { ref } from 'vue';

// 处理时间
const weeks: any = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
const currentDate = ref('');
const week = ref('');
const handleTime = () => {
    setInterval(() => {
        const now = new Date();
        const months = now.getMonth() + 1;
        const days = now.getDate();
        const hours = now.getHours();
        const minutes = now.getMinutes();
        const seconds = now.getSeconds();
        const month = months < 10 ? '0' + months : months;
        const day = days < 10 ? '0' + days : days;
        const hour = hours < 10 ? '0' + hours : hours;
        const minute = minutes < 10 ? '0' + minutes : minutes;
        const second = seconds < 10 ? '0' + seconds : seconds;
        week.value = weeks[now.getDay()];
        currentDate.value = `${now.getFullYear()}.${month}.${day} ${hour}:${minute}:${second}`;
    }, 1000);
}

// 是否全屏
const isFullscreen = ref(false);
const toggleFullScreen = () => {
    if (!isFullscreen.value) {
        // 开启全屏
        document.documentElement.requestFullscreen().catch((err) => {
            console.error(`Error attempting to enable full-screen mode: ${err.message} (${err.name})`);
        });
    } else {
        // 关闭全屏
        document.exitFullscreen().catch((err) => {
            console.error(`Error attempting to exit full-screen mode: ${err.message} (${err.name})`);
        });
    }
    isFullscreen.value = !isFullscreen.value;
};
handleTime()
</script>

<template>
    <!-- 头部容器 -->
    <div class="screen-header">
        <!-- 左侧 -->
        <div class="screen-header-aside">
            {{ currentDate }} {{ week }}
        </div>
        <!-- 中部 -->
        <div class="screen-header-title">
            <i class="screen-header-text"> 易题系统数据实时大屏 </i>
        </div>
        <!-- 右侧 -->
        <div class="screen-header-side">
            <!-- 全屏播放 -->
            <div class="screen-header-fullscreen" @click="toggleFullScreen">
                <FullscreenOutlined class="screen-header-fullscreen-icon" />
                <span class="screen-header-fullscreen-text">全屏播放</span>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.screen-header {
    padding: 10px 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    pointer-events: auto;

    &::after {
        content: '';
        position: absolute;
        height: 100%;
        width: 100%;
        z-index: -1;
    }


    .screen-header-aside {
        display: flex;
        font-size: 18px;
    }

    .screen-header-fullscreen {
        cursor: pointer;
        margin-left: 30px;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 114px;
        height: 28px;
        border-radius: 4px;
        background: linear-gradient(180deg, rgba(21, 85, 170, 0) 0%, rgba(3, 92, 226, 0.28) 100%);
        border: 3px solid #1f79c6;
        box-shadow: 0px 2px 8px rgba(7, 17, 52, 0.25);
        backdrop-filter: blur(4px);
    }

    .screen-header-fullscreen-icon {
        width: 22px;
        height: 22px;
        padding-right: 5px;
    }

    .screen-header-fullscreen-text {
        font-size: 14px;
        font-weight: 700;
    }

    .screen-header-title {
        flex: auto;
        text-align: center;
    }

    .screen-header-text {
        font-size: 28px;
        font-weight: 400;
        letter-spacing: 2px;
        vertical-align: middle;
    }


}
</style>
