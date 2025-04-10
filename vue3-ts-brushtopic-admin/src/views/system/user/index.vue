<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import { apiGetUserList } from '@/api/system/user/index'
import type { UserQueryType } from '@/api/system/user/type'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  DownloadOutlined,
  UploadOutlined
} from '@ant-design/icons-vue'

// 查询角色列表
const getUserList = async () => {
  const res = await apiGetUserList(params.value)
  tableData.value = res.data.rows
  total.value = res.data.total
}

// 定义查询参数
const params = ref<UserQueryType>({
  pageNum: 1,
  pageSize: 10,
  account: '',
  memberTime: '',
  roleName: '',
  createTime: '',
})

// 总数量
const total = ref(0)
// 表格loading
const tableLoading = ref(false)
// 表格数据
const tableData = ref([])
// 角色数据
const roleList = ref(['管理员', '用户', '会员'])
// 当前选中角色
const activeKey = ref('管理员')
// 表格字段
const columns = [
  {
    title: '序号',
    dataIndex: 'id',
    key: 'id',
    align: 'center',
    width: 80,
  },
  {
    title: '账户',
    dataIndex: 'account',
    key: 'account',
    align: 'center',
    width: 150,
  },
  {
    title: '头像',
    dataIndex: 'avatar',
    key: 'avatar',
    align: 'center',
    width: 80,
  },
  {
    title: '角色',
    dataIndex: 'roleName',
    key: 'roleName',
    align: 'center',
    width: 110,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    align: 'center',
    width: 130,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    align: 'center',
  },
  {
    title: '注册时间',
    dataIndex: 'createTime',
    key: 'createTime',
    align: 'center',
    width: 160,
  },
  {
    title: '会员时间',
    dataIndex: 'memberTime',
    key: 'memberTime',
    align: 'center',
    width: 160,
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    align: 'center'
  }
]

// 是否移入删除按钮
const isDangerHover = ref(false)

// 搜索
const handleQuery = () => {
  getUserList()
}

// 重置
const handleReset = () => {
  // params.value = {
  //   account: '',
  //   pageNum: 1,
  //   pageSize: 10,
  // }
  total.value = 0
  getUserList()
}

// 选中数组
const onSelectedRowKeys = ref([])
// 显示操作
const operation = ref(false)
// 选中菜单
const onSelectChange = (selectedRowKeys: any) => {
  onSelectedRowKeys.value = selectedRowKeys
  if (onSelectedRowKeys.value.length > 0) {
    operation.value = true
  } else {
    operation.value = false
  }
}

// 点击了tabs
const handleTabClick = (key: any) => {
  activeKey.value = key
  // params.value.roleName = key
  // getUserList()
}

onMounted(() => {
  getUserList()
})
</script>

<template>
  <div class="menu-body">
    <a-space class="space-box">
      <div class="query-box">
        <a-space :size="23">
          <!-- 查询条件 -->
          <a-form-item label="账户名称">
            <a-input placeholder="请输入账户名称" v-model:value="params.account"></a-input>
          </a-form-item>
          <a-form-item label="注册时间">
            <a-range-picker class="range-picker" v-model:value="params.createTime" />
          </a-form-item>
          <a-form-item label="会员时间">
            <a-range-picker class="range-picker" v-model:value="params.memberTime" />
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
          <a-button v-if="!operation" class="add-btn" type="primary" :icon="h(PlusOutlined)">新增</a-button>
        </a-space>
      </a-form-item>
    </a-space>
    <!-- 操作按钮 -->
    <a-form-item v-if="operation">
      <a-space>
        <a-button type="primary" :icon="h(PlusOutlined)">新增</a-button>
        <a-button @mouseenter="isDangerHover = true" @mouseleave="isDangerHover = false" :danger="isDangerHover"
          :icon="h(DeleteOutlined)">删除</a-button>
        <a-button :icon="h(DownloadOutlined)">导出</a-button>
        <a-button :icon="h(UploadOutlined)">导入</a-button>
      </a-space>
    </a-form-item>
    <a-tabs @tabClick="handleTabClick" v-model:activeKey="activeKey" tab-position="left">
      <a-tab-pane v-for="item in roleList" :key="item" :tab="item">
        <a-table :pagination="{
          current: params.pageNum,
          pageSize: params.pageSize,
          total: total,
          showTotal: (total: any) => `共 ${total} 条`,
          showSizeChanger: true,
          showQuickJumper: true,
        }" :loading="tableLoading" :dataSource="tableData" :columns="columns" rowKey="id"
          :row-selection="{ selectedRowKeys: onSelectedRowKeys, onChange: onSelectChange, fixed: true }">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'operation'">
              <a-button type="link" size="small" :icon="h(EditOutlined)">修改</a-button>
              <a-button type="link" size="small" :icon="h(DeleteOutlined)">删除</a-button>
            </template>
            <template v-if="column.key === 'avatar'">
              <a-avatar src="https://www.antdv.com/assets/logo.1ef800a8.svg" />
            </template>
            <!-- TODO  -->
            <template v-if="column.key === 'status'">
              <a-link>{{ record.status }}</a-link>
            </template>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<style lang="scss" scoped>
.space-box {
  display: flex;
  justify-content: space-between;

  .query-box {
    display: flex;
    justify-content: flex-end;

    .range-picker {
      width: 200px;
    }
  }
}
</style>