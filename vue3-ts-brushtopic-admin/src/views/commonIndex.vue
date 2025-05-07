<script setup lang="ts">
import { useUserStore } from '@/stores/modules/user';
const userStore = useUserStore()
import { ref, onMounted, watch, computed } from 'vue';
import * as echarts from 'echarts';
import { getYearDateRange, getDynamicDateRange } from '@/utils/date';
import { getWaterBallSVG } from '@/utils/customer';
import { apiUserHomeCount, apiUserHomeCategory, apiUserTopicCount } from '@/api/home'
import type { UserHomeCount } from '@/api/home/type';

// 用户身份计算
const getUserIdentity = computed(() => {
  // 这里可以根据用户的刷题数量、连续天数等计算用户身份
  const totalProblems = leftData.value?.topicFrequencyCount || 0;
  if (totalProblems >= 1000) return { name: '面试之神', color: '#722ed1' }; // 紫色
  if (totalProblems >= 800) return { name: '面试专家', color: '#f5222d' };  // 红色
  if (totalProblems >= 500) return { name: '面试达人', color: '#fa8c16' };  // 橙色
  if (totalProblems >= 300) return { name: '面试高手', color: '#1677ff' };  // 蓝色
  if (totalProblems >= 100) return { name: '面试新秀', color: '#52c41a' };  // 绿色
  return { name: '面试新手', color: '#8c8c8c' };  // 灰色
})

// 分类实例
const categoryChart = ref(null)
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
        console.log("==========>", params);

        return `<div style="text-align: center;border: 1px solid #ccc; background-color: #fff; padding: 5px; border-radius: 4px;">
                  <strong style="color: #1a1a1a;">${params.name}</strong><br/>
                  <span style="color: #1a1a1a;">${params.value}</span><span style="color: #b1b1b1;"> / ${params.data.count}</span>
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
            color: '#fff',
            fontSize: 11,
            fontWeight: 'bold',
          },
        },
      },
      data: rightData.value.map((cat: any) => ({
        name: cat.categoryName,
        value: cat.count,
        count: cat.totalCount,
        symbol: getWaterBallSVG(cat.count, cat.totalCount),
        symbolSize: (cat.totalCount < 5 ? 5 : cat.totalCount) * 10, // value越大，球越大
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
// 生成动态数据
function getVirtulData() {
  const { start, end } = getDynamicDateRange();
  // 后台返回的数据格式
  const backendData = trendData.value || [];
  const data: any[] = [];

  // 将后台数据转换为echarts需要的格式
  backendData.forEach((item: any) => {
    data.push([
      item.date,
      item.count
    ]);
  });

  // 数据验证日志
  console.log('数据时间范围:',
    echarts.format.formatTime('yyyy-MM-dd', start),
    '→',
    echarts.format.formatTime('yyyy-MM-dd', end),
    '总数:', data.length
  );

  return data;
}
// 初始化的日历热力图
const initContributionChart = () => {
  const myChart = echarts.init(contributionChart.value);
  const { startStr, endStr } = getDynamicDateRange();
  console.log(startStr, endStr);

  const option = {
    // 鼠标悬停
    tooltip: {
      formatter: function (params: any) {
        return `${params.value[1]}次刷题：${params.value[0]}`;
      }
    },
    visualMap: {
      type: 'piecewise',
      orient: 'horizontal',
      left: 'center',
      pieces: [
        { min: 0, max: 10, color: '#e0f0ff', label: '0-10' },
        { min: 10, max: 30, color: '#b3d8ff', label: '10-30' },
        { min: 30, max: 50, color: '#80bfff', label: '30-50' },
        { min: 50, max: 80, color: '#4da6ff', label: '50-80' },
        { min: 80, max: 120, color: '#1677ff', label: '80-120' },
        { min: 120, max: 99999, color: '#0050b3', label: '120+' }

      ],
      show: true,
      textStyle: {
        color: '#8c8c8c' // 设置为你想要的颜色，如红色
      }
    },
    calendar: {
      top: 25,
      left: 30,
      right: 30,
      cellSize: [18, 18],        // 固定单元格大小
      range: [startStr, endStr], // 动态设置范围
      itemStyle: {
        borderWidth: 1,
        borderColor: '#d0e3ff'
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
      data: getVirtulData()
    }]
  };
  myChart.setOption(option);

  window.addEventListener('resize', () => {
    myChart.resize();
  });
};

// 当前年份
const currentYear = new Date().getFullYear();
// 年份选择
const yearOptions = ref([
  currentYear,
  currentYear - 1,
  currentYear - 2
]);
// 当前选择年份
const selectedYear = ref(currentYear);
// 初始化和更新日历热力图
let myChart: any = null;
function renderContributionChart(year: number) {
  if (!myChart) {
    myChart = echarts.init(contributionChart.value);
  }
  const { startStr, endStr } = getYearDateRange(year);
  const option = {
    tooltip: {
      formatter: function (params: any) {
        return `${params.value[1]}次刷题：${params.value[0]}`;
      }
    },
    visualMap: {
      type: 'piecewise',
      orient: 'horizontal',
      left: 'center',
      pieces: [
        { min: 0, max: 10, color: '#e0f0ff', label: '0-10' },
        { min: 10, max: 30, color: '#b3d8ff', label: '10-30' },
        { min: 30, max: 50, color: '#80bfff', label: '30-50' },
        { min: 50, max: 80, color: '#4da6ff', label: '50-80' },
        { min: 80, max: 120, color: '#1677ff', label: '80-120' },
        { min: 120, max: 99999, color: '#0050b3', label: '120+' }
      ],
      show: true,
      textStyle: {
        color: '#8c8c8c' // 设置为你想要的颜色，如红色
      }
    },
    calendar: {
      top: 25,
      left: 30,
      right: 30,
      cellSize: [18, 18],
      range: [startStr, endStr],
      itemStyle: {
        borderWidth: 1,
        borderColor: '#d0e3ff'
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
      },
    },
    series: [{
      type: 'heatmap',
      coordinateSystem: 'calendar',
      data: trendData.value ? trendData.value.map((item: any) => [item.date, item.count]) : []
    }]
  };
  myChart.setOption(option);
  window.addEventListener('resize', () => {
    myChart.resize();
  });
}


// 监听年份变化，切换图表
watch(selectedYear, () => {
  getTrendData()
});

const leftData = ref<UserHomeCount>() // 题目相关数据
const rightData = ref() // 分类相关数据
const trendData = ref() // 每日刷题次数数据
// 查询用户左上角题目数据
const getLeftData = async () => {
  const { data } = await apiUserHomeCount();
  if (data) {
    leftData.value = data;
  }
};
// 查询用户分类相关数据
const getRightData = async () => {
  const { data } = await apiUserHomeCategory();
  if (data) {
    rightData.value = data;
    initBubbleChart();
  }
};
// 查询用户每日刷题数据
const getTrendData = async () => {
  const { data } = await apiUserTopicCount(String(selectedYear.value));
  if (data) {
    trendData.value = data;
    // 获取数据后重新渲染热力图
    if (selectedYear.value === currentYear) {
      initContributionChart();
    } else {
      renderContributionChart(selectedYear.value);
    }
  }
};

const initData = () => {
  Promise.all([
    getLeftData(),
    getRightData(),
    getTrendData()
  ])
}
// 组件挂载后初始化图表
onMounted(() => {
  initData()
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
                      userStore.userInfo.nickname || userStore.userInfo.account
                    }}</div>
                  <div class="achievements">
                    <!-- 用户成就 -->
                    <a-tag :color="getUserIdentity.color" class="user-identity">
                      <CrownOutlined /> {{ getUserIdentity.name }}
                    </a-tag>
                  </div>
                </div>
              </div>
            </a-col>
            <!-- 用户数据统计 -->
            <a-col :span="16" class="user-data-col">
              <a-row :gutter="[16, 16]">
                <a-col :span="12">
                  <a-statistic title="已刷题次数" :value="leftData?.topicFrequencyCount" class=" stat-item">
                    <template #prefix>
                      <BarChartOutlined />
                    </template>
                    <template #suffix>
                      <span>次</span>
                      <span class="top-magnitude" v-if="leftData && leftData.topicFrequencyGrowthRate > 0">
                        <ArrowUpOutlined /> {{ leftData.topicFrequencyGrowthRate }}次
                      </span>
                      <span class="bottom-magnitude" v-else>
                        <ArrowDownOutlined /> {{ Math.abs(leftData?.topicFrequencyGrowthRate ?? 0) }}次
                      </span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="刷题次数总排名" :value="leftData?.rank" class="stat-item">
                    <template #prefix>
                      <TrophyOutlined />
                    </template>
                    <template #suffix>
                      <span>名</span>
                      <span class="top-magnitude" v-if="leftData && leftData.rankGrowthRate > 0">
                        <ArrowUpOutlined /> {{ leftData?.rankGrowthRate }}名
                      </span>
                      <span class="bottom-magnitude" v-else>
                        <ArrowDownOutlined />{{ Math.abs(leftData?.rankGrowthRate ?? 0) }}名
                      </span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="已刷题目" :value="leftData?.totalTopicRecordCountSize" class="stat-item">
                    <template #prefix>
                      <CheckCircleOutlined />
                    </template>
                    <template #suffix>
                      <span>/{{ leftData?.totalTopicCount }}题</span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="AI陪背次数" :value="leftData?.aiCount" suffix="次" class="stat-item">
                    <template #prefix>
                      <RobotOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="最长连续刷题" :value="leftData?.maxConsecutiveCount" suffix="天" class="stat-item">
                    <template #prefix>
                      <CalendarOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="最近连续刷题" :value="leftData?.recentConsecutiveCount" suffix="天" class="stat-item">
                    <template #prefix>
                      <HourglassOutlined />
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
        <div class="header-row">
          <!-- 标题 -->
          <div class="chart-title">刷题次数</div>
          <!-- 年份选择前2年的 -->
          <div class="year-select">
            <a-select v-model:value="selectedYear">
              <a-select-option v-for="year in yearOptions" :key="year" :value="year">{{ year }}</a-select-option>
            </a-select>
          </div>
        </div>
        <!-- 日历热力图 -->
        <div ref="contributionChart" class="contribution-chart"></div>
        <!-- 底部描述 -->
        <div class="footer-description">每一次查看面试题就会统计刷题次数</div>
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

    .header-row {
      display: flex;
      justify-content: space-between; // 左右对齐
      align-items: center;
      margin-bottom: 8px;
    }

    .chart-title {
      font-size: 19px;
      font-weight: bold;
      font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
      color: #464646;
      text-align: left; // 标题靠左
    }

    .year-select {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }

    .footer-description {
      font-size: 14px;
      color: #8c92A4;
    }
  }
}



.category-chart {
  width: 100%;
  height: 286px;

}

.contribution-chart {
  width: 100%;
  height: 200px;
}

.user-data-col {
  padding-bottom: 33px;
}
</style>