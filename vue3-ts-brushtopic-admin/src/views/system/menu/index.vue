<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { apiGetMenuList } from '@/api/system/menu/index'


// 查询菜单列表
const getMenuList = async () => {
  const res = await apiGetMenuList(params.value)
  console.log("菜单返回结果===>", res);
}
// 定义查询参数
const params = ref({
  pageNum: 1,
  pageSize: 10,
  menuName: '',
})

onMounted(() => {
  getMenuList()
})
const dataSource = [
  {
    key: '1',
    name: '胡彦斌',
    age: 32,
    address: '西湖区湖底公园1号',
  },
  {
    key: '2',
    name: '胡彦祖',
    age: 42,
    address: '西湖区湖底公园1号',
  },
  {
    key: '2',
    name: '胡彦祖',
    age: 42,
    address: '西湖区湖底公园1号',
  },

  {
    key: '2',
    name: '胡彦祖',
    age: 42,
    address: '西湖区湖底公园1号',
  },
  {
    key: '2',
    name: '胡彦祖',
    age: 42,
    address: '西湖区湖底公园1号',
  },
]

const columns = [
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '年龄',
    dataIndex: 'age',
    key: 'age',
  },
  {
    title: '住址',
    dataIndex: 'address',
    key: 'address',
  },
]

const current = ref(1);
const pageSize = ref(10);
const total = ref(dataSource.length);
</script>
<template>
  <div class="menu-body">
    <a-space class="space-box" style="display: flex;justify-content: space-between;">
      <div style="display: flex;justify-content: flex-end;">
        <a-space>
          <!-- 查询条件 -->
          <a-form-item label="菜单名称">
            <a-input placeholder="请输入菜单名称"></a-input>
          </a-form-item>
          <a-form-item>
            <a-space>
              <a-button type='primary'>搜索</a-button>
              <a-button>重置</a-button>
            </a-space>
          </a-form-item>
        </a-space>
      </div>
      <!-- 操作按钮 -->
      <a-form-item>
        <a-space>
          <a-button>添加菜单</a-button>
          <a-button>修改菜单</a-button>
          <a-button>删除菜单</a-button>
          <a-button>导出菜单</a-button>
          <a-button>导入菜单</a-button>
        </a-space>
      </a-form-item>
    </a-space>
    <!-- 表格 -->
    <a-table :pagination="{
      current: current,
      pageSize: pageSize,
      total: total,
      showSizeChanger: true,
      showQuickJumper: true,
    }" :dataSource="dataSource" :columns="columns" />
  </div>
</template>
<style lang="scss" scoped></style>
