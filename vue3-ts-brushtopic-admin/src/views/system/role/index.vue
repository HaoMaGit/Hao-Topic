<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import { apiGetRoleList } from '@/api/system/role/index'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
} from '@ant-design/icons-vue';
import type { RoleQueryType } from '@/api/system/role/type';

// 查询角色列表
const getRoleList = async () => {
  const res = await apiGetRoleList(params.value)
  console.log("====>", res);
  tableData.value = res.data.rows
  total.value = res.data.total
}
// 定义查询参数
const params = ref<RoleQueryType>({
  pageNum: 1,
  pageSize: 10,
  name: '',
})
// 数量
const total = ref<number>(0)

// 表格数据
const tableData = ref([])
// 表格字段
const columns = [
  {
    title: '角色名称',
    dataIndex: 'name',
    key: 'name',
    align: 'center',
    width: 180,
  },

  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark',
    align: 'center',
    width: 350,
  },
  {
    title: '权限标识',
    dataIndex: 'roleKey',
    key: 'roleKey',
    align: 'center',
  },
  {
    title: '角色标识',
    dataIndex: 'identify',
    key: 'identify',
    align: 'center',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
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
  getRoleList()
}

// 重置
const handleReset = () => {
  params.value = {
    name: '',
    pageNum: 1,
    pageSize: 10,
  }
  total.value = 0
  getRoleList()
}

onMounted(() => {
  getRoleList()
})
</script>
<template>
  <div class="menu-body">
    <a-space class="space-box">
      <div class="query-box">
        <a-space>
          <!-- 查询条件 -->
          <a-form-item label="角色名称">
            <a-input placeholder="请输入菜单名称" v-model:value="params.name"></a-input>
          </a-form-item>
          <a-form-item>
            <a-space>
              <a-button type='primary' @click="handleQuery">搜索</a-button>
              <a-button @click="handleReset">重置</a-button>
            </a-space>
          </a-form-item>
        </a-space>
      </div>
      <!-- 操作按钮 -->
      <a-form-item>
        <a-space>
          <a-button ghost type="primary" :icon="h(PlusOutlined)">新增</a-button>
        </a-space>
      </a-form-item>
    </a-space>
    <!-- 表格 -->
    <a-table :pagination="{
      current: params.pageNum,
      pageSize: params.pageSize,
      total: total,
      showTotal: (total: any) => `共 ${total} 条`,
      showSizeChanger: true,
      showQuickJumper: true,
    }" :dataSource="tableData" :columns="columns">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operation'">
          <a-button type="link" size="small" :icon="h(PlusOutlined)">新增</a-button>
          <a-button type="link" size="small" :icon="h(EditOutlined)">修改</a-button>
          <a-button type="link" size="small" :icon="h(DeleteOutlined)">删除</a-button>
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
<style lang="scss" scoped>
.space-box {
  display: flex;
  justify-content: space-between;

  .query-box {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
