<script setup lang="ts">
import { useUserStore } from '@/stores/modules/user';
const userStore = useUserStore()


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
                  <a-statistic title="今日刷题总数" :value="1230" class=" stat-item">
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
</style>
