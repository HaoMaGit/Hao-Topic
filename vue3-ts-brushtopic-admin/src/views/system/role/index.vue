<script setup lang="ts">
import { ref, onMounted, h, createVNode } from 'vue'
import { apiGetRoleList, apiGetRoleMenu } from '@/api/system/role/index'
import { apiGetMenuList } from '@/api/system/menu/index'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons-vue';
import type { RoleQueryType, RoleType } from '@/api/system/role/type';
import Modal from 'ant-design-vue/es/modal/Modal';
import { message } from 'ant-design-vue';
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


// 表单实例
const formRef = ref<any>(null)
// 表单数据
const formData = ref<RoleType>({
  name: '',
  remark: '',
  roleKey: '',
  identify: null,
  id: null,
})
// 表单规则
const rules = ref({
  name: [
    {
      required: true,
      message: '请输入角色名称',
      trigger: 'blur',
    },
  ],
  remark: [
    {
      required: true,
      message: '请输入备注',
      trigger: 'blur',
    },
  ],
  roleKey: [
    {
      required: true,
      message: '请输入角色权限标识',
      trigger: 'blur',
    }
  ],
  identify: [
    {
      required: true,
      message: '请输入角色标识',
      trigger: 'blur',
    }
  ]
})

// 标题
const drawerTitle = ref('新增')
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
    name: '',
    remark: '',
    roleKey: '',
    identify: null,
    id: null,
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
  checkedKeys.value = []
}
// 新增
const handleAdd = () => {
  drawerTitle.value = '新增角色'
  drawer.value = true
}

// 获取菜单列表
const getMenuList = async () => {
  const res = await apiGetMenuList({ menuName: '' })
  if (res.data) {
    treeData.value = handleTreeData(res.data)
  }
}
// 修改
const handleEdit = async (record: RoleType) => {
  drawerTitle.value = '修改角色'
  const res = await apiGetRoleMenu(record.id)
  if (res.data) {
    checkedKeys.value = res.data.map((item: any) => item.id)
  }
  drawer.value = true
  formData.value = { ...record }
}

// 树形数据
const treeData = ref<any>([])
// 当前选中的树节点
const checkedKeys = ref([])
// 处理菜单数据为树形结构
const handleTreeData = (data: any) => {
  return data.map((item: any) => ({
    key: item.id,
    title: item.menuName,
    children: item.children ? handleTreeData(item.children) : []
  }))
}
// 删除
const handleDelete = (id: number) => {
  Modal.confirm({
    title: '是否确认删除该角色?',
    icon: createVNode(ExclamationCircleOutlined),
    content: createVNode('div', { style: 'color:red;' }, '删除角色会导致相关用户权限丢失，请慎重考虑!'),
    async onOk() {
      try {
        // await apiDeleteMenu(id)
        // getMenuList()
        // clearFormData()
        drawer.value = false
        message.success('删除成功')
      } catch {
        drawer.value = false
      }

    },
    onCancel() {
      console.log('Cancel');
    },
  });
  console.log(id);
}
// 保存
const onSave = () => {
  formRef.value.validate().then(() => {
    if (formData.value.id) {
      console.log('修改');
    }
  })
}


onMounted(() => {
  getRoleList()
  getMenuList()
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
          <a-button ghost type="primary" :icon="h(PlusOutlined)" @click="handleAdd">新增</a-button>
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
          <a-button type="link" size="small" :icon="h(EditOutlined)" @click="handleEdit(record)">修改</a-button>
          <a-button type="link" size="small" :icon="h(DeleteOutlined)" @click="handleDelete(record.id)">删除</a-button>
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
    <!-- 添加修改菜单 -->
    <a-drawer :title="drawerTitle" placement="right" v-model:open="drawer" @close="onClose">
      <a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="角色名称" name="name">
          <a-input placeholder="请输入角色名称" v-model:value="formData.name"></a-input>
        </a-form-item>
        <a-form-item label="权限标识" name="roleKey">
          <a-input placeholder="请输入权限标识" v-model:value="formData.roleKey"></a-input>
        </a-form-item>
        <a-form-item label="角色标识" name="identify">
          <a-input placeholder="请输入角色标识" v-model:value="formData.identify"></a-input>
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea placeholder="请输入备注" type="textarea" :auto-size="{ minRows: 2, maxRows: 5 }"
            v-model:value="formData.remark"></a-textarea>
        </a-form-item>
        <!-- 菜单权限 -->
        <a-form-item label="菜单权限">
          <a-card size="small">
            <a-tree checkable v-model:checkedKeys="checkedKeys" :tree-data="treeData">
            </a-tree>
          </a-card>
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
