<script setup lang="ts">
import { useUserStore } from '@/stores/modules/user';
const userStore = useUserStore()
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts';

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
  { name: '单调1栈', value: 50 },
  { name: '单调23栈', value: 50 },
];
// 初始化气泡图
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
      data: categoryData.map((cat) => ({
        name: cat.name,
        value: cat.value,
        symbolSize: cat.value * 1.05, // value越大，球越大
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
    backgroundColor: '#fff',
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['刷题人数', '题目数量'],
      right: '5%',
      top: '2%'
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
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月'],
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
        showSymbol: false,
        lineStyle: {
          width: 3,
          color: '#1677ff'
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
        data: [10, 132, 101, 134, 90, 230, 210, 182, 191, 234, 290, 330]
      },
      {
        name: '题目数量',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        showSymbol: false,
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
        data: [220, 182, 191, 34, 290, 330, 310, 320, 301, 334, 390, 430]
      }
    ]
  };

  myChart.setOption(option);

  // 在resize事件处理中增加布局重置
  window.addEventListener('resize', () => {
    myChart.resize();
  });
}

onMounted(() => {
  initBubbleChart();
  initProblemTrendChart()
})
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
                      userStore.userInfo.account
                    }}</div>
                  <div class="achievements">
                    <!-- 用户成就 -->
                    <a-tag color="#1677ff" class="user-identity">
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
                  <a-statistic title="今日刷题总数" :value="1230" class="stat-item">
                    <template #prefix>
                      <CodeOutlined />
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
                  <a-statistic title="AI调用总次数" :value="1230" class=" stat-item">
                    <template #prefix>
                      <RobotOutlined />
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
                  <a-statistic title="用户总数" :value="1000" class="stat-item">
                    <template #prefix>
                      <TeamOutlined />
                    </template>
                    <template #suffix>
                      <span>名</span>
                      <span class="bottom-magnitude">
                        <ArrowDownOutlined /> 10%
                      </span>
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="收录题目数量" suffix="题" :value="30" class="stat-item">
                    <template #prefix>
                      <FileTextOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="收录专题数量" :value="40" suffix="个" class="stat-item">
                    <template #prefix>
                      <FolderOutlined />
                    </template>
                  </a-statistic>
                </a-col>
                <a-col :span="12">
                  <a-statistic title="收录标签数量" :value="10" suffix="个" class="stat-item">
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
    <div class="bottom-section"></div>
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
}

.chart-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #1a1a1a;
  display: flex;
  align-items: center;
}

.chart-title::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 18px;
  background: linear-gradient(to bottom, #1677ff, #4096ff);
  margin-right: 10px;
  border-radius: 2px;
}

.topic-trend {
  width: 100%;
  height: 350px;
}
</style>
