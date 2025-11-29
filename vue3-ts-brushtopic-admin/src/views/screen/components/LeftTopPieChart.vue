<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import BoxPlate from './BoxPlate.vue'
import * as echarts from 'echarts'

// 用户等级配置
const LEVELS = [
  { name: '面试新手', color: '#8c8c8c', min: 0, max: 99 },
  { name: '面试新秀', color: '#52c41a', min: 100, max: 299 },
  { name: '面试高手', color: '#1677ff', min: 300, max: 499 },
  { name: '面试达人', color: '#fa8c16', min: 500, max: 799 },
  { name: '面试专家', color: '#f5222d', min: 800, max: 999 },
  { name: '面试之神', color: '#722ed1', min: 1000, max: Infinity }
]

// 后端接口数据占位
const userLevels = ref([
  { name: '面试新手', value: 0 },
  { name: '面试新秀', value: 0 },
  { name: '面试高手', value: 0 },
  { name: '面试达人', value: 0 },
  { name: '面试专家', value: 0 },
  { name: '面试之神', value: 0 }
])

// 颜色
const color = ['#8c8c8c', '#52c41a', '#1677ff', '#fa8c16', '#f5222d', '#722ed1']

// 生成随机用户等级数据
const generateRandomData = () => {
  const total = 1000 // 总用户数
  const data = LEVELS.map(level => ({
    name: level.name,
    value: Math.floor(Math.random() * (total / LEVELS.length)) // 随机分配
  }))

  // 确保总数正确
  const currentTotal = data.reduce((sum, item) => sum + item.value, 0)
  if (currentTotal < total) {
    // 如果总数不足，随机添加到某个等级
    const randomIndex = Math.floor(Math.random() * data.length)
    data[randomIndex].value += total - currentTotal
  }

  return data
}

// 定时器引用
let timer: number | null = null

// 更新数据
const updateData = () => {
  userLevels.value = generateRandomData()
  chartInstance?.setOption(getOption())
}

const chartRef = ref<HTMLDivElement | null>(null)
let chartInstance: echarts.ECharts | null = null


const getOption = () => {
  const total = userLevels.value.reduce((sum, item) => sum + item.value, 0)

  return {
    title: {
      text: '用户等级分布',
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
      formatter: (p: any) => {
        const percent = ((p.value / total) * 100).toFixed(1)
        return `${p.name}<br/>人数：${p.value}<br/>占比：${percent}%`
      }
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
        name: '等级',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,

        label: {
          color: '#e6f4ff',
          fontSize: 13,
          formatter: (p: any) => {
            const percent = ((p.value / total) * 100).toFixed(0)
            return `${p.name}  ${percent}%`
          }
        },

        labelLine: {
          length: 10,
          length2: 10,
          lineStyle: { color: '#a3c8ff' } // 贴合 #1677ff 的偏蓝线条
        },

        itemStyle: {
          borderWidth: 1,
          borderColor: '#0d1a33', // 深蓝边，适配 #1677ff 主色
          shadowBlur: 10,
          shadowColor: 'rgba(22, 119, 255, 0.25)'
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

        data: userLevels.value.map((item, i) => ({
          name: item.name,
          value: item.value,
          itemStyle: { color: color[i] }
        }))
      }
    ]
  }
}



// 初始化图表
onMounted(() => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
    chartInstance.setOption(getOption())

    // 启动定时器，每3秒更新一次数据
    updateData() // 初始更新
    timer = window.setInterval(updateData, 5000)
  }
})

// 组件卸载时清除定时器
onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>

<template>
  <!-- 左上 -->
  <BoxPlate title="各等级用户分布情况饼图">
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
