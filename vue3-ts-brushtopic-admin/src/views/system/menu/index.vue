<script setup lang="ts">
import { ref, onMounted, h, createVNode } from 'vue'
import { apiGetMenuList, apiAddMenu, apiDeleteMenu, apiUpdateMenu } from '@/api/system/menu/index'
import type { MenuType } from '@/api/system/menu/type'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons-vue';
// 导入所有图标
import * as Icons from '@ant-design/icons-vue'
import Modal from 'ant-design-vue/es/modal/Modal';
import { message } from 'ant-design-vue';

// 获取所有图标列表
const iconList = ref(
  Object.keys(Icons)
    .filter(key => key.endsWith('Outlined'))
    .map(key => ({
      value: key,
      label: key,
      icon: Icons[key as keyof typeof Icons]
    }))
)
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
    align: 'center',
    width: 180,
  },
  {
    title: '图标',
    dataIndex: 'icon',
    key: 'icon',
    align: 'center',
    width: 100,
  },
  {
    title: '排序',
    dataIndex: 'sorted',
    key: 'sorted',
    align: 'center',
    width: 100,
  },
  {
    title: '路径',
    dataIndex: 'route',
    key: 'route',
    align: 'center',
    width: 150,
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


// 表单实例
const formRef = ref<any>(null)
// 标题
const drawerTitle = ref('新增')
// 抽屉
const drawer = ref(false)
// 抽屉关闭
const onClose = () => {
  clearFormData()
  drawer.value = false
}

// 表单数据
const formData = ref<MenuType>({
  id: null,
  menuName: '',
  icon: '',
  route: '',
  sorted: null,
  parentId: 0
})
// 表单规则
const rules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  route: [{ required: true, message: '请输入路由路径', trigger: 'blur' }],
  icon: [{ required: true, message: '请选择图标', trigger: 'blur' }],
  sorted: [{ required: true, message: '请输入排序号', trigger: 'blur' }]
}
// 树形数据
const treeData = ref<any>([])
// 处理菜单数据为树形结构
const handleTreeData = (data: any) => {
  return data.map((item: any) => ({
    value: item.id,
    title: item.menuName,
    children: item.children ? handleTreeData(item.children) : []
  }))
}
// 获取树形菜单数据
const getTreeData = async () => {
  params.value = {
    menuName: '',
  }
  const res = await apiGetMenuList(params.value)
  // 添加顶层节点
  treeData.value = [{
    value: 0,
    title: '主类目',
    children: handleTreeData(res.data)
  }]
}

// 新增菜单
const handleAdd = () => {
  drawerTitle.value = '新增菜单'
  clearFormData()
  getTreeData()
  drawer.value = true
}
// 修改
const handleUpdate = (record: any) => {
  drawer.value = true
  formData.value = {
    ...record
  }
  getTreeData()
}

// 清除默认信息
const clearFormData = () => {
  formData.value = {
    menuName: '',
    id: null,
    icon: '',
    route: '',
    sorted: null,
    parentId: 0  // 默认选中主类目
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}
// 保存
const onSave = () => {
  formRef.value.validate().then(async () => {
    let mes = ''
    console.log(formData.value);
    try {
      // 判断是新增还是修改
      if (formData.value.id) {
        // 修改
        await apiUpdateMenu(formData.value)
        getMenuList()
        clearFormData()
        drawer.value = false
        mes = '修改菜单成功'
      } else {
        //  新增
        await apiAddMenu(formData.value)
        getMenuList()
        clearFormData()
        drawer.value = false
        mes = '新增菜单成功'
      }
      message.success(mes)
    } catch (error: any) {
      message.error(error.getMessage())
    }
  }).catch(() => {
    message.error('信息有误')
  })
}
// 删除菜单
const handleDelete = (id: number) => {
  Modal.confirm({
    title: '是否确认删除该菜单?',
    icon: createVNode(ExclamationCircleOutlined),
    content: createVNode('div', { style: 'color:red;' }, '删除菜单会导致相关页面丢失，请慎重考虑!'),
    onOk() {
      apiDeleteMenu(id).then(() => {
        getMenuList()
        clearFormData()
        drawer.value = false
      })
      message.success('删除成功')
    },
    onCancel() {
      console.log('Cancel');
    },
  });
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
          <a-button ghost type="primary" :icon="h(PlusOutlined)" @click="handleAdd">新增</a-button>
        </a-space>
      </a-form-item>
    </a-space>
    <!-- 表格 -->
    <a-table :pagination="false" :dataSource="tableData" :columns="columns">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operation'">
          <a-button type="link" size="small" :icon="h(PlusOutlined)" @click="handleAdd">新增</a-button>
          <a-button type="link" size="small" :icon="h(EditOutlined)" @click="handleUpdate(record)">修改</a-button>
          <a-button type="link" size="small" :icon="h(DeleteOutlined)" @click="handleDelete(record.id)">删除</a-button>
        </template>
        <!-- 处理icon -->
        <template v-else-if="column.key === 'icon'">
          <span v-if="record.icon">
            <component :is="record.icon" />
          </span>
        </template>
      </template>
    </a-table>
    <!-- 新增和修改 -->
    <a-drawer :title="drawerTitle" placement="right" v-model:open="drawer" @close="onClose">
      <a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="上级菜单" name="parentId">
          <a-tree-select v-model:value="formData.parentId" :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="treeData" placeholder="请选择上级菜单" allow-clear />
        </a-form-item>
        <a-form-item label="菜单名称" name="menuName">
          <a-input v-model:value="formData.menuName" placeholder="请输入菜单名称" />
        </a-form-item>
        <a-form-item label="路由路径" name="route">
          <a-input v-model:value="formData.route" placeholder="请输入路由路径" />
        </a-form-item>
        <a-form-item label="菜单图标" name="icon">
          <a-select v-model:value="formData.icon" show-search placeholder="请选择图标" :options="iconList" :filter-option="(input: string, option: any) =>
            option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0">
            <template #option="{ value, icon }">
              <span>
                <component :is="icon" /> {{ value }}
              </span>
            </template>
          </a-select>
        </a-form-item>
        <a-form-item label="菜单排序" name="sorted">
          <a-input-number style="width: 100%;" v-model:value="formData.sorted" :min="0" placeholder="菜单排序" />
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

// 添加抽屉标题样式
::v-deep(.ant-drawer-header) {
  flex-direction: row-reverse;
  justify-content: space-between;
  width: 100%;
  border: 1px solid red;
  color: red;
}
</style>
