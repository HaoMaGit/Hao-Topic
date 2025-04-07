<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import { apiGetMenuList } from '@/api/system/menu/index'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
} from '@ant-design/icons-vue';

// 查询菜单列表
const getMenuList = async () => {
  const res = await apiGetMenuList(params.value)
  tableData.value = res.data
}
// 定义查询参数
const params = ref({
  menuName: '',
})

// 表格数据
const tableData = ref([])
// 表格字段
const columns = [
  {
    title: '菜单名称',
    dataIndex: 'menuName',
    key: 'menuName',
    align: 'center'
  },
  {
    title: '图标',
    dataIndex: 'icon',
    key: 'icon',
    align: 'center'
  },
  {
    title: '排序',
    dataIndex: 'sorted',
    key: 'sorted',
    align: 'center'
  },
  {
    title: '路径',
    dataIndex: 'route',
    key: 'route',
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    align: 'center'
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    align: 'center'
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    align: 'center'
  }
]

// 是否移入删除按钮
const isDangerHover = ref(false);

// 搜索
const handleQuery = () => {
  getMenuList()
}

// 重置
const handleReset = () => {
  params.value = {
    menuName: '',
  }
  getMenuList()
}

onMounted(() => {
  getMenuList()
})
</script>
<template>
  <div class="menu-body">
    <a-space class="space-box">
      <div class="query-box">
        <a-space>
          <!-- 查询条件 -->
          <a-form-item label="菜单名称">
            <a-input placeholder="请输入菜单名称" v-model:value="params.menuName"></a-input>
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
          <a-button :icon="h(EditOutlined)">修改</a-button>
          <a-button @mouseenter="isDangerHover = true" @mouseleave="isDangerHover = false" :danger="isDangerHover"
            :icon="h(DeleteOutlined)">删除</a-button>
          <!-- <a-button :icon="h(DownloadOutlined)">导出</a-button>
          <a-button :icon="h(UploadOutlined)">导入</a-button> -->
        </a-space>
      </a-form-item>
    </a-space>
    <!-- 表格 -->
    <a-table :pagination="false" :dataSource="tableData" :columns="columns">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operation'">
          <a-button type="link" size="small" :icon="h(PlusOutlined)">新增</a-button>
          <a-button type="link" size="small" :icon="h(EditOutlined)">修改</a-button>
          <a-button type="link" size="small" :icon="h(DeleteOutlined)">删除</a-button>
        </template>
        <!-- 处理icon -->
        <template v-else-if="column.key === 'icon'">
          <span v-if="record.icon">
            <component :is="record.icon" />
          </span>
        </template>
      </template>
    </a-table>
    <!-- 
    :pagination="{
      current: params.pageNum,
      pageSize: params.pageSize,
      total: params.total,
      showTotal: (total: any) => `共 ${total} 条`,
      showSizeChanger: true,
      showQuickJumper: true,
    }"  -->
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
