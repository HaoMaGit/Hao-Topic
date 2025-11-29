<template>
    <!-- 使用echarts（具体是哪种图形，取决于option的内容） -->
    <BoxPlate :title="currentDate + '疫苗库存'">
        <!-- <div class="hl-right-center__title">{{ currentDate }}疫苗库存</div> -->
        <!-- <vue-echarts class="hl-right-center__charts" :option="option"></vue-echarts> -->
        <div ref="chartRef" style="width: 400px; height: 300px;"></div>
    </BoxPlate>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import BoxPlate from './BoxPlate.vue';
// import { queryVaccineInventory } from '@/api/VaccineInventory';
import * as echarts from 'echarts'
const currentDate = computed(() => {
    const date = new Date();
    const month = date.getMonth() + 1; // 月份从0开始，所以需要加1
    const day = date.getDate();
    return `${month}月${day}日`;
});
// 提取xAxis数据（疫苗名称）和yAxis数据（库存量）
const xAxisData = ref<any>([]);
const yAxisData = ref<any>([]);
const option: any = ref();
const chartRef = ref<HTMLDivElement | null>(null);
let chartInstance: echarts.ECharts | null = null

onMounted(() => {
    // const res = queryVaccineInventory()
    // 模拟data
    const data = {
        code: 200,
        data: {
            data: [
                { vaccineName: '新冠疫苗A型', inventory: 1200 },
                { vaccineName: '流感疫苗B型', inventory: 950 },
                { vaccineName: '乙肝疫苗C型', inventory: 780 },
                { vaccineName: 'HPV疫苗D型', inventory: 600 },
                { vaccineName: '狂犬疫苗E型', inventory: 450 },
                { vaccineName: '麻疹疫苗F型', inventory: 300 },
            ]
        }
    };
    console.log("疫苗今日库存", data.data);
    data.data.data.forEach((item: any) => {
        xAxisData.value.push(item.vaccineName);
        yAxisData.value.push(item.inventory);
    });
    console.log("xAxisData", xAxisData.value);

    option.value = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
            },
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '0%',
            top: '10%',
            containLabel: true,
        },
        xAxis: {
            type: 'category',
            data: xAxisData.value,
            axisLabel: {
                interval: 0, // 强制显示所有标签
                formatter: function (value: any) {
                    // 使用换行符分割过长的文本
                    // 这里简单地根据字符长度进行换行，您可能需要更复杂的逻辑来适应实际情况
                    if (value.length > 3) {
                        return value.slice(0, 3) + '\n' + value.slice(3);
                    } else {
                        return value;
                    }
                },
                textStyle: {
                    fontSize: 12, // 设置字体大小
                    fontWeight: 400, // 设置字体粗细
                    color: '#9fa2a9', // 设置字体颜色
                    align: 'center', // 文字居中对齐
                },
            },
            axisTick: {
                alignWithLabel: true,
            },
            axisLine: {
                show: true,
                lineStyle: {
                    color: 'rgba(153, 206, 255, 0.6)', // 轴线颜色为蓝色
                },
            },
            splitLine: {
                show: false,
            },
        },
        yAxis: {
            type: 'value',
            min: 0,
            axisLine: {
                show: true,
                lineStyle: {
                    color: 'rgba(153, 206, 255, 0.6)', // 轴线颜色为蓝色
                },
            },
            splitLine: {
                show: true,
                lineStyle: {
                    color: 'rgba(153, 206, 255, 0.3)', // 网格线颜色为蓝色
                    type: 'dashed', // 网格线类型为虚线
                },
            },
            axisLabel: {
                interval: 0, // 强制显示所有标签
                textStyle: {
                    fontSize: 10, // 设置字体大小
                    fontWeight: 400, // 设置字体粗细
                    color: '#5b626d', // 设置字体颜色
                },
            },
        },
        series: [
            {
                name: '库存',
                type: 'bar',
                barWidth: '60%',
                data: yAxisData.value,
                itemStyle: {
                    color: {
                        type: 'linear', // 设置颜色类型为线性渐变
                        x: 0, // 起点在左边
                        y: 0,
                        x2: 1, // 终点在右边
                        y2: 0,
                        colorStops: [
                            {
                                offset: 0,
                                color: 'rgba(51, 158, 235, 1)', // 柱子左侧颜色
                            },
                            {
                                offset: 1,
                                color: 'rgba(29, 63, 133, 0)', // 柱子右侧颜色
                            },
                        ],
                        global: false, // 默认值
                    },
                    barBorderRadius: [2, 0, 0, 2], // 圆角半径，这里设置了顶部两个角为5px的圆角
                },
                label: {
                    show: true,
                    position: 'top',
                    color: '#fff', // 设置标签文字颜色
                    fontSize: 14, // 设置字体大小
                },
            },
        ],
    }
    chartInstance = echarts.init(chartRef.value)
    chartInstance.setOption(option.value)
})

</script>

<style scoped lang="scss"></style>
