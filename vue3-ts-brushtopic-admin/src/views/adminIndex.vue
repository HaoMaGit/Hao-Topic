<script setup lang="ts">
import { useUserStore } from '@/stores/modules/user';
const userStore = useUserStore()
import { ref, watch, onMounted } from 'vue'
import * as echarts from 'echarts';
// 导入 settingStore
import { useSettingStore } from '@/stores/modules/setting';
const settingStore = useSettingStore();
import { apiAdminHomeCount, apiAdminHomeCategory, apiTopicTrend, apiUserTrend, apiAiTrend } from '@/api/home'
import type { AdminLeftDataType, TopicTrendType } from '@/api/home/type';
// 分类实例
const categoryChart = ref(null)
// 气泡图
const initBubbleChart = () => {
  const myChart = echarts.init(categoryChart.value);
  const option = {
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
          '{c|{c}}',
          '{b|{b}}',
        ].join('\n'),
        fontWeight: '400',
        fontSize: 12,
        color: '#1a1a1a',
        position: 'inside',
        rich: {
          b: {
            fontSize: 12,
            color: '#fff',
            padding: [0, 0, 2, 0],
            align: 'center',
            width: 60
          },
          c: {
            fontSize: 14,
            color: '#fff',
            fontWeight: 'bold',
            align: 'center',
            width: 60
          }
        }
      },
      itemStyle: {
        color: '#1677ff',  // 设置球的颜色
        opacity: 0.8,      // 设置透明度
        borderWidth: 1,    // 添加边框
        borderColor: '#fff'
      },
      data: rightData.value.map((cat: any) => ({
        name: cat.categoryName,
        value: cat.count,
        symbolSize: (cat.count === 0 ? 5 : cat.count) * 10, // value越大，球越大
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

// 刷题趋势图实例
const topicTrendChart = ref(null);
const initProblemTrendChart = () => {
  const myChart = echarts.init(topicTrendChart.value);
  const option = {
    backgroundColor: settingStore.isDark ? '#141414' : '#fff',
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['刷题人数', '题目数量'],
      right: '5%',
      top: '2%',
      textStyle: {
        color: '#8c8c8c'
      }
    },
    grid: {
      top: '10%',
      left: '3%',
      right: '4%',
      bottom: '8%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: middleData.value?.dateList,
      axisLine: {
        lineStyle: {
          color: '#E0E6F1'
        }
      },
      axisLabel: {
        color: '#666'
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        color: '#666'
      },
      splitLine: {
        lineStyle: {
          color: '#E0E6F1',
          type: 'dashed'
        }
      }
    },
    series: [
      {
        name: '刷题人数',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        showSymbol: true, // 显示标记
        lineStyle: {
          width: 3,
          color: '#1677ff',
          type: 'dashed' // 使用虚线
        },
        itemStyle: {
          color: '#1677ff',
          borderColor: '#fff',
          borderWidth: 2
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(22, 119, 255, 0.3)' },
            { offset: 1, color: 'rgba(22, 119, 255, 0)' }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: middleData.value?.countUserList
      },
      {
        name: '题目数量',
        type: 'line',
        smooth: true,
        symbol: 'triangle', // 使用不同的标记形状
        symbolSize: 8,
        showSymbol: true,
        lineStyle: {
          width: 3,
          color: '#4096ff'
        },
        itemStyle: {
          color: '#4096ff',
          borderColor: '#fff',
          borderWidth: 2
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 150, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 150, 255, 0)' }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: middleData.value?.countTopicList
      }
    ]
  };

  myChart.setOption(option);

  // 在resize事件处理中增加布局重置
  window.addEventListener('resize', () => {
    myChart.resize();
  });
}

// 用户增长趋势图
const userGrowthChart = ref(null);
const initUserGrowthChart = () => {
  const myChart = echarts.init(userGrowthChart.value);
  const option = {
    backgroundColor: settingStore.isDark ? '#141414' : '#fff',

    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#1677ff'
        }
      }
    },
    grid: {
      top: '10%',
      left: '3%',
      right: '7%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: userTrend.value.dateList,
      axisLine: {
        lineStyle: {
          color: '#E0E6F1'
        }
      },
      axisLabel: {
        color: '#666'
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        color: '#666'
      },
      splitLine: {
        lineStyle: {
          color: '#E0E6F1',
          type: 'dashed'
        }
      }
    },
    series: [
      {
        name: '用户增长',
        type: 'line',
        stack: 'Total',
        smooth: true,
        symbol: 'circle',
        symbolSize: 6,
        showSymbol: true,
        lineStyle: {
          width: 3,
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#1677ff' },
            { offset: 1, color: '#4096ff' }
          ])
        },
        itemStyle: {
          color: '#1677ff',
          borderColor: '#fff',
          borderWidth: 2
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(22, 119, 255, 0.3)' },
            { offset: 1, color: 'rgba(22, 119, 255, 0)' }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: userTrend.value.countList
      }
    ]
  };
  myChart.setOption(option);

  // 在resize事件处理中增加布局重置
  window.addEventListener('resize', () => {
    myChart.resize();
  });
}

// ai调用趋势图
const aiCallChart = ref(null);
const initAiCallChart = () => {
  const myChart = echarts.init(aiCallChart.value);
  const option = {
    backgroundColor: settingStore.isDark ? '#141414' : '#fff',
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#1677ff'
        }
      }
    },
    grid: {
      top: '10%',
      left: '3%',
      right: '7%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: aiTrend.value.dateList,
      axisLine: {
        lineStyle: {
          color: '#E0E6F1',
        }
      },
      axisLabel: {
        color: '#666',
      },
    },
    yAxis: {
      type: 'value',
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        color: '#666'
      },
      splitLine: {
        lineStyle: {
          color: '#E0E6F1',
          type: 'dashed'
        }
      }
    },
    series: [
      {
        name: '刷题次数',
        type: 'line',
        stack: 'Total',
        smooth: true,
        symbol: 'circle',
        symbolSize: 6,
        showSymbol: true,
        lineStyle: {
          width: 3,
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#1677ff' },
            { offset: 1, color: '#4096ff' }
          ])
        },
        itemStyle: {
          color: '#1677ff',
          borderColor: '#fff',
          borderWidth: 2
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(22, 119, 255, 0.3)' },
            { offset: 1, color: 'rgba(22, 119, 255, 0)' }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: aiTrend.value.countList
      }
    ]
  };
  myChart.setOption(option);

  // 在resize事件处理中增加布局重置
  window.addEventListener('resize', () => {
    myChart.resize();
  });
}
// 监听暗黑模式变化
watch(() => settingStore.isDark, () => {
  // 重新渲染图表
  initAiCallChart()
  initProblemTrendChart();
  initUserGrowthChart();
});

// 首页全部数据
const leftData = ref<AdminLeftDataType>(); // 左侧数据
const rightData = ref(); // 左侧数据
const middleData = ref<TopicTrendType>(); // 中间数据
const userTrend = ref(); // 用户统计数据
const aiTrend = ref(); // ai趋势数据
// 获取左侧数据
const getLeftData = async () => {
  const res = await apiAdminHomeCount();
  if (res.data) {
    leftData.value = res.data
  }
}
// 获取右侧数据
const getRightData = async () => {
  const res = await apiAdminHomeCategory();
  if (res.data) {
    rightData.value = res.data
    if (rightData.value) {
      initBubbleChart();
    }
  }
}
// 获取中间部分数据
const getMiddleData = async () => {
  const res = await apiTopicTrend();
  if (res.data) {
    middleData.value = res.data
    initProblemTrendChart();
  }
}
// 获取用户数据
const getUserTrendData = async () => {
  const res = await apiUserTrend();
  if (res.data) {
    userTrend.value = res.data
    initUserGrowthChart();
  }
}
// 获取ai数据
const getAiTrendData = async () => {
  const res = await apiAiTrend();
  if (res.data) {
    aiTrend.value = res.data
    initAiCallChart()
  }
}

// 统一异步执行请求
const initData = async () => {
  await Promise.all([
    getLeftData(),
    getRightData(),
    getMiddleData(),
    getUserTrendData(),
    getAiTrendData()
  ])
}

onMounted(() => {
  initData()
});
</script>
<template>
  <div class="admin-body">
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
                    <a-tag
                      style="background: linear-gradient(to right, rgba(243, 156, 18, 0.1), rgba(243, 156, 18, 0.2) 30%, rgba(243, 156, 18, 0.3) 60%, rgba(243, 156, 18, 0.4) 80%);"
                      class="user-identity">
                      <CrownOutlined /> 管理员
                    </a-tag>
                  </div>
                </div>
              </div>
            </a-col>
            <!-- 用户数据统计 -->
            <a-col :span="16" class="user-data-col">
              <a-row :gutter="[16, 16]">
                <a-col :span="12">
                  <a-statistic title="刷题次数总数" :value="leftData?.countTodayFrequency" class="stat-item">
                    <template #prefix>
                      <CodeOutlined />
                    </template>
                    <template #suffix>
                      <span>次</span>
                      <span class="top-magnitude" v-if="leftData && leftData?.topicGrowthRate > 0">
                        <ArrowUpOutlined /> {{ leftData?.topicGrowthRate }}次
                      </span>
                      <span class="bottom-magnitude" v-else>
                        <ArrowDownOutlined /> {{ Math.abs(leftData?.topicGrowthRate ?? 0) }}次
                      </span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="AI调用总次数" :value="leftData?.aiCount" class=" stat-item">
                    <template #prefix>
                      <RobotOutlined />
                    </template>
                    <template #suffix>
                      <span>次</span>
                      <span class="top-magnitude" v-if="leftData && leftData.aiGrowthRate > 0">
                        <ArrowUpOutlined /> {{ leftData.aiGrowthRate }}次
                      </span>
                      <span class="bottom-magnitude" v-else>
                        <ArrowDownOutlined /> {{ Math.abs(leftData?.aiGrowthRate ?? 0) }}次
                      </span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="用户总数" :value="leftData?.userCount" class="stat-item">
                    <template #prefix>
                      <TeamOutlined />
                    </template>
                    <template #suffix>
                      <span>名</span>
                      <span class="top-magnitude" v-if="leftData && leftData?.userGrowthRate > 0">
                        <ArrowUpOutlined /> {{ leftData?.userGrowthRate }}名
                      </span>
                      <span class="bottom-magnitude">
                        <ArrowDownOutlined /> {{ Math.abs(leftData?.userGrowthRate ?? 0) }}名
                      </span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="收录题目数量" suffix="题" :value="leftData?.totalTopicCount" class="stat-item">
                    <template #prefix>
                      <FileTextOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="收录专题数量" :value="leftData?.totalSubjectCount" suffix="个" class="stat-item">
                    <template #prefix>
                      <BookOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="收录标签数量" :value="leftData?.topicLabelCount" suffix="个" class="stat-item">
                    <template #prefix>
                      <TagsOutlined />
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
    <!-- 中间部分刷题趋势图 -->
    <div class="middle-section">
      <a-card :bordered="false">
        <div class="chart-title">刷题趋势图</div>
        <div ref="topicTrendChart" class="topic-trend"></div>
      </a-card>
    </div>
    <!-- 底部部分 -->
    <div class="bottom-section">
      <!-- 分为左右 -->
      <a-card :bordered="false" class="bottom-section-container">
        <a-row>
          <a-col :span="12">
            <div class="chart-title">用户增长趋势图</div>
            <div ref="userGrowthChart" class="user-growth"></div>
          </a-col>
          <a-col :span="12">
            <div class="chart-title">AI调用次数趋势图</div>
            <div ref="aiCallChart" class="ai-call"></div>
          </a-col>
        </a-row>
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

.user-data-col {
  padding-bottom: 33px;
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

.category-chart {
  width: 100%;
  height: 286px;

}

.middle-section {
  margin-top: 20px;
  margin-bottom: 20px;
}

.chart-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: $base-personal-color;
  display: flex;
  align-items: center;
}

.chart-title::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 18px;
  background: #1677ff;
  margin-right: 10px;
  border-radius: 2px;
}

.topic-trend {
  width: 100%;
  height: 350px;
}

.bottom-section {
  width: 100%;
  display: flex;

  .bottom-section-container {
    width: 100%;

  }
}

.user-growth {

  width: 100%;
  height: 350px;
}

.ai-call {

  width: 100%;
  height: 350px;
}
</style>
