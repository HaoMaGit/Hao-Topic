<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import BoxPlate from './BoxPlate.vue'
import * as echarts from 'echarts'

// 初始数据
const usageData = ref([
    { name: '用户使用率', value: 8.6, color: '#1677ff' },
    { name: '系统使用率', value: 9.55, color: '#52c41a' },
    { name: '当前空闲率', value: 80.85, color: '#8c8c8c' }
])

// 定时器
let timer: number | null = null

// 随机模拟更新（可替换真实接口）
const generateNewData = () => {
    const user = +(Math.random() * 20).toFixed(2)
    const system = +(Math.random() * 20).toFixed(2)
    const idle = +(100 - user - system).toFixed(2)

    usageData.value = [
        { name: '用户使用率', value: user, color: '#1677ff' },
        { name: '系统使用率', value: system, color: '#52c41a' },
        { name: '当前空闲率', value: idle, color: '#8c8c8c' }
    ]
}

const chartRef = ref<HTMLDivElement | null>(null)
let chartInstance: echarts.ECharts | null = null

const getOption = () => {
    return {
        title: {
            text: '系统资源情况',
            left: '50%',
            top: '45%',
            textAlign: 'center',
            textStyle: {
                color: '#fff',
                fontSize: 18,
                fontWeight: 500
            }
        },

        tooltip: {
            trigger: 'item',
            formatter: (p: any) => `${p.name}<br/>占比：${p.value}%`
        },

        legend: {
            bottom: 0,
            orient: 'horizontal',
            textStyle: { color: '#fff', fontSize: 12 },
            itemWidth: 12,
            itemHeight: 12
        },

        series: [
            {
                name: '系统',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,

                label: {
                    color: '#e6f4ff',
                    fontSize: 13,
                    formatter: '{b}  {d}%'
                },

                labelLine: {
                    length: 10,
                    length2: 10,
                    lineStyle: { color: '#a3c8ff' }
                },

                itemStyle: {
                    borderWidth: 1,
                    borderColor: '#0d1a33',
                    shadowBlur: 10,
                    shadowColor: 'rgba(22,119,255,0.25)'
                },

                emphasis: {
                    scale: true,
                    scaleSize: 6,
                    itemStyle: {
                        shadowBlur: 20,
                        shadowColor: 'rgba(22, 119, 255, 0.45)',
                        borderColor: '#1677ff'
                    }
                },

                data: usageData.value.map(item => ({
                    name: item.name,
                    value: item.value,
                    itemStyle: { color: item.color }
                }))
            }
        ]
    }
}

// 初始化
onMounted(() => {
    if (chartRef.value) {
        chartInstance = echarts.init(chartRef.value)
        chartInstance.setOption(getOption())
    }

    // 初始更新 + 定时刷新
    generateNewData()
    timer = window.setInterval(() => {
        generateNewData()
        chartInstance?.setOption(getOption())
    }, 5000)
})

onUnmounted(() => {
    if (timer) clearInterval(timer)
    chartInstance?.dispose()
})
</script>

<template>
    <BoxPlate title="服务监控">
        <div class="chart" ref="chartRef"></div>
    </BoxPlate>
</template>

<style scoped lang="scss">
.chart {
    display: flex;
    align-items: center;
    width: 100%;
    height: 300px;
}
</style>
