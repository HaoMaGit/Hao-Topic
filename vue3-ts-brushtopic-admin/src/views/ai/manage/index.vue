<script setup lang="ts">
import { ref, onMounted, } from 'vue'
import { apiGetManageList } from '@/api/ai/manage/index'
import type { ManageQueryType } from '@/api/ai/manage/type'
// import {
//   EditOutlined,
//   DeleteOutlined,
// } from '@ant-design/icons-vue';
// 定义查询参数
const params = ref<ManageQueryType>({
  pageNum: 1,
  pageSize: 10,
  account: '',
})
// 获取用户列表
const getManageList = async () => {
  const res = await apiGetManageList(params.value)
  console.log("====>", res);
  tableData.value = res.data.rows
  total.value = res.data.total
}
// 分页变化处理
const handleTableChange = (pagination: any) => {
  params.value.pageNum = pagination.current;
  params.value.pageSize = pagination.pageSize;
  getManageList();
}

// 数量
const total = ref<number>(0)

// 表格数据
const tableData = ref([])
// 表格字段
const columns = [
  {
    title: '账户',
    dataIndex: 'account',
    key: 'account',
    align: 'center',
    width: 180,
  },

  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    align: 'center',
    width: 180,
  },
  {
    title: '使用次数',
    dataIndex: 'aiCount',
    key: 'aiCount',
    align: 'center',
  },
  {
    title: '总次数',
    dataIndex: 'count',
    key: 'count',
    align: 'center',
  },
  {
    title: '最近使用时间',
    dataIndex: 'recentlyUsedTime',
    key: 'recentlyUsedTime',
    align: 'center'
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
  getManageList()
}

// 重置
const handleReset = () => {
  params.value = {
    account: '',
    pageNum: 1,
    pageSize: 10,
  }
  total.value = 0
  getManageList()
}


onMounted(() => {
  getManageList()
})
</script>
<template>
  <div class="manage-body">
    <a-space class="space-box">
      <div class="query-box">
        <a-space>
          <!-- 查询条件 -->
          <a-form-item label="账户名称">
            <a-input placeholder="请输入账户名称" v-model:value="params.account"></a-input>
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
    }" @change="handleTableChange" :dataSource="tableData" :columns="columns">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operation'">
          <!-- <a-button type="link" size="small" :icon="h(EditOutlined)" @click="handleEdit(record)">修改</a-button> -->
          <!-- <a-button type="link" size="small" :icon="h(DeleteOutlined)" @click="handleDelete(record.id)">删除</a-button> -->
        </template>
        <template v-if="column.key === 'remark'">
          <a-tooltip>
            <template #title>{{ record.remark }}</template>
            <!-- 超出部分显示为 tooltip截取20个字符 -->
            {{ record.remark.slice(0, 20) }}
          </a-tooltip>
        </template>
      </template>
    </a-table>
  </div>
</template>
<style lang="scss" scoped></style>
