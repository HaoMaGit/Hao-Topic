<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import { apiGetUserList, apiGetRoleList } from '@/api/system/user/index'
import type { UserQueryType } from '@/api/system/user/type'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  DownloadOutlined,
  UploadOutlined,
  LoadingOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue';
import type { UploadChangeParam, UploadProps } from 'ant-design-vue';

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
}

// 定义查询参数
const params = ref<UserQueryType>({
  pageNum: 1,
  pageSize: 10,
  account: '',
  memberTime: '',
  roleName: '管理员',
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
    width: 110,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    align: 'center',
    width: 120,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    align: 'center',
    width: 100,
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
  params.value = {
    pageNum: 1,
    pageSize: 10,
    account: '',
    memberTime: '',
    roleName: '管理员',
    createTime: '',
  }
  total.value = 0
  activeKey.value = '管理员'
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
  params.value.roleName = key
  getUserList()

}

// 表单实例
const formRef = ref<any>(null)
// 表单数据
const formData = ref({
  account: '',
  memberTime: '',
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
  // formData.value = {
  //   name: '',
  //   remark: '',
  //   roleKey: '',
  //   identify: null,
  //   id: null,
  // }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 新增
const handleAdd = () => {
  drawer.value = true
  drawerTitle.value = '新增用户'
}

// 保存
const onSave = () => {
}

// 文件列表
const fileList = ref([]);
// 图片loading
const loading = ref<boolean>(false);
// 图片地址
const imageUrl = ref<string>('');

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
    // 获取图片地址
    getBase64(info.file.originFileObj, (base64Url: string) => {
      imageUrl.value = base64Url;
      loading.value = false;
    });
  }
  // 上传失败
  if (info.file.status === 'error') {
    loading.value = false;
    message.error('上传失败');
  }
};

// 获取图片地址
function getBase64(img: any, callback: (base64Url: string) => void) {
  const reader = new FileReader();
  reader.addEventListener('load', () => callback(reader.result as string));
  reader.readAsDataURL(img);
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
          <a-button @click="handleAdd" v-if="!operation" class="add-btn" type="primary"
            :icon="h(PlusOutlined)">新增</a-button>
        </a-space>
      </a-form-item>
    </a-space>
    <!-- 操作按钮 -->
    <a-form-item v-if="operation">
      <a-space>
        <a-button type="primary" :icon="h(PlusOutlined)" @click="handleAdd">新增</a-button>
        <a-button @mouseenter="isDangerHover = true" @mouseleave="isDangerHover = false" :danger="isDangerHover"
          :icon="h(DeleteOutlined)">删除</a-button>
        <a-button :icon="h(DownloadOutlined)">导出</a-button>
        <a-button :icon="h(UploadOutlined)">导入</a-button>
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
            class="avatar-uploader" :show-upload-list="false" action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
            :before-upload="beforeUpload" @change="handleChange">
            <img v-if="imageUrl" :src="imageUrl" alt="avatar" />
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item label="角色" name="roleName">
          <a-select v-model:value="formData.roleName" show-search placeholder="请选择角色" :options="roleListSelect">
          </a-select>
        </a-form-item>
        <a-form-item label="邮箱" name="email">
          <a-input placeholder="请输入邮箱" v-model:value="formData.email"></a-input>
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-switch v-model:checked="formData.status" checked-children="正常" un-checked-children="停用" />
        </a-form-item>
        <a-form-item label="会员时间" name="memberTime">
          <a-range-picker v-model:value="formData.memberTime" />
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
      width: 200px;
    }
  }
}

.space-footer-box {
  display: flex;
  justify-content: flex-end;
}
</style>