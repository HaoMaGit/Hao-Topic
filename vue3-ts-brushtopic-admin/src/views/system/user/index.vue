<script setup lang="ts">
import { ref, onMounted, h, createVNode } from 'vue'
import { apiGetUserList, apiExportUser, apiGetRoleList, apiAddUser, apiUpdateUser, apiDeleteUser } from '@/api/system/user/index'
import type { UserQueryType } from '@/api/system/user/type'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  DownloadOutlined,
  UploadOutlined,
  LoadingOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue';
import type { UploadChangeParam, UploadProps } from 'ant-design-vue';
import { useUserStore } from '@/stores/modules/user';
import { addDateRange, clearDateRange } from '@/utils/date';
import Hao from '@/assets/images/H.png'
import FileSaver from 'file-saver'

const userStore = useUserStore()
// 获取上传路径
const { VITE_SERVE } = import.meta.env
// 头像上传路径
const uploadUrl = VITE_SERVE + '/api/system/user/avatar'
// 请求头
const headers = { authorization: userStore.token };


// 查询用户列表
const getUserList = async () => {
  tableLoading.value = true
  const res = await apiGetUserList(params.value)
  tableData.value = res.data.rows
  total.value = res.data.total
  tableLoading.value = false
}

// 查询角色列表
const getRoleList = async () => {
  const res = await apiGetRoleList()
  roleListSelect.value = res.data.map((item: any) => {
    return {
      label: item.roleName,
      value: item.roleName
    }
  })
  roleList.value = res.data.map((item: any) => item.roleName)
  roleList.value.unshift("全部")
}

// 定义查询参数
const params = ref<UserQueryType>({
  pageNum: 1,
  pageSize: 5,
  account: '',
  roleName: '',
  params: {}
})
// 时间
const createTimeDateRange = ref([]);
const memberTimeDateRange = ref([]);

// 总数量
const total = ref(0)
// 表格loading
const tableLoading = ref(false)
// 表格数据
const tableData = ref([])
// 角色数据
const roleList = ref(['管理员', '用户', '会员'])
// 当前选中角色
const activeKey = ref('全部')
// 选择角色列表
const roleListSelect = ref([])
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
    width: 100,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    align: 'center',
    width: 80,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    align: 'center',
    width: 80,
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
    align: 'center',
  }
]

// 是否移入删除按钮
const isDangerHover = ref(false)

// 搜索
const handleQuery = () => {
  if (createTimeDateRange.value && createTimeDateRange.value.length > 0) {
    params.value = addDateRange(params.value, createTimeDateRange.value, 'CreateTime')
  } else {
    params.value = clearDateRange(params.value, 'CreateTime')
  }
  if (memberTimeDateRange.value && memberTimeDateRange.value.length > 0) {
    params.value = addDateRange(params.value, memberTimeDateRange.value, 'MemberTime')
  } else {
    params.value = clearDateRange(params.value, 'MemberTime')
  }
  getUserList()
}

// 重置
const handleReset = () => {
  params.value = {
    pageNum: 1,
    pageSize: 5,
    account: '',
    roleName: '',
    params: null
  }
  memberTimeDateRange.value = []
  createTimeDateRange.value = []
  total.value = 0
  activeKey.value = '全部'
  getUserList()
}

// 选中数组
const onSelectedRowKeys = ref<number[]>([])

// 选中菜单
const onSelectChange = (selectedRowKeys: any) => {
  onSelectedRowKeys.value = selectedRowKeys
}

// 点击了tabs
const handleTabClick = (key: any) => {
  params.value = {
    pageNum: 1,
    pageSize: 5,
    account: '',
    roleName: '',
    params: null
  }
  memberTimeDateRange.value = []
  createTimeDateRange.value = []
  total.value = 0
  activeKey.value = key
  if (key == '全部') {
    params.value.roleName = ''
  } else {
    params.value.roleName = key
  }
  getUserList()

}

// 表单实例
const formRef = ref<any>(null)
// 表单数据
const formData = ref({
  account: '',
  password: null,
  roleName: '',
  id: null,
  avatar: '',
  email: '',
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
  roleName: [
    {
      required: true,
      message: '请选择角色',
      trigger: 'blur',
    },
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'blur',
    },
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
    account: '',
    password: null,
    roleName: '',
    id: null,
    avatar: '',
    email: '',
    status: null,
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
  createTimeDateRange.value = []
  memberTimeDateRange.value = []
}

// 新增
const handleAdd = () => {
  drawer.value = true
  drawerTitle.value = '新增用户'
}

// 修改
const handleEdit = (record: any) => {
  drawer.value = true
  console.log(record);
  drawerTitle.value = '修改用户'
  formData.value = {
    account: record.account,
    password: null,
    roleName: record.roleName,
    id: record.id,
    avatar: record.avatar,
    email: record.email,
    status: record.status,
  }
}
// 删除
const handleDelete = (record: any) => {
  Modal.confirm({
    title: '是否确认删除该用户?',
    icon: createVNode(ExclamationCircleOutlined),
    content: createVNode('div', { style: 'color:red;' }, '删除用户会导致相关用户功能丢失，请慎重考虑!'),
    async onOk() {
      if (record.id) {
        onSelectedRowKeys.value.push(record.id)
      }
      // 判断是否为当前用户
      if (record.account === userStore.userInfo.account) {
        message.error('不能删除自己')
        return
      }
      await apiDeleteUser(onSelectedRowKeys.value)
      getRoleList()
      getUserList()
      clearFormData()
      message.success('删除成功')
    },
    onCancel() {
      console.log('Cancel');
    },
  });
}

// 保存
const onSave = () => {
  formRef.value.validate().then(async () => {
    let mes = ''
    // 判断是新增还是修改
    if (formData.value.id) {
      mes = '修改用户成功'
      await apiUpdateUser(formData.value)
    } else {
      mes = '新增用户成功'
      await apiAddUser(formData.value)
    }
    getRoleList()
    getUserList()
    drawer.value = false
    message.success(mes)
    if (formData.value.account == userStore.userInfo.account) {
      userStore.logout()
    }
    clearFormData()
  })
}

// 文件列表
const fileList = ref([]);
// 图片loading
const loading = ref<boolean>(false);

/**
 * 文件校验
 * @param file 
 */
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
const beforeUpload = (file: UploadProps['fileList'][number]) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message.error('图片格式错误');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('图片大小不能超过2M');
  }
  return isJpgOrPng && isLt2M;
};

/**
 * 图片上传
 * @param info 
 */
const handleChange = (info: UploadChangeParam) => {
  // 判断是否上传中
  if (info.file.status === 'uploading') {
    loading.value = true;
    return;
  }
  // 上传完成
  if (info.file.status === 'done') {
    formData.value.avatar = info.file.response.data;
    loading.value = false;
  }
  // 上传失败
  if (info.file.status === 'error') {
    loading.value = false;
    message.error('上传失败');
  }
};

// 图片
const handleErrorImg = (event: any) => {
  event.target.src = Hao
};

// 分页变化处理
const handleTableChange = (pagination: any) => {
  params.value.pageNum = pagination.current;
  params.value.pageSize = pagination.pageSize;
  getUserList();
}

// 导出数据
const handleExport = async () => {
  tableLoading.value = true
  let response: any = null
  // 判断是否有数据
  if (onSelectedRowKeys.value.length > 0) {
    // 导出excel数据
    response = await apiExportUser(params.value, onSelectedRowKeys.value)
  } else {
    // 导出excel数据
    response = await apiExportUser(params.value, [0])
    // 下载下来
  }
  FileSaver.saveAs(response, `易题后台用户数据_${new Date().getTime()}.xlsx`) // 下载文件
  message.success('导出成功')
  tableLoading.value = false
}

// 导入数据
const handleImport = () => {
  // Modal.confirm({
  //   title: '是否确认导入用户?',
  //   icon: createVNode(ExclamationCircleOutlined),
  //   content: createVNode('div', { style: 'color:red;' }, '导入用户会导致相关用户功能丢失，请慎重考虑!'),
  //   async onOk() {
  //     await apiImportUser()
  //     getRoleList()
  //     getUserList()
  //   }
  // })
}

onMounted(() => {
  getUserList()
  getRoleList()
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
            <a-range-picker class="range-picker" v-model:value="createTimeDateRange" />
          </a-form-item>
          <a-form-item label="会员时间">
            <a-range-picker class="range-picker" v-model:value="memberTimeDateRange" />
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
    <!-- 操作按钮 -->
    <a-form-item>
      <a-space>
        <a-button type="primary" :icon="h(PlusOutlined)" @click="handleAdd">新增</a-button>
        <a-button :disabled="onSelectedRowKeys.length == 0" @mouseenter="isDangerHover = true"
          @mouseleave="isDangerHover = false" :danger="isDangerHover" :icon="h(DeleteOutlined)"
          @click="handleDelete(null)">删除</a-button>
        <a-button :disabled="tableData.length == 0" :icon="h(DownloadOutlined)" @click="handleExport">导出</a-button>
        <a-button :icon="h(UploadOutlined)" @click="handleImport">导入</a-button>
      </a-space>
    </a-form-item>
    <a-tabs :style="{ height: '500px' }" @tabClick="handleTabClick" v-model:activeKey="activeKey" tab-position="left">
      <a-tab-pane v-for="item in roleList" :key="item" :tab="item">
        <a-table :pagination="{
          current: params.pageNum,
          pageSize: params.pageSize,
          total: total,
          showTotal: (total: any) => `共 ${total} 条`,
          showSizeChanger: true,
        }" @change="handleTableChange" :loading="tableLoading" :dataSource="tableData" :columns="columns" rowKey="id"
          :row-selection="{ selectedRowKeys: onSelectedRowKeys, onChange: onSelectChange, fixed: true }">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'operation'">
              <a-button type="link" size="small" :icon="h(EditOutlined)" @click="handleEdit(record)">修改</a-button>
              <a-button type="link" size="small" :icon="h(DeleteOutlined)" @click="handleDelete(record)">删除</a-button>
            </template>
            <template v-if="column.key === 'avatar'">
              <img class="user-avatar" :src="record.avatar != null ? record.avatar : 'Hao'" @error="handleErrorImg"
                alt="" srcset="">
            </template>
            <template v-if="column.key === 'status'">
              <span>{{ record.status === 0 ? '正常' : '停用' }}</span>
            </template>
            <template v-if="column.key === 'email'">
              <a-button type="link">{{ record.email }}</a-button>
            </template>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <!-- 新增修改  -->
    <a-drawer :title="drawerTitle" placement="right" v-model:open="drawer" @close="onClose">
      <a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="账户" name="account">
          <a-input placeholder="请输入账户名称" v-model:value="formData.account"></a-input>
        </a-form-item>
        <a-form-item label="头像" name=avatar>
          <a-upload maxCount="1" v-model:file-list="fileList" name="avatar" list-type="picture-card"
            class="avatar-uploader" :show-upload-list="false" :headers="headers" :action="uploadUrl"
            :before-upload="beforeUpload" @change="handleChange">
            <img v-if="formData.avatar" :src="formData.avatar" alt="avatar" class="avatar" />
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item v-if="!formData.id" label="密码" name="password">
          <a-input placeholder="请输入密码" v-model:value="formData.password"></a-input>
        </a-form-item>
        <a-form-item label="角色" name="roleName">
          <a-select v-model:value="formData.roleName" show-search placeholder="请选择角色" :options="roleListSelect">
          </a-select>
        </a-form-item>
        <a-form-item label="邮箱" name="email">
          <a-input placeholder="请输入邮箱" v-model:value="formData.email"></a-input>
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-switch v-model:checked="formData.status" :checkedValue="0" :unCheckedValue="1" checked-children="正常"
            un-checked-children="停用" />
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

    .range-picker {
      width: 240px;
    }
  }
}

.space-footer-box {
  display: flex;
  justify-content: flex-end;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 5%;
}

.user-avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
}
</style>