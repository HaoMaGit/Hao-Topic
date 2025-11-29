<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue";
import { LoopScroll } from "@joyday/vue-loop-scroll";
import BoxPlate from "./BoxPlate.vue";

const names = ["小李", "小王", "小刘", "小张", "小赵", "小周", "小陈", "小孙", "小郭"];

// 格式化时间：yyyy-MM-dd HH:mm:ss
function getTimeStr() {
    const t = new Date();
    const pad = (n: number) => String(n).padStart(2, "0");
    return `${t.getFullYear()}-${pad(t.getMonth() + 1)}-${pad(t.getDate())} `
        + `${pad(t.getHours())}:${pad(t.getMinutes())}:${pad(t.getSeconds())}`;
}

// 初始生成 N 条
function generateInitialLogs(count = 20) {
    return Array.from({ length: count }, (_, i) => {
        const user = names[Math.floor(Math.random() * names.length)];
        const nums = Math.floor(Math.random() * 3) + 1;
        return {
            id: Date.now() + i,
            user,
            time: getTimeStr(),
            count: nums,
        };
    });
}

const dataSource = ref(generateInitialLogs());

let timer: number | null = null;

// 每 10 秒追加一条
function pushLog() {
    const user = names[Math.floor(Math.random() * names.length)];
    const nums = Math.floor(Math.random() * 3) + 1;

    dataSource.value.push({
        id: Date.now(),
        user,
        time: getTimeStr(),
        count: nums,
    });

    if (dataSource.value.length > 40) {
        dataSource.value.shift();
    }
}

onMounted(() => {
    timer = window.setInterval(pushLog, 10000);
});

onUnmounted(() => {
    if (timer) clearInterval(timer);
});
</script>

<template>
    <BoxPlate title="用户刷题动态">
        <!-- 表头 -->
        <div class="table-header">
            <span class="col user">用户名称</span>
            <span class="col time">刷题时间</span>
            <span class="col count">刷题数</span>
        </div>

        <div class="scroll-wrapper">
            <LoopScroll :dataSource="dataSource" itemKey="id" direction="down" :speed="0.3">
                <template #default="{ item }">
                    <div class="user-item">
                        <span class="col user">{{ item.user }}</span>
                        <span class="col time">{{ item.time }}</span>
                        <span class="col count">{{ item.count }} 道</span>
                    </div>
                </template>
            </LoopScroll>
        </div>
    </BoxPlate>
</template>

<style scoped>
.scroll-wrapper {
    height: 240px;
    resize: both;
    overflow: auto;
}

/* 表头 */
.table-header {
    display: flex;
    justify-content: space-between;
    padding: 8px 12px;
    font-size: 13px;
    color: #91caff;
    border-bottom: 1px solid rgba(22, 119, 255, 0.35);
    background: rgba(22, 119, 255, 0.08);
    font-weight: bold;
}

.user-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px 12px;
    font-size: 13px;
    color: #e6f4ff;
    border-bottom: 1px solid rgba(22, 119, 255, 0.12);
    line-height: 24px;
}

.col {
    display: inline-block;
}

.col.user {
    width: 80px;
}

.col.time {
    width: 180px;
}

.col.count {
    width: 100px;
    text-align: center;
}
</style>
