<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import china from "@/assets/map/china.json";

const myChartRef = ref(null);
let chart = null;
let timer = null;

// 固定的目标坐标（杭州）
const TARGET_COORDS = [120.19, 30.25];

// 城市列表
const cities = [
    { name: "北京", coords: [116.24, 39.55] },
    { name: "上海", coords: [121.46, 31.28] },
    { name: "广州", coords: [113.23, 23.16] },
    { name: "深圳", coords: [114.07, 22.62] },
    { name: "杭州", coords: [120.19, 30.25] },
    { name: "南京", coords: [118.78, 32.04] },
    { name: "天津", coords: [117.2, 39.13] },
    { name: "重庆", coords: [106.54, 29.59] },
    { name: "成都", coords: [104.06, 30.67] },
    { name: "武汉", coords: [114.31, 30.52] },
    { name: "西安", coords: [108.95, 34.27] },
    { name: "郑州", coords: [113.62, 34.75] },
    { name: "长沙", coords: [112.94, 28.23] },
    { name: "苏州", coords: [120.62, 31.32] },
    { name: "无锡", coords: [120.29, 31.59] },
    { name: "厦门", coords: [118.1, 24.46] },
    { name: "福州", coords: [119.3, 26.08] },
    { name: "青岛", coords: [120.33, 36.07] },
    { name: "济南", coords: [117.0, 36.65] },
    { name: "大连", coords: [121.62, 38.92] },
    { name: "沈阳", coords: [123.38, 41.8] },
    { name: "哈尔滨", coords: [126.63, 45.75] },
    { name: "长春", coords: [125.32, 43.9] },
    { name: "昆明", coords: [102.73, 25.04] },
    { name: "南宁", coords: [108.33, 22.84] },
    { name: "贵阳", coords: [106.71, 26.57] },
    { name: "拉萨", coords: [91.11, 29.97] },
    { name: "乌鲁木齐", coords: [87.62, 43.82] },
    { name: "呼和浩特", coords: [111.65, 40.82] },
    { name: "兰州", coords: [103.82, 36.07] },
    { name: "银川", coords: [106.27, 38.47] },
    { name: "石家庄", coords: [114.48, 38.03] },
    { name: "太原", coords: [112.53, 37.87] },
    { name: "合肥", coords: [117.27, 31.86] },
    { name: "南昌", coords: [115.89, 28.68] },
    { name: "海口", coords: [110.32, 20.03] },
    { name: "唐山", coords: [118.02, 39.63] },
    { name: "洛阳", coords: [112.44, 34.7] },
    { name: "烟台", coords: [121.39, 37.52] },
    { name: "潍坊", coords: [119.1, 36.62] },
    { name: "常州", coords: [119.95, 31.79] },
    { name: "绍兴", coords: [120.58, 30.01] },
    { name: "嘉兴", coords: [120.76, 30.77] },
    { name: "湖州", coords: [120.09, 30.89] },
    { name: "温州", coords: [120.65, 28.01] },
    { name: "金华", coords: [119.64, 29.12] },
    { name: "东莞", coords: [113.75, 23.04] },
    { name: "佛山", coords: [112.87, 23.16] },
    { name: "惠州", coords: [114.41, 23.09] }
];


// ⭐ 随机生成 5 条飞往杭州的飞线
const generateRandomLines = () => {
    const availableCities = cities.filter(c =>
        c.coords[0] !== TARGET_COORDS[0] || c.coords[1] !== TARGET_COORDS[1]
    );

    // 随机挑选 5 个
    const selected = [...availableCities]
        .sort(() => 0.5 - Math.random())
        .slice(0, 5);

    return selected.map(city => ({
        fromName: city.name,
        toName: "杭州",
        coords: [city.coords, TARGET_COORDS],
        value: Math.floor(Math.random() * 100) + 50,
    }));
};

// ⭐ 更新图表（飞线 & 散点）
const updateChart = () => {
    if (!chart) return;

    // 当前 5 条飞线
    const lineData = generateRandomLines();

    // 散点只显示：5 个来源城市 + 杭州
    const scatterCities = [
        ...lineData.map(item => ({
            name: item.fromName,
            value: [...item.coords[0], item.value]
        })),
        {
            name: "杭州",
            value: [...TARGET_COORDS, 100]
        }
    ];

    const option = {
        backgroundColor: "transparent",
        geo: {
            map: "china",
            roam: false,
            zoom: 1.45,
            center: [104.5, 35],
            left: "center",
            top: "center",
            label: {
                show: true,
                fontSize: 10,
                color: "#c8e6ff",
                emphasis: { color: "#fff", fontSize: 12 }
            },
            itemStyle: {
                areaColor: "#072745",
                borderColor: "#39c5ff",
                borderWidth: 1.4,
                shadowBlur: 30,
                shadowColor: "rgba(0,150,255,0.45)"
            },
            emphasis: {
                itemStyle: { areaColor: "#1488cc" }
            }
        },

        series: [
            // ✨散点（来源 + 杭州）
            {
                type: "effectScatter",
                coordinateSystem: "geo",
                zlevel: 3,
                symbolSize: 12,
                rippleEffect: {
                    period: 4,
                    scale: 6,
                    brushType: "stroke"
                },
                itemStyle: {
                    color: "#00eaff",
                    shadowBlur: 15,
                    shadowColor: "#00eaff"
                },
                data: scatterCities
            },

            // ✨飞线
            {
                type: "lines",
                coordinateSystem: "geo",
                zlevel: 2,
                effect: {
                    show: true,
                    period: 4,
                    trailLength: 0,
                    symbol: "circle",
                    symbolSize: 8,
                    color: "#00aaff",
                    shadowColor: "#55ccff"
                },
                lineStyle: {
                    width: 2.5,
                    opacity: 0.9,
                    curveness: 0.25,
                    color: {
                        type: "linear",
                        x: 0,
                        y: 0,
                        x2: 1,
                        y2: 0,
                        colorStops: [
                            { offset: 0, color: "rgba(0, 180, 255, 0.1)" },
                            { offset: 1, color: "rgba(0, 240, 255, 1)" }
                        ]
                    }
                },
                data: lineData
            }
        ]
    };

    chart.setOption(option);
};

const drawChart = () => {
    chart = echarts.init(myChartRef.value);
    echarts.registerMap("china", china);
    updateChart();
};

const resize = () => chart?.resize();

onMounted(() => {
    drawChart();
    window.addEventListener("resize", resize);
    timer = setInterval(updateChart, 5000);
});

onUnmounted(() => {
    window.removeEventListener("resize", resize);
    clearInterval(timer);
    chart?.dispose();
});
</script>

<template>
    <div class="china_map_container">
        <div ref="myChartRef" class="chart"></div>
    </div>
</template>

<style scoped>
.china_map_container {
    display: flex;
    justify-content: center;
    align-items: center;
}

.chart {
    width: 100%;
    height: 800px;
}
</style>
