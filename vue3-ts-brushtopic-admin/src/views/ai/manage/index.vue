<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import { apiGetManageList, apiUpdateAiUser } from '@/api/ai/manage/index'
import type { ManageQueryType } from '@/api/ai/manage/type'
import {
  EditOutlined,
} from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
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
    title: '状态',
    dataIndex: 'status',
    key: 'status',
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

// 表单实例
const formRef = ref<any>(null)
// 表单数据
const formData = ref({
  account: '',
  id: null,
  aiCount: null,
  count: null,
  status: null,

})
// 表单规则
const rules = ref({
  account: [
    {
      required: true,
      message: '请输入用户名称',
      trigger: 'blur',
    },
  ],
  aiCount: [
    {
      required: true,
      message: 'AI次数不能为空',
      trigger: 'blur',
    },
  ],
  count: [
    {
      required: true,
      message: '总次数不能为空',
      trigger: 'blur',
    },
  ]
})
// 标题
const drawerTitle = ref('修改')
// 抽屉
const drawer = ref(false)
// 抽屉关闭
const onClose = () => {
  clearFormData()
  drawer.value = false
}
// 清空表单数据
const clearFormData = () => {
  formData.value = {
    account: '',
    id: null,
    aiCount: null,
    count: null,
    status: null,
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}
// 修改
const handleEdit = (record: any) => {
  drawer.value = true
  formData.value = {
    ...record
  }
}
// 保存以及修改
const onSave = () => {
  formRef.value.validate().then(async () => {
    await apiUpdateAiUser(formData.value)
    drawer.value = false
    message.success("修改成功")
    clearFormData()
  })
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
          <a-button type="link" size="small" :icon="h(EditOutlined)" @click="handleEdit(record)">修改</a-button>
        </template>
        <template v-if="column.key === 'remark'">
          <a-tooltip>
            <template #title>{{ record.remark }}</template>
            <!-- 超出部分显示为 tooltip截取20个字符 -->
            {{ record.remark.slice(0, 20) }}
          </a-tooltip>
        </template>
        <template v-if="column.key === 'status'">
          <a-tag v-if="record.status === 0" color="green">正常</a-tag>
          <a-tag v-if="record.status === 1" color="red">停用</a-tag>
        </template>
      </template>
    </a-table>

    <!-- 新增修改  -->
    <a-drawer :title="drawerTitle" placement="right" v-model:open="drawer" @close="onClose">
      <a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="账户" name="account">
          <a-input placeholder="请输入账户名称" v-model:value="formData.account"></a-input>
        </a-form-item>
        <a-form-item label="AI次数" name="aiCount">
          <a-input placeholder="请输入AI次数" v-model:value="formData.aiCount"></a-input>
        </a-form-item>
        <a-form-item label="总次数" name="count">
          <a-input placeholder="请输入总次数" v-model:value="formData.count"></a-input>
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-select placeholder="请选择状态" v-model:value="formData.status">
            <a-select-option value="0">正常</a-select-option>
            <a-select-option value="1">停用</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <!-- 添加底部按钮 -->
      <template #footer>
        <a-space class="space-footer-box">
          <a-button @click="onClose">取消</a-button>
          <a-button type="primary" @click="onSave">保存</a-button>
        </a-space>
      </template>
    </a-drawer>
  </div>
</template>
<style lang="scss" scoped></style>