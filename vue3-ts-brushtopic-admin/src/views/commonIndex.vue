<script setup lang="ts">
import { useUserStore } from '@/stores/modules/user';
const userStore = useUserStore()
import { onMounted, ref } from 'vue';
import * as echarts from 'echarts';

// 用户身份计算
const getUserIdentity = () => {
  // 这里可以根据用户的刷题数量、连续天数等计算用户身份
  const totalProblems = 30; // 假设这是从用户数据中获取的

  if (totalProblems >= 100) return '算法大师';
  if (totalProblems >= 50) return '面试糕手';
  if (totalProblems >= 20) return '面试实习生';
  return '面试新手';
};


// 分类实例
const categoryChart = ref(null)
const categoryData = [
  { name: '哈希表', value: 60 },
  { name: '数组', value: 80 },
  { name: '动态规划', value: 90 },
  { name: '队列', value: 40 },
  { name: '短阵', value: 50 },
  { name: '堆（优先队列）', value: 70 },
  { name: '栈', value: 55 },
  { name: '双指针', value: 65 },
  { name: '并查集', value: 45 },
  { name: '单调栈', value: 50 },
];

// 自定义水球图样式
function getWaterBallSVG(percent: number, color = '#4da6ff', size = 100) {
  const h = size;
  const w = size;
  const waterHeight = h * (1 - percent / 100);
  const waveY = waterHeight;
  const wave = `M0,${waveY} Q${w / 4},${waveY - 8} ${w / 2},${waveY} T${w},${waveY} L${w},${h} L0,${h} Z`;
  // 使用clipPath裁剪水波到圆形区域
  const svg = `<svg width="${w}" height="${h}" xmlns="http://www.w3.org/2000/svg">
    <defs>
      <clipPath id="clipCircle">
        <circle cx="${w / 2}" cy="${h / 2}" r="${w / 2 - 2}" />
      </clipPath>
    </defs>
    <circle cx="${w / 2}" cy="${h / 2}" r="${w / 2 - 2}" fill="#ebebeb" stroke="${color}" stroke-width="0"/>
    <g clip-path="url(#clipCircle)">
      <path d="${wave}" fill="${color}" fill-opacity="0.5"/>
    </g>
  </svg>`;
  return 'image://data:image/svg+xml;base64,' + window.btoa(unescape(encodeURIComponent(svg)));
}
// 初始化气泡图
const initBubbleChart = () => {
  const myChart = echarts.init(categoryChart.value);
  const option = {
    // 提示框
    tooltip: {
      trigger: 'item',
      backgroundColor: 'transparent', // 关键：透明背景
      borderColor: 'transparent',     // 关键：透明边框
      padding: 0,                     // 关键：清除内边距
      textStyle: {                    // 关键：重置文本样式
        color: 'transparent',
        fontSize: 0
      },
      extraCssText: 'box-shadow: none;', // 关键：禁用阴影
      formatter: function (params: any) {
        return `<div style="text-align: center;border: 1px solid #ccc; background-color: #fff; padding: 5px; border-radius: 4px;">
                  <strong style="color: #1a1a1a;">${params.name}</strong><br/>
                  <span style="color: #1a1a1a;">${params.value}</span><span style="color: #b1b1b1;"> / 2081</span>
                </div>`;
      }
    },
    animationDurationUpdate: function (idx: number) {
      // 越往后的数据延迟越大
      return idx * 100;
    },
    animationEasingUpdate: 'bounceIn',
    series: [{
      type: 'graph',
      layout: 'force',
      force: {
        repulsion: 100,
        edgeLength: 10,
      },
      label: {
        show: true,
        formatter: [
          '{b|{b}}'
        ].join('\n'),
        fontWeight: '100',
        fontSize: '18',
        align: 'center',
        verticalAlign: 'center',
        rich: {
          b: {
            color: '#4da6ff',
            fontSize: 12,
            textBorderColor: 'transparent',
            textBorderWidth: 0,
          },
        },
      },
      data: categoryData.map((cat) => ({
        name: cat.name,
        value: cat.value,
        symbol: getWaterBallSVG(cat.value),
        symbolSize: cat.value * 1.1, // value越大，球越大
        draggable: true,
      }))
    }]
  };
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-expect-error
  myChart.setOption(option);

  // 在resize事件处理中增加布局重置
  window.addEventListener('resize', () => {
    myChart.resize();
  });
};


// 日历热力图
const contributionChart = ref(null)
// 生成模拟数据（可替换为后端真实数据）
function getVirtulData(year: number) {
  const date = +echarts.number.parseDate(year + '-01-01');
  const end = +echarts.number.parseDate((year + 1) + '-01-01');
  const dayTime = 3600 * 24 * 1000;
  const data = [];
  for (let time = date; time < end; time += dayTime) {
    data.push([
      echarts.format.formatTime('yyyy-MM-dd', time),
      Math.floor(Math.random() * 5) // 0~4，代表当天的活跃度
    ]);
  }
  return data;
}
// 初始化日历热力图
const initContributionChart = () => {
  const myChart = echarts.init(contributionChart.value);
  const year = new Date().getFullYear();
  const option = {
    // 鼠标悬停
    tooltip: {
      formatter: function (params: any) {
        return `${params.value[0]}<br/>贡献：${params.value[1]}`;
      }
    },
    visualMap: {
      min: 0,
      max: 4,
      type: 'piecewise',
      orient: 'horizontal',
      left: 'center',
      top: 20,
      inRange: {
        color: ['#e0f0ff', '#b3d8ff', '#80bfff', '#4da6ff', '#1677ff'] // 渐变色数组
      },
      show: false // 不显示图例
    },
    calendar: {
      top: 60,
      left: 30,
      right: 30,
      cellSize: ['auto', 18],
      range: year,
      itemStyle: {
        borderWidth: 1,
        borderColor: '#fff'
      },
      yearLabel: { show: false },
      monthLabel: {
        nameMap: 'cn',
        color: '#888'
      },
      dayLabel: {
        firstDay: 1,
        nameMap: 'cn',
        color: '#888'
      }
    },
    series: [{
      type: 'heatmap',
      coordinateSystem: 'calendar',
      data: getVirtulData(year)
    }]
  };
  myChart.setOption(option);

  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 组件挂载后初始化图表
onMounted(() => {
  initBubbleChart();
  initContributionChart();
});
</script>

<template>
  <div class="common-index">
    <!-- 上部分 -->
    <a-row>
      <!-- 用户部分 -->
      <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <a-card :bordered="false">
          <a-row>
            <!-- 用户相关信息 -->
            <a-col :span="8" class="user-info-col">
              <div class="user-info-container">
                <a-avatar
                  :style="{ 'border': `1px solid ${userStore.userInfo.identity === 1 ? '#ffd700' : '#1677ff'}` }"
                  :size="100" :src="userStore.userInfo.avatar">
                  <template #icon>
                    <UserOutlined />
                  </template>
                </a-avatar>
                <div class="user-details">
                  <!-- 用户名称和身份 -->
                  <div class="username" :style="{ 'color': userStore.userInfo.identity === 1 ? '#ffd700' : '#1677ff' }">
                    {{
                      userStore.userInfo.account
                    }}</div>
                  <div class="achievements">
                    <!-- 用户成就 -->
                    <a-tag color="#f50" class="user-identity">
                      <CrownOutlined /> {{ getUserIdentity() }}
                    </a-tag>
                  </div>
                </div>
              </div>
            </a-col>
            <!-- 用户数据统计 -->
            <a-col :span="16">
              <a-row :gutter="[16, 16]">
                <a-col :span="12">
                  <a-statistic title="最长连续刷题" :value="10" suffix="天" class="stat-item">
                    <template #prefix>
                      <CalendarOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="最近连续刷题" :value="20" suffix="天" class="stat-item">
                    <template #prefix>
                      <HourglassOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="已刷题目" :value="30" class="stat-item">
                    <template #prefix>
                      <CheckCircleOutlined />
                    </template>
                    <template #suffix>
                      <span>/300题</span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="AI陪背次数" :value="40" suffix="次" class="stat-item">
                    <template #prefix>
                      <RobotOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="已刷题次数" :value="1230" class=" stat-item">
                    <template #prefix>
                      <BarChartOutlined />
                    </template>
                    <template #suffix>
                      <span>次</span>
                      <span class="top-magnitude">
                        <ArrowUpOutlined /> 10%
                      </span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="刷题次数总排名" :value="1000" class="stat-item">
                    <template #prefix>
                      <TrophyOutlined />
                    </template>
                    <template #suffix>
                      <span>名</span>
                      <span class="bottom-magnitude">
                        <arrow-down-outlined /> 10%
                      </span>
                    </template>
                  </a-statistic>
                </a-col>
              </a-row>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <!-- 分类部分仿力扣分类 -->
      <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <a-card :bordered="false">
          <div ref="categoryChart" class="category-chart"></div>
        </a-card>
      </a-col>
    </a-row>
    <!-- 下部分仿gitee贡献度 -->
    <div class="bottom-section">
      <a-card :bordered="false">
        <div ref="contributionChart" class="contribution-chart"></div>
      </a-card>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.user-info-col {
  display: flex;
  justify-content: center;
}

.user-info-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding-right: 30px;
}

.user-details {
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-left: 4px;
}

.username {
  text-align: center;
  margin-bottom: 8px;
}

.achievements {
  display: flex;
  justify-content: center;
}

.stat-item {
  margin-bottom: 10px;

  .top-magnitude {
    color: #1677ff;
    font-size: 14px;
  }

  .bottom-magnitude {
    color: #cf1322;
    font-size: 14px;
  }
}

.common-index {
  .bottom-section {
    margin-top: 20px;
  }
}



.category-chart {
  width: 100%;
  height: 254px;

}

.contribution-chart {
  width: 100%;
  height: 200px;
}
</style>