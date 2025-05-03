<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { apiGetFeedbackList } from '@/api/system/feedback/index'
import type { FeedbackQueryType } from '@/api/system/feedback/type';
// 查询反馈列表
const getFeedbackList = async () => {
  tableLoading.value = true
  const res = await apiGetFeedbackList(params.value)
  console.log("====>", res);
  tableData.value = res.data.rows
  total.value = res.data.total
  tableLoading.value = false
}
// 定义查询参数
const params = ref<FeedbackQueryType>({
  pageNum: 1,
  pageSize: 10,
  account: null,
  replyAccount: null,
  status: null
})
// 数量
const total = ref<number>(0)
// 表格加载中
const tableLoading = ref(false)

// 表格数据
const tableData = ref([])
// 表格字段
const columns = [
  {
    title: '账户名称',
    dataIndex: 'account',
    key: 'account',
    align: 'center',
    width: 160,
  },
  {
    title: '反馈内容',
    dataIndex: 'feedbackContent',
    key: 'feedbackContent',
    align: 'center',
    width: 200,
  },
  {
    title: '回复人',
    dataIndex: 'replyAccount',
    key: 'replyAccount',
    align: 'center',
    width: 160,
  },
  {
    title: '回复内容',
    dataIndex: 'replyContent',
    key: 'replyContent',
    align: 'center',
    width: 200,
  },
  {
    title: '反馈时间',
    dataIndex: 'createTime',
    key: 'createTime',
    align: 'center'
  },
  {
    title: '回复时间',
    dataIndex: 'replyTime',
    key: 'replyTime',
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    align: 'center'
  }
]


// 搜索
const handleQuery = () => {
  getFeedbackList()
}

// 重置
const handleReset = () => {
  params.value = {
    pageNum: 1,
    pageSize: 10,
    account: null,
    replyAccount: null,
    status: null
  }
  total.value = 0
  getFeedbackList()
}

// 分页变化处理
const handleTableChange = (pagination: any) => {
  params.value.pageNum = pagination.current;
  params.value.pageSize = pagination.pageSize;
  getFeedbackList();
}

// 弹窗
const drawer = ref(false)
// 关闭弹窗
const onClose = () => {
  drawer.value = false
}
// 回复
const onSave = () => {
}
onMounted(() => {
  getFeedbackList()
})
</script>
<template>
  <div class="role-body">
    <a-space class="space-box">
      <div class="query-box">
        <a-space :size="20">
          <!-- 查询条件 -->
          <a-form-item label="账户">
            <a-input placeholder="请输入账户名称" v-model:value="params.account"></a-input>
          </a-form-item>
          <a-form-item label="回复人">
            <a-input placeholder="请输入回复人名称" v-model:value="params.replyAccount"></a-input>
          </a-form-item>
          <a-form-item label="状态">
            <a-select v-model:value="params.status" placeholder="请选择状态">
              <a-select-option value="0">待回复</a-select-option>
              <a-select-option value="1">已回复</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item>
            <a-space>
              <a-button type='primary' @click="handleQuery">搜索</a-button>
              <a-button @click="handleReset">重置</a-button>
            </a-space>
          </a-form-item>
        </a-space>
      </div>
    </a-space>
    <!-- 表格 -->
    <a-table :pagination="{
      current: params.pageNum,
      pageSize: params.pageSize,
      total: total,
      showTotal: (total: any) => `共 ${total} 条`,
      showSizeChanger: true,
    }" :loading="tableLoading" @change="handleTableChange" :dataSource="tableData" :columns="columns">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operation'">
          <a-button ghost type="primary" @click="drawer = true" v-if="record.status === 0">回复</a-button>
          <a-button type="success" v-else>已回复</a-button>
        </template>
        <template v-if="column.key === 'feedbackContent'">
          <a-tooltip>
            <template #title>{{ record.feedbackContent }}</template>
            <!-- 超出部分显示为 tooltip截取20个字符 -->
            {{ record.feedbackContent?.slice(0, 20) }}
          </a-tooltip>
        </template>
        <template v-if="column.key === 'replyContent'">
          <a-tooltip>
            <template #title>{{ record.replyContent }}</template>
            <!-- 超出部分显示为 tooltip截取20个字符 -->
            {{ record.replyContent?.slice(0, 20) }}
          </a-tooltip>
        </template>
        <template v-if="column.key === 'status'">
          <a-tag v-if="record.status === 0" color="processing">待回复</a-tag>
          <a-tag v-if="record.status === 1" color="success">已回复</a-tag>
        </template>
      </template>
    </a-table>
    <!-- 回复框 -->
    <a-drawer title="回复" placement="right" v-model:open="drawer" @close="onClose">
      <!-- 回复内容 -->
      <a-form-item label="回复内容">
        <a-textarea></a-textarea>
      </a-form-item>
      <!-- 添加底部按钮 -->
      <template #footer>
        <a-space class="space-footer-box">
          <a-button @click="onClose">取消</a-button>
          <a-button type="primary" @click="onSave">回复</a-button>
        </a-space>
      </template>
    </a-drawer>
  </div>
</template>
<style lang="scss" scoped>
.space-box {
  display: flex;
  justify-content: space-between;

  .query-box {
    display: flex;
    justify-content: flex-end;
  }
}

.space-footer-box {
  display: flex;
  justify-content: flex-end;
}
</style>
